package org.firstinspires.ftc.teamcode.SubSystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoSubsystem {
    private Servo servo;

    // כיול סרוו: אלה מיקומים מנורמלים בין 0 ל־1, ולא זוויות במעלות.
    // התאימו כל יעד בצעדים קטנים לפי המנגנון, לפני שהוא נלחץ אל הקצה המכני.
    public static final double OPEN_POSITION = 0.8;   // כיול: המיקום שבו המנגנון פתוח מספיק.
    public static final double CLOSED_POSITION = 0.2; // כיול: המיקום שבו המנגנון סגור כנדרש.

    public void init(HardwareMap hardwareMap) {
        // הגדרת חומרה: השם servo חייב להתאים בדיוק לשם הסרוו בקונפיגורציה.
        servo = hardwareMap.get(Servo.class, "servo");
    }

    public void open() {
        setPosition(OPEN_POSITION);
    }

    public void close() {
        setPosition(CLOSED_POSITION);
    }

    public void setPosition(double position) {
        // הגבלת 0 עד 1 שומרת על טווח הפקודה בלבד; הגבולות המכניים נקבעים בכיול היעדים למעלה.
        position = Math.max(0.0, Math.min(1.0, position));
        servo.setPosition(position);
    }

    // מחזיר את המיקום המבוקש האחרון, ולא מדידה מחיישן.
    public double getPosition() {
        return servo.getPosition();
    }
}
