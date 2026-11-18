package org.firstinspires.ftc.teamcode.SubSystems;

import android.content.Context;
import android.content.SharedPreferences;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ServoTestSubsystem {
    // R1 שומר נקודת פתיחה לבדיקה; L1 שומר נקודת סגירה לבדיקה.
    private static final String R1_POINT_KEY = ServoSubsystem.OPEN_POSITION_KEY;
    private static final String L1_POINT_KEY = ServoSubsystem.CLOSED_POSITION_KEY;

    // מהירות כיול: מעבר של 0.20 בטווח המיקום בכל שנייה.
    private static final double MOVE_PERCENT_PER_SECOND = 20.0;

    private final ServoSubsystem servo = new ServoSubsystem();
    private SharedPreferences calibration;
    private double selectedPercent;
    private long previousUpdateNanos;
    private int appliedPercent;
    private int r1PointPercent;
    private int l1PointPercent;
    private boolean previousY;
    private boolean previousX;
    private boolean previousUp;
    private boolean previousDown;
    private boolean previousR1;
    private boolean previousL1;

    public void init(HardwareMap hardwareMap) {
        servo.init(hardwareMap);
        calibration = hardwareMap.appContext.getSharedPreferences(
                ServoSubsystem.CALIBRATION_PREFERENCES, Context.MODE_PRIVATE);
        r1PointPercent = calibration.getInt(R1_POINT_KEY, -1);
        l1PointPercent = calibration.getInt(L1_POINT_KEY, -1);

        // אין תנועה ב-INIT. R2 מגדיל מיקום ו-L2 מקטין מיקום.
        selectedPercent = 50;
        appliedPercent = -1;
        previousUpdateNanos = 0;
        previousY = false;
        previousX = false;
        previousUp = false;
        previousDown = false;
        previousR1 = false;
        previousL1 = false;
    }

    public void update(Gamepad gamepad) {
        long now = System.nanoTime();
        // הגבלת הזמן מונעת קפיצה במיקום לאחר עיכוב בלולאה.
        double elapsedSeconds = previousUpdateNanos == 0
                ? 0.0 : Math.min((now - previousUpdateNanos) / 1_000_000_000.0, 0.1);
        previousUpdateNanos = now;

        int change = 0;
        if (gamepad.y && !previousY) {
            change += 10;
        }
        if (gamepad.x && !previousX) {
            change -= 10;
        }
        if (gamepad.dpad_up && !previousUp) {
            change += 1;
        }
        if (gamepad.dpad_down && !previousDown) {
            change -= 1;
        }
        selectedPercent = Math.max(0, Math.min(100, selectedPercent + change));

        boolean forward = gamepad.right_trigger > 0.1;
        boolean backward = gamepad.left_trigger > 0.1;
        // לחיצה על שניהם יחד משאירה את המיקום האחרון.
        if (forward != backward) {
            double direction = forward ? 1.0 : -1.0;
            selectedPercent = Math.max(0.0, Math.min(100.0,
                    selectedPercent + direction * MOVE_PERCENT_PER_SECOND * elapsedSeconds));
            appliedPercent = (int) Math.round(selectedPercent);
            servo.setPosition(getAppliedPosition());
        }

        // שומרים את הפקודה האחרונה שנשלחה, ולא יעד שנבחר בלי להפעיל.
        // זה אינו מיקום שנמדד בחיישן; הסרוו עשוי עדיין להיות בתנועה.
        boolean saveR1 = gamepad.right_bumper && !previousR1;
        boolean saveL1 = gamepad.left_bumper && !previousL1;
        if (hasAppliedPosition() && (saveR1 || saveL1)) {
            SharedPreferences.Editor editor = calibration.edit();
            if (saveR1) {
                r1PointPercent = appliedPercent;
                editor.putInt(R1_POINT_KEY, r1PointPercent);
            }
            if (saveL1) {
                l1PointPercent = appliedPercent;
                editor.putInt(L1_POINT_KEY, l1PointPercent);
            }
            editor.apply();
        }

        previousY = gamepad.y;
        previousX = gamepad.x;
        previousUp = gamepad.dpad_up;
        previousDown = gamepad.dpad_down;
        previousR1 = gamepad.right_bumper;
        previousL1 = gamepad.left_bumper;
    }

    public double getSelectedPosition() {
        return Math.round(selectedPercent) / 100.0;
    }

    public boolean hasAppliedPosition() {
        return appliedPercent >= 0;
    }

    public double getAppliedPosition() {
        return appliedPercent / 100.0;
    }

    public boolean hasR1Point() {
        return r1PointPercent >= 0;
    }

    public double getR1Point() {
        return r1PointPercent / 100.0;
    }

    public boolean hasL1Point() {
        return l1PointPercent >= 0;
    }

    public double getL1Point() {
        return l1PointPercent / 100.0;
    }
}
