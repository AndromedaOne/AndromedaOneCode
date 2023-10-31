package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.subsystems.drivetrain.tankDriveTrain.TankDriveTrain;

public abstract class RealLEDs extends LEDs {

  TankDriveTrain m_driveTrain;
  private LEDRobotInformation m_ledRobotInfo = LEDRobotInformation.getInstance();

  public RealLEDs(TankDriveTrain driveTrain) {
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
