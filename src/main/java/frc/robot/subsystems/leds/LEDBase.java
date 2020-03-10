package frc.robot.subsystems.leds;

public abstract class LEDBase {
    public abstract void setRed(double brightness);

    public abstract void setGreen(double brightness);

    public abstract void setBlue(double brightness);

    public abstract void clearLEDs();

    public abstract void toggleLEDs(boolean ledStatus);

    public abstract void setRGB(double red, double green, double blue);
}