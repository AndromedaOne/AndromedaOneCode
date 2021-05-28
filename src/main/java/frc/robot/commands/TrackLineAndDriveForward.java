package frc.robot.commands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDController4905SampleStop;

public class TrackLineAndDriveForward extends TrackLineAndDrive {
  public static final double MOVING_SPEED = 0.3;

  public TrackLineAndDriveForward() {
    super(new PIDController4905SampleStop("FrontColorSensorPID", 0, 0, 0, 0),
        Robot.getInstance().getSensorsContainer().getFrontColorSensor(),
        (output) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(MOVING_SPEED, -output, false), 0);

    Config commandConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();

    double pValue = commandConstantsConfig.getDouble("ColorSensorFront.p");
    double iValue = commandConstantsConfig.getDouble("ColorSensorFront.i");
    double dValue = commandConstantsConfig.getDouble("ColorSensorFront.d");

    double setpoint = commandConstantsConfig.getDouble("ColorSensorFront.setpoint");

    getController().setP(pValue);
    getController().setI(iValue);
    getController().setD(dValue);

    getController().setSetpoint(setpoint);

    SmartDashboard.putNumber("AAA DriveForward P", pValue);
    SmartDashboard.putNumber("AAA DriveForward I", iValue);
    SmartDashboard.putNumber("AAA DriveForward D", dValue);
    SmartDashboard.putNumber("AAA DriveForward Setpoint", setpoint);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Robot.getInstance().getSubsystemsContainer().getDrivetrain().stop();
  }
}
