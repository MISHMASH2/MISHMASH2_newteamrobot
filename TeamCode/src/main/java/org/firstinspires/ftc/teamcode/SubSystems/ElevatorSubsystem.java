package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ElevatorSubsystem {

    // כיול עוצמה: ערך חיובי בין 0 ל־1, המשמש גם לעלייה וגם לירידה.
    // התאימו בהדרגה לפי העומס והמהירות הרצויה; הירידה משתמשת בסימן שלילי.
    private static final double MOVE_POWER = 0.3;
    private DcMotor motor1;
    private DcMotor motor2;

    public void init(HardwareMap hardwareMap) {
        // הגדרת חומרה: שני השמות חייבים להתאים בדיוק לשמות בקונפיגורציה.
        motor1 = hardwareMap.get(DcMotor.class, "motor1_elv");
        motor2 = hardwareMap.get(DcMotor.class, "motor2_elv");

        stop();

        // כיול כיוונים: ודאו שעוצמה חיובית בשני המנועים מעלה את המעלית יחד.
        // אם ההרכבה מחייבת זאת, הגדירו setDirection למנוע המתאים לפני בדיקת התנועה.
        // כרגע השליטה ידנית, ללא יעד גובה או עצירה אוטומטית בקצות המהלך.
        // לכיול יעדי אנקודר בעתיד: אפסו רק בנקודת ייחוס תחתונה ידועה,
        // ואז מדדו getCurrentPosition לכל מנוע בגובה הרצוי, לפני הקצה המכני.
        // RUN_USING_ENCODER לבדו אינו מסנכרן בין שני המנועים ואינו מגביל את הגובה.
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void up() {
        motor1.setPower(MOVE_POWER);
        motor2.setPower(MOVE_POWER);
    }

    public void Down() {
        motor1.setPower(-MOVE_POWER);
        motor2.setPower(-MOVE_POWER);
    }

    public void stop() {
        motor1.setPower(0);
        motor2.setPower(0);
    }
}
