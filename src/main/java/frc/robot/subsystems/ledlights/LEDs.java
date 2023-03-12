package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class LEDs extends SubsystemBase {

  // The 1 in m_blinkRate makes the LEDs blink for one second on and one second
  // off.
  private double m_blinkRate = 1;
  private int m_blinkCounter = 0;
  private double m_redValue = 0;
  private double m_greenValue = 0;
  private double m_blueValue = 0;
  private boolean m_ledsOn = false;
  private int m_rainbowCounter = 0;

  enum Mode {
    SOLID, BLINKING, RAINBOW,
  };

  private Mode m_mode = Mode.BLINKING;

  public enum TeleOpMode {
    SLOW, MID, FAST, BRAKED
  };

  private TeleOpMode m_teleOpMode = TeleOpMode.FAST;

  public LEDs() {
  }

  public void setSolid() {
    m_mode = Mode.SOLID;
  }

  public void setBlinking(double blinkRateSeconds) {
    m_mode = Mode.BLINKING;
    m_blinkRate = blinkRateSeconds;
  }

  public void setRainbow() {
    m_mode = Mode.RAINBOW;
  }

  @Override
  public void periodic() {
    Color color;
    switch (m_mode) {

    case SOLID:
      color = new Color(m_redValue, m_greenValue, m_blueValue);
      break;

    case BLINKING:
      if (m_blinkCounter < m_blinkRate * 50) {
        color = new Color(0, 0, 0);
      } else {
        color = new Color(m_redValue, m_greenValue, m_blueValue);
      }
      m_blinkCounter = (m_blinkCounter + 1) % (int) (m_blinkRate * 100);
      break;

    case RAINBOW:
      m_rainbowCounter = (m_rainbowCounter + 1) % 100;
      color = rainbow(m_rainbowCounter, 100);
      break;

    default:
      color = new Color(1.0, .8, .8); // Pink
    }
    updateRedDutyCycle(validateBrightness(color.red));
    updateGreenDutyCycle(validateBrightness(color.green));
    updateBlueDutyCycle(validateBrightness(color.blue));
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
      updateRedDutyCycle(0);
      updateGreenDutyCycle(0);
      updateBlueDutyCycle(0);
      m_ledsOn = false;
    } else {
      updateRedDutyCycle(m_redValue);
      updateGreenDutyCycle(m_greenValue);
      updateBlueDutyCycle(m_blueValue);
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
    m_greenValue = 0;
    m_blueValue = 0.2;
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
    m_greenValue = 0;
    m_blueValue = brightness;
  }

  public void setOrange(double brightness) {
    clearColor();
    m_redValue = brightness;
    m_greenValue = brightness * 0.65;
    m_blueValue = 0;
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

  public void setTeleopMode(TeleOpMode teleOpMode) {
    m_teleOpMode = teleOpMode;
  }

  protected TeleOpMode getTeleOpMode() {
    return m_teleOpMode;
  }

  protected abstract void updateRedDutyCycle(double brightness);

  protected abstract void updateBlueDutyCycle(double brightness);

  protected abstract void updateGreenDutyCycle(double brightness);
}
