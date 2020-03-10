package frc.robot.subsystems.leds;

public class MockLED extends LEDBase {

    @Override
    public void setRed(double brightness) {

    }

    @Override
    public void setGreen(double brightness) {

    }

    @Override
    public void setBlue(double brightness) {

    }

    @Override
    public void clearLEDs() {

    }

    @Override
    public void toggleLEDs(boolean ledStatus) {

    }

    @Override
    public void setRGB(double red, double green, double blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

}