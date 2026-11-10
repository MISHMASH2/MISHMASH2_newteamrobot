package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class IntakeSubsystem {
    DcMotor intake;

    DcMotor transfer;

    public void init(@NonNull HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void basicFeed() {
        intake.setPower(1);
        transfer.setPower(0.2);

    }

    public void outPut() {
        intake.setPower(-1);
        transfer.setPower(-0.2);

    }

    public void stop() {
        intake.setPower(0);
        transfer.setPower(0);
    }
}

