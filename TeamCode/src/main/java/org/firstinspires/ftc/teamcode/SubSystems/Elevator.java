package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Elevator {

    double full_spin=5;
    double start=0;
    DcMotor motor1;
    DcMotor motor2;

    public void init(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotor.class, "motor1_elv");
        motor2 = hardwareMap.get(DcMotor.class, "motor2_elv");

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor1.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void Up(){

            motor1.setTargetPosition((int)full_spin);
            motor2.setTargetPosition((int)full_spin-3);


            motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motor1.setPower(0.3);
            motor2.setPower(0.3);

    }



    public void Down(){



            motor1.setTargetPosition((int)start);
            motor2.setTargetPosition((int)start);

            motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motor1.setPower(0.3);
            motor2.setPower(0.3);

    }


}
