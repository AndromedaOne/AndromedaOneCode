package frc.robot.subsystems.ledlights;

import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public abstract class RealLEDs extends LEDs {

  DriveTrainBase m_driveTrain;
  private LEDRobotInformation m_ledRobotInfo = LEDRobotInformation.getInstance();

  public RealLEDs(DriveTrainBase driveTrain) {
    setPurple(1.0);
    setSolid();
    m_driveTrain = driveTrain;
  }

  @Override
  public void periodic() {
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
      switch (m_driveTrain.getDriveTrainMode()) {
      case SLOW:
        setBlue(1);
        
        setSolid();
        break;

      case MID:
        setGreen(1);
        setSolid();
        break;

      case FAST:
        setRed(1);
        setSolid();
        break;

      default:
        setOrange(1);
        setSolid();
      }
    } else if (Robot.getInstance().isAutonomous()) {
      setWhite(1);
      setSolid();
    } else {
      setRainbow();
    }
  }
}
