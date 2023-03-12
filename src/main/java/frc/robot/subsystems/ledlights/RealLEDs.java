package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;

public class RealLEDs extends LEDs {
  private DigitalOutput m_red;
  private DigitalOutput m_green;
  private DigitalOutput m_blue;

  public RealLEDs(Config conf) {
    m_red = new DigitalOutput(conf.getInt("Red"));
    m_red.enablePWM(0);
    m_green = new DigitalOutput(conf.getInt("Green"));
    m_green.enablePWM(0);
    m_blue = new DigitalOutput(conf.getInt("Blue"));
    m_blue.enablePWM(0);
    setPurple(1.0);
    setSolid();
  }

  @Override
  public void periodic() {
    if (Robot.getInstance().isDisabled()) {
      setRainbow();
    } else if (Robot.getInstance().isTeleop()) {
      double matchTime = DriverStation.getMatchTime();
      if (matchTime <= 30 && matchTime > 0) {
        setYellow(1);
        setBlinking(1);
      } else {
        switch (getTeleOpMode()) {
        case SLOW:
          setBlue(1);
          setSolid();
          break;

        case MID:
          setPurple(1);
          setSolid();
          break;

        case FAST:
          setGreen(1);
          setSolid();
          break;

        case BRAKED:
          setRed(1);
          setBlinking(0.05);
          break;

        default:
          setOrange(1);
          setSolid();
        }
      }
    } else if (Robot.getInstance().isAutonomous()) {
      setWhite(1);
      setSolid();
    } else {
      setRainbow();
    }
    super.periodic();
  }

  @Override
  protected void updateRedDutyCycle(double brightness) {
    m_red.updateDutyCycle(brightness);
  }

  @Override
  protected void updateBlueDutyCycle(double brightness) {
    m_blue.updateDutyCycle(brightness);
  }

  @Override
  protected void updateGreenDutyCycle(double brightness) {
    m_green.updateDutyCycle(brightness);
  }
}
