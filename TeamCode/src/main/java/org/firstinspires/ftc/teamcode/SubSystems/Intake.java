package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    DcMotor intake;

    public void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void basicFeed(){
        intake.setPower(1);
    }
    public void outPut(){
        intake.setPower(-1);
    }
    public void stop(){
        intake.setPower(0);
    }
}
