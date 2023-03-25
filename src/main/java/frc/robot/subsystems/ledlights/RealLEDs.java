package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.commands.samLEDCommands.ConeLEDs;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public class RealLEDs extends LEDs {
  private DigitalOutput m_red;
  private DigitalOutput m_green;
  private DigitalOutput m_blue;
  DriveTrain m_driveTrain;
  private LEDStates m_LedStates;
  private static ConeOrCubeLEDsSingleton m_ConeOrCube;

  public RealLEDs(Config conf, DriveTrain driveTrain) {
    m_red = new DigitalOutput(conf.getInt("Red"));
    m_red.enablePWM(0);
    m_green = new DigitalOutput(conf.getInt("Green"));
    m_green.enablePWM(0);
    m_blue = new DigitalOutput(conf.getInt("Blue"));
    m_blue.enablePWM(0);
    setPurple(1.0);
    setSolid();
    m_driveTrain = driveTrain;
    m_ConeOrCube = new ConeOrCubeLEDsSingleton().getInstance();
  }

  @Override
  public void periodic() {
    if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESON) {
      setSethRed(1);
      setBlinking(0.05);
    } else if (Robot.getInstance().isDisabled()) {
      setRainbow();
    } else if (Robot.getInstance().isTeleop()) {
      double matchTime = DriverStation.getMatchTime();
      if (m_ConeOrCube.getButtonHeld() == true) {
        if (m_ConeOrCube.getLEDStates() == LEDStates.CONE) {
          setYellow(1);
          setBlinking(0.5);
        } else if (m_ConeOrCube.getLEDStates() == LEDStates.CUBE) {
          setPurple(1);
          setBlinking(0.5);
        }
      } else if (matchTime <= 30 && matchTime > 0) {
        setYellow(1);
        setBlinking(1);
        ConeLEDs.m_ledState = 0;
      } else {
        switch (m_driveTrain.getDriveTrainMode()) {
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

        default:
          setOrange(1);
          setSolid();
          ConeLEDs.m_ledState = 0;
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
