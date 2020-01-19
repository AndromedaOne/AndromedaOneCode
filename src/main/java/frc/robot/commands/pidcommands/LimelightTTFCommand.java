package frc.robot.commands.pidcommands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;

/**
 * Limelight Turn To Face Command.
 */
public class LimelightTTFCommand extends PIDCommand {
    boolean lastSetpoint = false;
  public LimelightTTFCommand() {
    super(new PIDController(3e-2, 8e-3, 5e-4),
        () -> -NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0), 
        0.0,
        output -> Robot.getInstance().getDriveTrain().move(0.0, output), Robot.getInstance().getDriveTrain());
    this.getController().setTolerance(3.0);
  }
  @Override
  public boolean isFinished() {
    double angle = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
    System.out.println(angle);
    boolean returnValue = this.getController().atSetpoint() && lastSetpoint;
    lastSetpoint = this.getController().atSetpoint();
    return returnValue;
  }
}