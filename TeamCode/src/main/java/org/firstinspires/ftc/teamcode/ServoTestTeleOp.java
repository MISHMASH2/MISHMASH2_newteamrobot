package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubSystems.ServoTestSubsystem;

@TeleOp(name = "Servo Calibration Test", group = "Test")
public class ServoTestTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        ServoTestSubsystem servo = new ServoTestSubsystem();
        servo.init(hardwareMap);

        telemetry.addLine("Gamepad 1 | Hardware name: servo");
        telemetry.addLine("Y +0.10 | X -0.10 | Up +0.01 | Down -0.01");
        telemetry.addLine("Hold R2: move forward | Hold L2: move backward (starts at 0.50)");
        telemetry.addLine("R1: save OPEN | L1: save CLOSED (kept across runs)");
        telemetry.addLine("MainTeleOp: D-pad up toggles fixed OPEN 0.76 / CLOSED 0.24");
        telemetry.addLine("Position range: 0.00 to 1.00, not degrees");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            servo.update(gamepad1);

            telemetry.addData("Selected position", "%.2f", servo.getSelectedPosition());
            if (servo.hasAppliedPosition()) {
                telemetry.addData("Last commanded position", "%.2f", servo.getAppliedPosition());
            } else {
                telemetry.addData("Last commanded position", "Press R2 or L2 before saving");
            }
            if (servo.hasR1Point()) {
                telemetry.addData("Saved OPEN (R1)", "%.2f", servo.getR1Point());
            } else {
                telemetry.addData("Saved OPEN (R1)", "Not saved");
            }
            if (servo.hasL1Point()) {
                telemetry.addData("Saved CLOSED (L1)", "%.2f", servo.getL1Point());
            } else {
                telemetry.addData("Saved CLOSED (L1)", "Not saved");
            }
            telemetry.addLine("Y +0.10 | X -0.10 | Up +0.01 | Down -0.01");
            telemetry.addLine("Hold R2: forward | Hold L2: backward");
            telemetry.addLine("R1: save OPEN | L1: save CLOSED");
            telemetry.addLine("Release triggers: hold position | Both triggers: hold position");
            telemetry.update();
            idle();
        }
    }
}
