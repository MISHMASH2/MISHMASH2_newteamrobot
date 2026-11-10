package org.firstinspires.ftc.teamcode.SubSystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class IntakeSubsystem {
    DcMotor intake;

    DcMotor transfer;

    public void init(@NonNull HardwareMap hardwareMap) {
        // הגדרת חומרה: השמות intake ו־transfer חייבים להתאים בדיוק לקונפיגורציה.
        // כיול כיוונים: ודאו שעוצמה חיובית מושכת פנימה ומעבירה פנימה;
        // התאימו setDirection לכל מנוע לפי ההרכבה אם צריך.
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");

        stop();
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void basicFeed() {
        // כיול הזנה: 1 הוא 100% עוצמה ליניקה, ו־0.2 הוא 20% עוצמה להעברה.
        // התאימו כל ערך בנפרד כך שהפריט מתקדם פנימה בלי להיתקע; טווח עוצמה חיובית: 0 עד 1.
        intake.setPower(1);
        transfer.setPower(0.2);

    }

    public void outPut() {
        // כיול הוצאה: הערכים השליליים הופכים את כיוון הפעולה; הטווח כאן הוא ‎-1 עד 0.
        // אפשר לכייל את עוצמת ההוצאה בנפרד מעוצמת ההזנה ב־basicFeed.
        intake.setPower(-1);
        transfer.setPower(-0.2);

    }

    public void stop() {
        intake.setPower(0);
        transfer.setPower(0);
    }
}

