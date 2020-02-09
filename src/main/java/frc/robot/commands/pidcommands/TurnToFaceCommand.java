package frc.robot.commands.pidcommands;

import java.util.function.DoubleSupplier;

import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;

/**
 * Limelight Turn To Face Command.
 */
public class TurnToFaceCommand extends PIDCommand4905 {
  DoubleSupplier sensor;
  boolean lastSetpoint = false;

  public TurnToFaceCommand(DoubleSupplier sensor) {
    super(new PIDController4905("Turn To Face PID", 3e-2, 8e-3, 5e-4, 0.2), sensor, 0.0, 
      (lambda) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().moveUsingGyro(0.0, -lambda, true, true));
    this.getController().setTolerance(0.1);
    this.sensor = sensor;
  }


  @Override
  public boolean isFinished() {
    double angle = sensor.getAsDouble();
    System.out.println(angle +" "+this.getController().atSetpoint());
    boolean returnValue = this.getController().atSetpoint() && lastSetpoint;
    lastSetpoint = this.getController().atSetpoint();
    return returnValue;
  }
}