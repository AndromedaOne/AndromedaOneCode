package frc.robot.commands.billthovenEndEffectorPositionCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorState;

public class SwitchEndEffectorPosition extends Command {
  /** Creates a new beautiful OpenCloseGripper. */
  protected BillEndEffectorPositionBase m_endEffectorPositionBase;

  public SwitchEndEffectorPosition(BillEndEffectorPositionBase endEffectorPositionBase) {
    // Use addRequirements() here to declare subsystem dependencies
    m_endEffectorPositionBase = endEffectorPositionBase;
    addRequirements(m_endEffectorPositionBase.getSubsystemBase());

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // below was in execute
    if (m_endEffectorPositionBase.getState() == BillEndEffectorState.HIGHSHOOTING) {
      m_endEffectorPositionBase.moveLowEndEffector();

    } else if (m_endEffectorPositionBase.getState() == BillEndEffectorState.LOWSHOOTING) {
      m_endEffectorPositionBase.moveHighEndEffector();

    }
    // below was in end
    // if (m_state == GripperState.OPENGRIPPER.ordinal()) {
    // m_state = GripperState.CLOSEGRIPPER.ordinal();
    // } else if (m_state == GripperState.CLOSEGRIPPER.ordinal()) {
    // m_state = GripperState.OPENGRIPPER.ordinal();
    // }
    SmartDashboard.putString("New End Effector Position state =",
        m_endEffectorPositionBase.getState().name());
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
