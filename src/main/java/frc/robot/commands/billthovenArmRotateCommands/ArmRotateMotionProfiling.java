package frc.robot.commands.billthovenArmRotateCommands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;

public class ArmRotateMotionProfiling extends ProfiledPIDCommand {
  public int m_goal = 0;

  public ArmRotateMotionProfiling() {
    super(new ProfiledPIDController(0, 0, 0, 
    new TrapezoidProfile.Constraints(0, 0)), () -> 0,
        // This should return the goal (can also be a constant)
        () -> new TrapezoidProfile.State(),
        // This uses the output
        (output, setpoint) -> {
          // Use the output (and setpoint, if desired) here
        });

// Use addRequirements() here to declare subsystem dependencies.
// Configure additional PID options by calling `getController` here.
  }
  public void initialize(){

  }
  public void execute(){

  }
  @Override
  public boolean isFinished() {
    return false;
  }

}