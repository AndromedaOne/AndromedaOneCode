package frc.robot.subsystems.ledlights;

public class MockLEDs extends LEDs {

  @Override
  public void clearColor() {
  }

  @Override
  protected void toggleLEDs() {
  }

  @Override
  protected void toggleLEDsOn() {
  }

  @Override
  protected double validateBrightness(double brightness) {
    return brightness;
  }

  @Override
  public void setRGB(double red, double green, double blue) {
  }

  /**
   * This method takes a brightness value from 0 - 1 for red
   */
  @Override
  public void setRed(double brightness) {
  }

  /**
   * This method takes a brightness value from 0 - 1 for green
   */
  @Override
  public void setGreen(double brightness) {
  }

  /**
   * This method takes a brightness value from 0 - 1 for blue
   */

  @Override
  public void setBlue(double brightness) {
  }

  /**
   * This method takes a brightness value from 0 - 1 for yellow
   */
  @Override
  public void setWhite(double brightness) {
  }

  /**
   * This method takes a brightness value from 0 - 1 for blue
   */
  @Override
  public void setPurple(double brightness) {
  }

}
