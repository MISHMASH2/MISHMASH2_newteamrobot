package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubSystems.ElevatorTestSubsystem;

@TeleOp(name = "Elevator Encoder Test", group = "Test")
public class ElevatorEncoderTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        ElevatorTestSubsystem elevator = new ElevatorTestSubsystem();
        elevator.init(hardwareMap);

        try {
            telemetry.addLine("Place elevator at bottom before INIT: encoders reset to 0");
            telemetry.addLine("Gamepad 1: hold Up / Down to move at the configured test power");
            telemetry.addLine("Release to stop | X: reset encoders while stopped at bottom");
            telemetry.addLine("Manual test: no automatic height limits");
            telemetry.addData("motor1_elv ticks", elevator.getMotor1Ticks());
            telemetry.addData("motor2_elv ticks", elevator.getMotor2Ticks());
            telemetry.update();

            waitForStart();

            while (opModeIsActive()) {
                elevator.update(gamepad1);

                telemetry.addData("motor1_elv ticks", elevator.getMotor1Ticks());
                telemetry.addData("motor2_elv ticks", elevator.getMotor2Ticks());
                telemetry.addData("Power", "%.2f", elevator.getAppliedPower());
                telemetry.addLine("Hold Up / Down | Release: stop | X: zero at bottom");
                telemetry.update();
                idle();
            }
        } finally {
            elevator.stop();
        }
    }
}
