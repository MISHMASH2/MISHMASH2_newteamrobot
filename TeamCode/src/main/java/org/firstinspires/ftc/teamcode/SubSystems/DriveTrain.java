package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class DriveTrain {
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor BL;
    public DcMotor BR;

    public void init(HardwareMap hardwaremap) {
        // הגדרת חומרה: השמות FL, FR, BL, BR חייבים להתאים בדיוק לקונפיגורציה ולמיקום הגלגלים.
        FL = hardwaremap.get(DcMotor.class, "FL");
        FR = hardwaremap.get(DcMotor.class, "FR");
        BL = hardwaremap.get(DcMotor.class, "BL");
        BR = hardwaremap.get(DcMotor.class, "BR");

        // כיול כיוונים: בדקו תחילה כשהגלגלים באוויר שפקודת קדימה מסובבת את כולם לנסיעה קדימה.
        // התאימו FORWARD / REVERSE לכל מנוע לפי ההרכבה הפיזית.
        FR.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);
        FL.setDirection(DcMotor.Direction.FORWARD);
        BL.setDirection(DcMotor.Direction.FORWARD);

        stop();
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void drive(double x, double y,double rx){

        double FLPower=y+x+rx;
        double FRPower=y-x-rx;
        double BLPower=y-x+rx;
        double BRPower=y+x-rx;

        double big = Math.max(1.0, Math.max(Math.max(Math.abs(FLPower), Math.abs(BLPower)),Math.max(Math.abs(BRPower), Math.abs(FRPower))));
        FL.setPower(FLPower/big);
        FR.setPower(FRPower/big);
        BR.setPower(BRPower/big);
        BL.setPower(BLPower/big);
    }
    public void drive(Gamepad gamepad) {
        // כיול ג׳ויסטיקים: 0.1 הוא סף התעלמות מתזוזות קטנות סביב המרכז (טווח הציר: ‎-1 עד 1).
        // אם יש תנועה בלי נגיעה בסטיק הגדילו מעט את הסף; אם תחילת התנועה אינה רגישה מספיק הקטינו.
        // הסטיק השמאלי שולט בקדימה, אחורה וסיבוב. הסטיק הימני שולט בנסיעה הצידה.
        double forward = Math.abs(gamepad.left_stick_y) > 0.1 ? -gamepad.left_stick_y : 0;
        double turn = Math.abs(gamepad.left_stick_x) > 0.1 ? gamepad.left_stick_x : 0;
        double strafe = Math.abs(gamepad.right_stick_x) > 0.1 ? gamepad.right_stick_x : 0;

        // החזקת R2 מפעילה נהיגה איטית; שחרור מחזיר לנהיגה רגילה.
        // כיול הדק: 0.1 הוא סף הלחיצה להפעלת המצב, מתוך טווח 0 עד 1.
        if (gamepad.right_trigger > 0.1) {
            sdrive(strafe, forward, turn);
        } else {
            drive(strafe, forward, turn);
        }
    }

    public void stop() {
        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
    }
    public void sdrive(double x, double y,double rx){
        double FLPower=y+x+rx;
        double FRPower=y-x-rx;
        double BLPower=y-x+rx;
        double BRPower=y+x-rx;
        double big = Math.max(1.0, Math.max(Math.max(Math.abs(FLPower), Math.abs(BLPower)),Math.max(Math.abs(BRPower), Math.abs(FRPower))));
        // בנהיגה איטית כל מנוע מקבל שליש מהעוצמה המנורמלת.
        // שנו את המחלק בכל ארבע השורות יחד; מחלק גדול יותר נותן נסיעה איטית יותר.
        // מופעלת בהחזקת R2 דרך drive(Gamepad), שאליה קורא MainTeleOp.
        FL.setPower(FLPower/big/3);
        FR.setPower(FRPower/big/3);
        BR.setPower(BRPower/big/3);
        BL.setPower(BLPower/big/3);
    }






}