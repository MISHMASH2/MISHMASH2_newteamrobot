package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intakeTestMotor {
    private DcMotor intake;
    // שומרים את העוצמה באחוזים כדי שכל צעד של 0.01 יהיה מדויק.
    private int powerPercent;
    private boolean previousY;
    private boolean previousX;
    private boolean previousUp;
    private boolean previousDown;

    public void init(@NonNull HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intakeT");
        stop();
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        powerPercent = 0;
        previousY = false;
        previousX = false;
        previousUp = false;
        previousDown = false;
    }

    public void update(@NonNull Gamepad gamepad) {
        int change = 0;
        // שינוי אחד לכל לחיצה, גם אם ממשיכים להחזיק את הכפתור.
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

        powerPercent = Math.max(0, Math.min(100, powerPercent + change));
        previousY = gamepad.y;
        previousX = gamepad.x;
        previousUp = gamepad.dpad_up;
        previousDown = gamepad.dpad_down;

        // R2 משמש כפתור הפעלה; עוצמת הלחיצה אינה משנה את העוצמה שנבחרה.
        if (gamepad.right_trigger > 0.1) {
            basicFeed();
        } else {
            stop();
        }
    }

    public double getSelectedPower() {
        return powerPercent / 100.0;
    }

    public double getAppliedPower() {
        return intake.getPower();
    }

    public void basicFeed() {
        intake.setPower(getSelectedPower());
    }

    public void stop() {
        intake.setPower(0);
    }
}
