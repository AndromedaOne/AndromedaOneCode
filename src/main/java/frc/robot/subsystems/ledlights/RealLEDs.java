package frc.robot.subsystems.ledlights;

import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public abstract class RealLEDs extends LEDs {

  private DriveTrainBase m_driveTrain;
  private double m_intakeBlinkRate = 0.1;
  private LEDRobotInformation m_ledRobotInfo = LEDRobotInformation.getInstance();

  public RealLEDs(DriveTrainBase driveTrain) {
    setPurple(1.0);
    setSolid();
    m_driveTrain = driveTrain;
  }

  @Override
  public void periodic() {
    if (Robot.getInstance().isAutonomous()) {
      return;
    }
    super.periodic();
    if (!Robot.getInstance().getSensorsContainer().getGyro().getIsCalibrated()) {
      setWhite(1);
      setBlinking(0.25);
    } else if (m_ledRobotInfo.getCannonIsPressurized()) {
      setRed(1);
      setBlinking(0.25);
    } else if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESON) {
      setYellow(1);
      setBlinking(0.05);
    } else if (Robot.getInstance().isDisabled()) {
      setRainbow();
    } else if (Robot.getInstance().isTeleop()) {
      switch (m_driveTrain.getRegion()) {
      case NORTH:
      case SOUTH:
        setGreen(1);
        break;
      case NORTHEAST:
      case SOUTHWEST:
        setRed(1);
        break;
      case NORTHWEST:
      case SOUTHEAST:
        setBlue(1);
        break;
      default:
        setOrange(1);
        break;
      }
      if (m_driveTrain.getDriveTrainMode() == DriveTrainModeEnum.FAST) {
        setBlinking(0.1);
      } else {
        setBlinking(0.5);
      }

    } else if (Robot.getInstance().isAutonomous()) {
      if (!getTargetFound()) {
        setPink(1);
        setBlinking(0.2);
      } else {
        setWhite(1);
        setSolid();
      }
    } else {
      setRainbow();
    }
  }
}
