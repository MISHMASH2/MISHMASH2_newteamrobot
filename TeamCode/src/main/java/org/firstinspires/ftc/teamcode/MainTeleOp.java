package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SubSystems.DriveTrain;
import org.firstinspires.ftc.teamcode.SubSystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.SubSystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.SubSystems.ServoSubsystem;

@TeleOp
public class MainTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        IntakeSubsystem intake = new IntakeSubsystem();
        intake.init(hardwareMap);

        DriveTrain dt = new DriveTrain();
        dt.init(hardwareMap);

        ElevatorSubsystem elevator = new ElevatorSubsystem();
        elevator.init(hardwareMap);

        ServoSubsystem servo = new ServoSubsystem();
        servo.init(hardwareMap);

        try {
            waitForStart();

            while (opModeIsActive()) {
                dt.drive(gamepad1);

                // Y = triangle, A = cross (X).
                if (gamepad1.y && !gamepad1.a) {
                    elevator.up();
                } else if (gamepad1.a && !gamepad1.y) {
                    elevator.Down();
                } else {
                    elevator.stop();
                }

                // R1 opens, L1 closes; releasing keeps the last position.
                if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                    servo.open();
                } else if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                    servo.close();
                }

                // X = square, B = circle.
                if (gamepad1.x) {
                    intake.basicFeed();
                } else if (gamepad1.b) {
                    intake.outPut();
                } else {
                    intake.stop();
                }

            }
        } finally {
            dt.stop();
            intake.stop();
            elevator.stop();
        }
    }
}
