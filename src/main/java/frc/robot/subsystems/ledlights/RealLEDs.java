package frc.robot.subsystems.ledlights;

import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
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
      if (!getTargetFound()) {
        setPink(1);
        setBlinking(0.2);
      } else {
        switch (m_driveTrain.getDriveTrainMode()) {
        case SLOW:
          setBlue(1);
          if (getNoteState()) {
            setBlinking(m_intakeBlinkRate);
          } else {
            setSolid();
          }
          break;

        case MID:
          setGreen(1);
          if (getNoteState()) {
            setBlinking(m_intakeBlinkRate);
          } else {
            setSolid();
          }
          break;

        case FAST:
          setRed(1);
          if (getNoteState()) {
            setBlinking(m_intakeBlinkRate);
          } else {
            setSolid();
          }
          break;

        default:
          setOrange(1);
          if (getNoteState()) {
            setBlinking(m_intakeBlinkRate);
          } else {
            setSolid();
          }
        }
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
