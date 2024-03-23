package frc.robot.commands.billthovenArmRotateCommands;


import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;

public class ArmRotateMotionProfiling extends ProfiledPIDCommand {

  public ArmRotateMotionProfiling() {
    super(
    new ProfiledPIDController(0, 0, 0, new TrapezoidProfile.Constraints(5,10)),
    () -> 0,
    // This should return the goal (can also be a constant)
    () -> new TrapezoidProfile.State(),
    // This uses the output
    (output, setpoint) -> {
      // Use the output (and setpoint, if desired) here
    });
// Use addRequirements() here to declare subsystem dependencies.
// Configure additional PID options by calling `getController` here.
}

@Override
  public boolean isFinished() {
    return false;
  }
}