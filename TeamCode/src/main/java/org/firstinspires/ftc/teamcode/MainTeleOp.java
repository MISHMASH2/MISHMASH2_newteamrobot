package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.SubSystems.IntakeSubsystem;

@TeleOp
public class MainTeleOp extends LinearOpMode {

    @Override
    public void runOpMode(){

     IntakeSubsystem intake = new IntakeSubsystem();
     intake.init(hardwareMap);

     waitForStart();
     while (opModeIsActive()){
         if (gamepad1.a)
            intake.basicFeed();
         else if (gamepad1.b)
            intake.outPut();
          else intake.stop();
     }
    }
}
