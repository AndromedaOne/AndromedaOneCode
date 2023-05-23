package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.commands.samLEDCommands.ConeLEDs;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public abstract class RealLEDs extends LEDs {

  DriveTrain m_driveTrain;
  private LEDRobotInformation m_ledRobotInfo = LEDRobotInformation.getInstance();

  public RealLEDs(DriveTrain driveTrain) {
    setPurple(1.0);
    setSolid();
    m_driveTrain = driveTrain;
  }

  @Override
  public void periodic() {
    super.periodic();
    if (m_ledRobotInfo.getCannonIsPressurized()) {
      setRed(1);
      setBlinking(0.25);
    } else if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESON) {
      setSethRed(1);
      setBlinking(0.05);
    } else if (Robot.getInstance().isDisabled()) {
      setRainbow();
    } else if (Robot.getInstance().isTeleop()) {
      double matchTime = DriverStation.getMatchTime();
      if (m_ledRobotInfo.getButtonHeld() == true) {
        if (m_ledRobotInfo.getLEDStates() == LEDStates.CONE) {
          setYellow(1);
          setBlinking(0.2);
        } else if (m_ledRobotInfo.getLEDStates() == LEDStates.CUBE) {
          setRed(1);
          setBlinking(0.2);
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
  }
}
