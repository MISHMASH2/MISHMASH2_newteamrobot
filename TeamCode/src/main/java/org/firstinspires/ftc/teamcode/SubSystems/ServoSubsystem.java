package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoSubsystem {
    // אחסון נקודות עבור מצב בדיקת הכיול בלבד.
    public static final String CALIBRATION_PREFERENCES = "servo_test_calibration";
    public static final String OPEN_POSITION_KEY = "r1_point_percent";
    public static final String CLOSED_POSITION_KEY = "l1_point_percent";

    // יעדי העבודה שנמדדו בכיול: מיקומים מנורמלים, לא מעלות.
    public static final double OPEN_POSITION = 0.76;
    public static final double CLOSED_POSITION = 0.24;
    // קצב שינוי הפקודה ביחידות מיקום לשנייה, לא עוצמת מנוע.
    public static final double MOVEMENT_SPEED = 0.8;

    private Servo servo;
    private double commandedPosition;
    private double targetPosition;
    private boolean open;
    private boolean moving;
    private long previousUpdateNanos;

    public void init(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "servo");
        double lastPosition = servo.getPosition();
        commandedPosition = lastPosition >= 0.0 && lastPosition <= 1.0
                ? lastPosition : CLOSED_POSITION;
        targetPosition = commandedPosition;
        // אין תנועה ב-INIT. הלחיצה הראשונה בוחרת את יעד הפתיחה.
        open = false;
        moving = false;
        previousUpdateNanos = 0;
    }

    public void open() {
        targetPosition = OPEN_POSITION;
        open = true;
        moving = true;
    }

    public void close() {
        targetPosition = CLOSED_POSITION;
        open = false;
        moving = true;
    }

    public void toggle() {
        if (open) {
            close();
        } else {
            open();
        }
    }

    // קוראים בכל איטרציה; התנועה אינה חוסמת נהיגה או שליטה במנגנונים אחרים.
    public void update() {
        long now = System.nanoTime();
        double elapsedSeconds = previousUpdateNanos == 0
                ? 0.0 : Math.min((now - previousUpdateNanos) / 1_000_000_000.0, 0.1);
        previousUpdateNanos = now;
        if (!moving) {
            return;
        }

        double difference = targetPosition - commandedPosition;
        double maxStep = MOVEMENT_SPEED * elapsedSeconds;
        if (Math.abs(difference) <= maxStep) {
            commandedPosition = targetPosition;
            moving = false;
        } else {
            commandedPosition += Math.copySign(maxStep, difference);
        }
        servo.setPosition(commandedPosition);
    }

    // מציין את היעד המבוקש, ולא אישור הגעה מחיישן.
    public boolean isOpen() {
        return open;
    }

    public double getOpenPosition() {
        return OPEN_POSITION;
    }

    public double getClosedPosition() {
        return CLOSED_POSITION;
    }

    // פקודה ישירה עבור בדיקת הכיול; מבטלת תנועה הדרגתית פעילה.
    public void setPosition(double position) {
        commandedPosition = Math.max(0.0, Math.min(1.0, position));
        targetPosition = commandedPosition;
        moving = false;
        servo.setPosition(commandedPosition);
    }

    // מחזיר את המיקום המבוקש האחרון, ולא מדידה מחיישן.
    public double getPosition() {
        return servo.getPosition();
    }
}
