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
        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.FORWARD);
        BR.setDirection(DcMotor.Direction.FORWARD);

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
        // אפשר לכייל כל ציר בנפרד בשלוש השורות הבאות.
        double y = Math.abs(gamepad.left_stick_y) > 0.1 ? -gamepad.left_stick_y : 0;
        double x = Math.abs(gamepad.left_stick_x) > 0.1 ? gamepad.left_stick_x : 0;
        double rx = Math.abs(gamepad.right_stick_x) > 0.1 ? gamepad.right_stick_x : 0;
        drive(x, y, rx);
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
        // כיול נהיגה איטית: חלוקה ב־10 מגבילה ל־10% מהעוצמה המנורמלת.
        // שנו את המחלק בכל ארבע השורות יחד; מחלק גדול יותר נותן נסיעה איטית יותר.
        // המתודה sdrive אינה מחוברת כרגע לכפתור ב־MainTeleOp.
        FL.setPower(FLPower/big/10);
        FR.setPower(FRPower/big/10);
        BR.setPower(BRPower/big/10);
        BL.setPower(BLPower/big/10);
    }






}