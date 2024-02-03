package frc.robot.commands.billthovenEndEffectorPositionCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;

public class DefaultEndEffectorPositionCommand extends Command {
  /** Creates a new DefaultGripperCommand. */
  private BillEndEffectorPositionBase m_defaultEndEffectorPosition;

  public DefaultEndEffectorPositionCommand(BillEndEffectorPositionBase defaultEndEffectorPosition) {
    m_defaultEndEffectorPosition = defaultEndEffectorPosition;
    addRequirements(m_defaultEndEffectorPosition.getSubsystemBase());
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

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
