package frc.robot.commands.pidcommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

/**
 * Limelight Turn To Face Command.
 */
public class TurnToFaceCommand extends PIDCommand {
  DoubleSupplier sensor;
  boolean lastSetpoint = false;

  public TurnToFaceCommand(DoubleSupplier sensor) {
    super(new PIDController(3e-2, 8e-3, 5e-4), sensor, 0.0, TurnToFaceCommand::stub);
    this.getController().setTolerance(3.0);
    this.sensor = sensor;
  }

  public static void stub(double d) {
    System.out.println(d);
  }

  @Override
  public boolean isFinished() {
    double angle = sensor.getAsDouble();// NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
    System.out.println(angle);
    boolean returnValue = this.getController().atSetpoint() && lastSetpoint;
    lastSetpoint = this.getController().atSetpoint();
    return returnValue;
  }
}