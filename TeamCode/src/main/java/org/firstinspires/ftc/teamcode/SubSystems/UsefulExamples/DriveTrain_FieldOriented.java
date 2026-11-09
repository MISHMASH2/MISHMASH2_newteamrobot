package org.firstinspires.ftc.teamcode.SubSystems.UsefulExamples; // שימו לב: חובה שהתיקייה תיקרא באנגלית

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/*
 * ==========================================================
 * דוגמה: נסיעת Mecanum חכמה ביחס למגרש (Field Centric)
 * ==========================================================
 * מה זה אומר?
 * בנסיעה רגילה: דוחפים סטיק קדימה -> הרובוט נוסע לאן שה"פנים" שלו מכוונות.
 * בנסיעה חכמה (Field Oriented): דוחפים סטיק קדימה -> הרובוט תמיד נוסע
 * לכיוון הקהל/הלוחות, גם אם הוא כרגע מסובב הצידה או אחורה!
 * זה מוריד המון לחץ מהנהגים בתחרות כי לא צריך לחשב בראש "לאן הרובוט פונה עכשיו?".
 */

@TeleOp(name = "Robot: Field Relative Mecanum Drive", group = "Examples")
// @Disabled // מיה: אם אנחנו רוצים שהקוד הזה יופיע ב-Driver Hub (המסך של הנהגים) כדי לבדוק אותו בחיים האמיתיים, חייבים למחוק את השורה הזאת!
public class DriveTrain_FieldOriented extends OpMode {

    // 1. הגדרת משתנים - פה אנחנו אומרים לקוד אילו מנועים יש לנו בשלדה
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;

    // 2. הגדרת חיישן ה-IMU. זה בעצם הג'יירו הפנימי שיושב בתוך ה-Control Hub (הקופסה השחורה על הרובוט). הוא יודע לזהות לאן הרובוט מסתובב.
    IMU imu;

    @Override
    public void init() {
        // קישור המשתנים לשמות הפיזיים!
        // חובה: השמות במרכאות ("front_left_drive") חייבים להיות זהים 1 ל-1 למה שהגדרתם בקונפיגורציה ב-Driver Hub!
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        // הפיכת כיוון למנועים השמאליים.
        // בשלדות כמו הגו-בילדה שלנו, המנועים בצד שמאל מורכבים פיזית הפוך מאלה שבצד ימין.
        // אם לא נהפוך אותם בקוד, הרובוט פשוט יסתובב במקום כשתתנו גז קדימה.
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        // שימוש באנקודרים (החיישן השחור הקטן שיושב על המנוע וסופר סיבובים).
        // זה עוזר לרובוט לנסוע ישר יותר ובמהירות יציבה, במיוחד כשהסוללה מתחילה לרדת.
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // מדליקים את חיישן הג'יירו
        imu = hardwareMap.get(IMU.class, "imu");

        // שלב קריטי לצוות המכניקה: איך הרכבתם את ה-Control Hub על הרובוט?
        // הקוד חייב לדעת כדי שהג'יירו יעבוד נכון!
        // LogoFacingDirection: לאיזה כיוון הפנים של לוגו ה-REV מסתכלות? (למעלה/למטה/הצידה)
        // UsbFacingDirection: לאיזה כיוון חיבורי ה-USB מצביעים? (קדימה/אחורה)
        // שנו את ה-UP וה-FORWARD פה לפי איך שהקופסה מורכבת אצלכם.
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    @Override
    public void loop() {
        // טלמטריה - אלה ההודעות שמופיעות על המסך של ה-Driver Hub (הטאבלט של הנהגים).
        // טוב בשביל לראות נתונים בלייב בזמן נסיעה או להשאיר הוראות לנהג.
        telemetry.addLine("Press A to reset Yaw (לאפס את הזווית)");
        telemetry.addLine("Hold left bumper to drive in robot relative (לנסוע רגיל)");

        // איפוס הג'יירו ("איפוס צפון").
        // כל פעם שמתחילים מקצה, הנהג חייב להעמיד את הרובוט ישר וללחוץ A.
        // ככה הרובוט לומד שזה ה"קדימה" הקבוע של המגרש.
        if (gamepad1.a) {
            imu.resetYaw();
        }

        // איך הנהג שולט?
        // אם הנהג מחזיק את הכפתור השמאלי העליון בשלט (Left Bumper), הרובוט חוזר לנסיעה רגילה וטיפשה (הקדימה של הרובוט זה קדימה).
        if (gamepad1.left_bumper) {
            drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        } else {
            // אם לא לוחצים על ה-Bumper (זה המצב הרגיל), מפעילים את הפונקציה החכמה - נסיעה ביחס למגרש.
            driveFieldRelative(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
    }

    // ==========================================================
    // קסם המתמטיקה: פונקציה שהופכת תנועת ג'ויסטיק לנסיעה חכמה במגרש
    // ==========================================================
    private void driveFieldRelative(double forward, double right, double rotate) {
        // 1. לוקחים את הכיוון שהנהג דחף אליו את הג'ויסטיק, ומחשבים מה הזווית והעוצמה.
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // 2. שואלים את חיישן ה-IMU: "באיזו זווית הרובוט נמצא עכשיו?", ומחסרים את זה מהבקשה של הנהג.
        // ככה אנחנו בעצם "מבטלים" את העובדה שהרובוט מסובב הצידה.
        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // 3. מתרגמים את הזווית החדשה חזרה למספרים שהמנועים יכולים להבין.
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // 4. שולחים את המספרים המתוקנים לפונקציית הנסיעה הרגילה.
        drive(newForward, newRight, rotate);
    }

    // ==========================================================
    // פונקציית הנסיעה הבסיסית לשלדת גלגלי הולונומיק / Mecanum
    // ==========================================================
    public void drive(double forward, double right, double rotate) {
        // חישוב מתמטי פשוט שאומר לכל אחד מ-4 המנועים כמה כוח לתת כדי שהרובוט גם ייסע קדימה, גם יחליק הצידה (Strafe) וגם יסתובב - הכל באותו זמן!
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 1.0;
        // רוצים נהיגה רגועה יותר לאימונים או הצגות להורים? שנו את maxSpeed למשל ל-0.5.
        double maxSpeed = 1.0;

        // הגנה על המנועים: מנוע מקבל כוח בין 0 ל-1.0 (שזה 100%).
        // אם בגלל השילוב של קדימה + סיבוב החישוב יצא למשל 1.5, המנוע פשוט יסתובב ב-1.0 אבל כל הנסיעה תתעקם.
        // השורות האלה בודקות מי המנוע שיצא לו הכי הרבה כוח, ואם זה מעל 1.0, הן מקטינות את העוצמה של *כל המנועים יחד* באותו יחס, כדי שהרובוט ייסע בדיוק לאן שרצינו.
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        // נותנים את הגז בפועל ל-4 המנועים.
        frontLeftDrive.setPower(maxSpeed * (frontLeftPower / maxPower));
        frontRightDrive.setPower(maxSpeed * (frontRightPower / maxPower));
        backLeftDrive.setPower(maxSpeed * (backLeftPower / maxPower));
        backRightDrive.setPower(maxSpeed * (backRightPower / maxPower));
    }
}