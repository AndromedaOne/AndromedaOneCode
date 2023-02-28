package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Config4905;
import frc.robot.Robot;

public class RealLEDs extends LEDs {
  public RealLEDs() {
    Config conf = Config4905.getConfig4905().getLEDConfig();

    m_red = new DigitalOutput(conf.getInt("Red"));
    m_red.enablePWM(0);
    m_green = new DigitalOutput(conf.getInt("Green"));
    m_green.enablePWM(0);
    m_blue = new DigitalOutput(conf.getInt("Blue"));
    m_blue.enablePWM(0);
    setPurple(1.0);
    setSolid();

    m_readyForPeriodic = true;
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
}
