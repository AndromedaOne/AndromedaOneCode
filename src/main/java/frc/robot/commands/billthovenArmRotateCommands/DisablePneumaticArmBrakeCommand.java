package frc.robot.commands.billthovenArmRotateCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;

public class DisablePneumaticArmBrakeCommand extends Command {
  /** Creates a new ToggleArmBrake. */
  protected ArmTestBenchRotateBase m_armRotateBase;

  public DisablePneumaticArmBrakeCommand(ArmTestBenchRotateBase armRotateBase) {
    m_armRotateBase = armRotateBase;
    addRequirements(m_armRotateBase.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_armRotateBase.disengageArmBrake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
