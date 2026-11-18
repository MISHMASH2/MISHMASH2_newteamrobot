package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubSystems.intakeTestMotor;

@TeleOp(name = "Intake Motor Test", group = "Test")
public class teleOPTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        intakeTestMotor intake = new intakeTestMotor();
        intake.init(hardwareMap);

        try {
            telemetry.addLine("Gamepad 1: Y +0.10 | X -0.10");
            telemetry.addLine("D-pad up +0.01 | down -0.01");
            telemetry.addLine("Hold R2 to run the intake. Starting power: 0.00");
            telemetry.update();

            waitForStart();

            while (opModeIsActive()) {
                intake.update(gamepad1);

                telemetry.addData("Selected power", "%.2f", intake.getSelectedPower());
                telemetry.addData("Applied power", "%.2f", intake.getAppliedPower());
                telemetry.addLine("Y +0.10 | X -0.10 | Up +0.01 | Down -0.01");
                telemetry.addLine("Hold R2 to run");
                telemetry.update();
                idle();
            }
        } finally {
            intake.stop();
        }
    }
}
