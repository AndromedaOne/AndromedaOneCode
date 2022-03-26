package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class LEDs extends SubsystemBase {

  protected DigitalOutput m_red;
  protected DigitalOutput m_green;
  protected DigitalOutput m_blue;
  protected final int KBLINKPERIOD = 100;
  protected int m_blinkCounter = 0;
  private double m_redValue = 0;
  private double m_greenValue = 0;
  private double m_blueValue = 0;
  private boolean m_ledsOn = false;
  private int m_rainbowCounter = 0;
  protected boolean m_readyForPeriodic = false;

  enum Mode {
    SOLID, BLINKING, RAINBOW,
  };

  Mode m_mode = Mode.BLINKING;

  public LEDs() {
  }

  public void setSolid() {
    m_mode = Mode.SOLID;
  }

  public void setBlinking() {
    m_mode = Mode.BLINKING;
  }

  public void setRainbow() {
    m_mode = Mode.RAINBOW;
  }

  @Override
  public void periodic() {
    if (!m_readyForPeriodic) {
      return;
    }
    Color color;
    switch (m_mode) {

    case SOLID:
      color = new Color(m_redValue, m_greenValue, m_blueValue);
      break;

    case BLINKING:
      if (m_blinkCounter < KBLINKPERIOD / 2) {
        color = new Color(0, 0, 0);
      } else {
        color = new Color(m_redValue, m_greenValue, m_blueValue);
      }
      m_blinkCounter = (m_blinkCounter + 1) % KBLINKPERIOD;
      break;

    case RAINBOW:
      m_rainbowCounter = (m_rainbowCounter + 1) % 100;
      color = rainbow(m_rainbowCounter, 100);
      break;

    default:
      color = new Color(1.0, .8, .8); // Pink
    }
    m_red.updateDutyCycle(validateBrightness(color.red));
    m_green.updateDutyCycle(validateBrightness(color.green));
    m_blue.updateDutyCycle(validateBrightness(color.blue));
  }

  /**
   * This turns the LEDs off and clears the saved color
   */
  public void clearColor() {
    m_ledsOn = false;
    m_redValue = 0;
    m_blueValue = 0;
    m_greenValue = 0;
  }

  // This difference between toggle and clear color is that toggling
  // the LEDs will save the last color value so you can toggle them back
  // on with the same color
  protected void toggleLEDs() {
    if (m_ledsOn) {
      m_red.updateDutyCycle(0);
      m_green.updateDutyCycle(0);
      m_blue.updateDutyCycle(0);
      m_ledsOn = false;
    } else {
      m_red.updateDutyCycle(m_redValue);
      m_green.updateDutyCycle(m_greenValue);
      m_blue.updateDutyCycle(m_blueValue);
      m_ledsOn = true;
    }
  }

  protected void toggleLEDsOn() {
    if (!m_ledsOn) {
      toggleLEDs();
    }
  }

  protected double validateBrightness(double brightness) {
    m_ledsOn = true;
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
    m_redValue = red;
    m_greenValue = green;
    m_blueValue = blue;
  }

  /**
   * This method takes a brightness value from 0 - 1 for red
   */
  public void setRed(double brightness) {
    clearColor();
    m_redValue = brightness;
  }

  /**
   * This method takes a brightness value from 0 - 1 for yellow
   */
  public void setYellow(double brightness) {
    clearColor();
    m_redValue = brightness;
    m_greenValue = brightness / 1.5;
  }

  /**
   * This method takes a brightness value from 0 - 1 for green
   */
  public void setGreen(double brightness) {
    clearColor();
    m_greenValue = brightness;
  }

  /**
   * This method takes a brightness value from 0 - 1 for blue
   */
  public void setBlue(double brightness) {
    clearColor();
    m_blueValue = brightness;
  }

  /**
   * This method takes a brightness value from 0 - 1 for yellow
   */
  public void setWhite(double brightness) {
    clearColor();
    m_redValue = brightness;
    m_greenValue = brightness;
    m_blueValue = brightness;
  }

  /**
   * This method takes a brightness value from 0 - 1 for blue
   */
  public void setPurple(double brightness) {
    clearColor();
    m_redValue = brightness;
    m_blueValue = brightness;
  }

//#get the i'th color, of n colors. 
  public static Color rainbow(int i, int n) {
    int r = 0;
    int g = 0;
    int b = 0;
    int stepsize = (int) Math.floor((255 * 6) / n);
    int progress = i * stepsize;
    switch ((int) Math.floor(progress / 255)) {
    case 0:
      b = 255;
      r = progress % 255;
      break;
    case 1:
      r = 255;
      b = progress % 255;
      break;
    case 2:
      r = 255;
      g = progress % 255;
      break;
    case 3:
      g = 255;
      r = progress % 255;
      break;
    case 4:
      g = 255;
      b = progress % 255;
      break;
    case 5:
      b = 255;
      g = progress % 255;
      break;
    }
    return new Color(r / 256.0, g / 256.0, b / 256.0);
  }
}
