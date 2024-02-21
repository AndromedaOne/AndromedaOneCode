package frc.robot.commands.billthovenEndEffectorPositionCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;

public class MoveToLowPosition extends Command {
  BillEndEffectorPositionBase m_endEffectorPositionBase;

  /** Creates a new OpenGripper. */
  public MoveToLowPosition(BillEndEffectorPositionBase endEffectorPositionBase) {
    addRequirements(endEffectorPositionBase.getSubsystemBase());
    m_endEffectorPositionBase = endEffectorPositionBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_endEffectorPositionBase.moveLowEndEffector();
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
