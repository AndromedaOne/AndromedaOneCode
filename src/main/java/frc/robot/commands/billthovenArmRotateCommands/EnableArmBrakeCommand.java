package frc.robot.commands.billthovenArmRotateCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;

public class EnableArmBrakeCommand extends Command {
  /** Creates a new ToggleArmBrake. */
  protected ArmTestBenchRotateBase m_armRotateBase;

  public EnableArmBrakeCommand(ArmTestBenchRotateBase armRotateBase) {
    m_armRotateBase = armRotateBase;
    addRequirements(m_armRotateBase.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_armRotateBase.engageArmBrake();
    m_armRotateBase.stop();

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armRotateBase.disengageArmBrake();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
