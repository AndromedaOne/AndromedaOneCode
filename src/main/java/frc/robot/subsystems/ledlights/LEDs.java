package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class LEDs extends SubsystemBase {

  protected DigitalOutput red;
  protected DigitalOutput green;
  protected DigitalOutput blue;
  protected final double kBlinkSpeedMultiplier = 1.5;
  protected int blinkCounter = 0;
  private double redValue = 0;
  private double greenValue = 0;
  private double blueValue = 0;
  private boolean ledsOn = false;

  /**
   * This turns the LEDs off and clears the saved color
   */
  public void clearColor() {
    ledsOn = false;
    red.updateDutyCycle(0);
    green.updateDutyCycle(0);
    blue.updateDutyCycle(0);
    redValue = 0;
    blueValue = 0;
    greenValue = 0;
  }

  // This difference between toggle and clear color is that toggling
  // the LEDs will save the last color value so you can toggle them back
  // on with the same color
  protected void toggleLEDs() {
    if (ledsOn) {
      red.updateDutyCycle(0);
      green.updateDutyCycle(0);
      blue.updateDutyCycle(0);
      ledsOn = false;
    } else {
      red.updateDutyCycle(redValue);
      green.updateDutyCycle(greenValue);
      blue.updateDutyCycle(blueValue);
      ledsOn = true;
    }
  }

  protected void toggleLEDsOn() {
    if (!ledsOn) {
      toggleLEDs();
    }
  }

  protected double validateBrightness(double brightness) {
    ledsOn = true;
    if (brightness > 1.0) {
      brightness = 1;
    } else if (brightness < 0) {
      brightness = 0;
    }
    return brightness;
  }

  /**
   * This method takes a brightness value from 0 - 1 for each color
   */
  public void setRGB(double red, double green, double blue) {
    clearColor();
    this.red.updateDutyCycle(validateBrightness(red));
    this.green.updateDutyCycle(validateBrightness(green));
    this.blue.updateDutyCycle(validateBrightness(blue));
    redValue = red;
    greenValue = green;
    blueValue = blue;
  }

  /**
   * This method takes a brightness value from 0 - 1 for red
   */
  public void setRed(double brightness) {
    clearColor();
    redValue = brightness;
    red.updateDutyCycle(validateBrightness(brightness));
  }

  /**
   * This method takes a brightness value from 0 - 1 for green
   */
  public void setGreen(double brightness) {
    clearColor();
    greenValue = brightness;
    green.updateDutyCycle(validateBrightness(brightness));
  }

  /**
   * This method takes a brightness value from 0 - 1 for blue
   */
  public void setBlue(double brightness) {
    clearColor();
    blueValue = brightness;
    blue.updateDutyCycle(validateBrightness(brightness));
  }

  /**
   * This method takes a brightness value from 0 - 1 for yellow
   */
  public void setWhite(double brightness) {
    clearColor();
    red.updateDutyCycle(validateBrightness(brightness));
    green.updateDutyCycle(validateBrightness(brightness));
    blue.updateDutyCycle(validateBrightness(brightness));
    redValue = brightness;
    greenValue = brightness;
    blueValue = brightness;
  }

  /**
   * This method takes a brightness value from 0 - 1 for blue
   */
  public void setPurple(double brightness) {
    clearColor();
    System.out.println("Setting purple to: " + validateBrightness(brightness));
    blue.updateDutyCycle(validateBrightness(brightness));
    red.updateDutyCycle(validateBrightness(brightness));
    redValue = brightness;
    blueValue = brightness;
  }

}
