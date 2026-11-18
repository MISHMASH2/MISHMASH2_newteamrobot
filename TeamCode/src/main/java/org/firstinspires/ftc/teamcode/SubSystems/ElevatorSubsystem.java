package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ElevatorSubsystem {

    // יעדים קבועים בטיקים, ביחס לתחתית הפיזית שבה מאפסים.
    private static final int MIN_TICKS = 0;
    private static final int MOTOR1_MAX_TICKS = 1100;
    private static final int MOTOR2_MAX_TICKS = 1209;
    private static final double MOVE_POWER = 0.7;
    private DcMotor motor1;
    private DcMotor motor2;

    public void init(HardwareMap hardwareMap) {
        // הגדרת חומרה: שני השמות חייבים להתאים בדיוק לשמות בקונפיגורציה.
        motor1 = hardwareMap.get(DcMotor.class, "motor1_elv");//חיבור 1
        motor2 = hardwareMap.get(DcMotor.class, "motor2_elv");//חיבור
        // אותם כיוונים שבהם נמדדו יעדי האנקודרים בבדיקת המעלית.
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);
        stop();

        // חובה להציב את המעלית בתחתית הפיזית לפני INIT: שם נקבע האפס.
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void up() {
        moveTo(MOTOR1_MAX_TICKS, MOTOR2_MAX_TICKS);
    }

    public void Down() {
        moveTo(MIN_TICKS, MIN_TICKS);
    }

    private void moveTo(int motor1Ticks, int motor2Ticks) {
        // לכל מנוע יעד וגבול משלו, ביחס לאפס שנקבע בתחתית.
        motor1.setTargetPosition(Math.max(MIN_TICKS, Math.min(MOTOR1_MAX_TICKS, motor1Ticks)));
        motor2.setTargetPosition(Math.max(MIN_TICKS, Math.min(MOTOR2_MAX_TICKS, motor2Ticks)));
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor1.setPower(MOVE_POWER);
        motor2.setPower(MOVE_POWER);
    }

    public void stop() {
        motor1.setPower(0);
        motor2.setPower(0);
    }
}
