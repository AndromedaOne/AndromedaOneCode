package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.ArmSetpoints;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

public class sbsdCoralLoadArmEndEffectorPositon extends ParallelCommandGroup4905 {
  private SBSDArmBase m_sbsdArmBase;
  private CoralEndEffectorRotateBase m_endEffector;

  public sbsdCoralLoadArmEndEffectorPositon() {
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    m_endEffector = Robot.getInstance().getSubsystemsContainer().getSBSDCoralEndEffectorBase();
    addCommands(new ArmControlCommand(() -> ArmSetpoints.CORAL_LOAD),
        new EndEffectorControlCommand(() -> ArmSetpoints.CORAL_LOAD));
  }

  @Override
  public void additionalInitialize() {
    CommandScheduler.getInstance().removeDefaultCommand(m_endEffector.getSubsystemBase());
    CommandScheduler.getInstance().removeDefaultCommand(m_sbsdArmBase.getSubsystemBase());
    CommandScheduler.getInstance().setDefaultCommand(m_endEffector.getSubsystemBase(),
        new EndEffectorControlCommand(() -> ArmSetpoints.CORAL_LOAD));
    CommandScheduler.getInstance().setDefaultCommand(m_sbsdArmBase.getSubsystemBase(),
        new ArmControlCommand(() -> ArmSetpoints.CORAL_LOAD));

  }

}
