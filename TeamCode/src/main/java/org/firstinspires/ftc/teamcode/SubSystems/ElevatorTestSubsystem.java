package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ElevatorTestSubsystem {
    // בדיקת תנועה ידנית בלבד, ללא יעדי גובה או גבולות אוטומטיים.
    private static final double TEST_POWER = 0.7;

    private DcMotor motor1;
    private DcMotor motor2;
    private boolean previousX;

    public void init(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotor.class, "motor1_elv");
        motor2 = hardwareMap.get(DcMotor.class, "motor2_elv");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);

        stop();
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // יש להציב בתחתית לפני INIT כדי שמדידת הגובה תתחיל מאפס.
        resetEncoders();
        previousX = false;
    }

    public void update(Gamepad gamepad) {
        double power = 0.0;
        if (gamepad.dpad_up && !gamepad.dpad_down) {
            power = TEST_POWER;
        } else if (gamepad.dpad_down && !gamepad.dpad_up) {
            power = -TEST_POWER;
        }

        motor1.setPower(power);
        motor2.setPower(power);

        // איפוס רק בלחיצה חדשה וללא פקודת תנועה; לבצע בתחתית לאחר עצירה.
        if (gamepad.x && !previousX && power == 0.0) {
            resetEncoders();
        }
        previousX = gamepad.x;
    }

    private void resetEncoders() {
        stop();
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // טיקים נקראים גם במצב ידני; אין תנועה אוטומטית ליעד.
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public int getMotor1Ticks() {
        return motor1.getCurrentPosition();
    }

    public int getMotor2Ticks() {
        return motor2.getCurrentPosition();
    }

    public double getAppliedPower() {
        return motor1.getPower();
    }

    public void stop() {
        motor1.setPower(0.0);
        motor2.setPower(0.0);
    }
}
