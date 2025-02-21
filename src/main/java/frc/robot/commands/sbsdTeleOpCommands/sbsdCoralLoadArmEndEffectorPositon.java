package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

public class sbsdCoralLoadArmEndEffectorPositon extends ParallelCommandGroup4905 {
  private SBSDArmBase m_sbsdArmBase;
  private CoralEndEffectorRotateBase m_endEffector;
  private boolean m_isSBSD = false;

  public sbsdCoralLoadArmEndEffectorPositon() {
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    m_endEffector = Robot.getInstance().getSubsystemsContainer().getSBSDCoralEndEffectorBase();
    m_isSBSD = Config4905.getConfig4905().isSBSD();
    addCommands(new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, true),
        new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, true));
  }

  @Override
  public void additionalInitialize() {
    if (m_isSBSD) {
      CommandScheduler.getInstance().removeDefaultCommand(m_sbsdArmBase.getSubsystemBase());
      CommandScheduler.getInstance().removeDefaultCommand(m_endEffector.getSubsystemBase());
      CommandScheduler.getInstance().setDefaultCommand(m_sbsdArmBase.getSubsystemBase(),
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, false));
      CommandScheduler.getInstance().setDefaultCommand(m_endEffector.getSubsystemBase(),
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, false));
    }

  }

}
