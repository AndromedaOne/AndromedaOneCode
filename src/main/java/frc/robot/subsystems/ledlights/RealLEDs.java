package frc.robot.subsystems.ledlights;

import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public abstract class RealLEDs extends LEDs {

  private DriveTrainBase m_driveTrain;

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
    } else if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESON) {
      setYellow(1);
      setBlinking(0.05);
    } else if (Robot.getInstance().isDisabled()) {
      setRainbow();
    } else if (Robot.getInstance().isTeleop()) {
      if (m_driveTrain.getDriveTrainMode() == DriveTrainModeEnum.FAST) {
        setRed(1);
      } else if (m_driveTrain.getDriveTrainMode() == DriveTrainModeEnum.MID) {
        setGreen(1);
      } else {
        setBlue(1);
      }
      setSolid();

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
