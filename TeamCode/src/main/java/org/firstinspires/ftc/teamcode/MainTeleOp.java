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
            telemetry.addData("סרוו", "טרם הופעל");
            telemetry.update();
            waitForStart();
            boolean previousUp = gamepad1.dpad_up;
            boolean servoCommanded = false;

            while (opModeIsActive()) {
                dt.drive(gamepad1);

                // PS5: משולש בוחר יעדים 1185/1209, איקס מחזיר לבסיס 0/0.
                // RUN_TO_POSITION ממשיך ליעד גם לאחר שחרור הכפתור.
                if (gamepad1.y && !gamepad1.a) {
                    elevator.up();
                } else if (gamepad1.a && !gamepad1.y) {
                    elevator.Down();
                }

                // החלפה אחת לכל לחיצה על חץ למעלה, גם כאשר מחזיקים את הכפתור.
                if (gamepad1.dpad_up && !previousUp) {
                    servo.toggle();
                    servoCommanded = true;
                }
                previousUp = gamepad1.dpad_up;
                servo.update();

                // X = square, B = circle.
                if (gamepad1.x) {
                    intake.basicFeed();
                } else if (gamepad1.b) {
                    intake.outPut();
                } else {
                    intake.stop();
                }

                telemetry.addData("סרוו", servoCommanded
                        ? (servo.isOpen() ? "פתוח" : "סגור") : "טרם הופעל");
                telemetry.update();
                idle();
            }
        } finally {
            dt.stop();
            intake.stop();
            elevator.stop();
        }
    }
}
