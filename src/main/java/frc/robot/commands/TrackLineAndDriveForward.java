package frc.robot.commands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDController4905SampleStop;

public class TrackLineAndDriveForward extends TrackLineAndDrive {

  public TrackLineAndDriveForward(double speed, String PIDConfigName) {
    super(new PIDController4905SampleStop("FrontColorSensorPID", 0, 0, 0, 0),
        Robot.getInstance().getSensorsContainer().getFrontColorSensor(),
        (output) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(DEFAULT_MOVING_SPEED, -output, false),
        () -> Config4905.getConfig4905().getCommandConstantsConfig().getDouble(PIDConfigName + ".setpoint"));

    Config commandConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();

    double pValue = commandConstantsConfig.getDouble(PIDConfigName + ".p");
    double iValue = commandConstantsConfig.getDouble(PIDConfigName + ".i");
    double dValue = commandConstantsConfig.getDouble(PIDConfigName + ".d");

    getController().setP(pValue);
    getController().setI(iValue);
    getController().setD(dValue);
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
  }

  public TrackLineAndDriveForward() {
    this(DEFAULT_MOVING_SPEED, "ColorSensorFront");
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
