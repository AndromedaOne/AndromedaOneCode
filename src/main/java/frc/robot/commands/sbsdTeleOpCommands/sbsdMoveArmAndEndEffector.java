// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpointsSupplier;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class sbsdMoveArmAndEndEffector extends ParallelCommandGroup4905 {
  /** Creates a new sbsdMoveArmAndEndEffector. */
  boolean m_moved;
  ArmControlCommand m_moveArm;
  EndEffectorControlCommand m_moveEndEffector;
  private SBSDArmBase m_sbsdArmBase;
  private CoralEndEffectorRotateBase m_endEffector;
  private ArmSetpointsSupplier m_level;
  private boolean m_isSBSD = false;

  public sbsdMoveArmAndEndEffector(ArmSetpointsSupplier level) {
    m_level = level;
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    m_endEffector = Robot.getInstance().getSubsystemsContainer()
        .getSBSDCoralEndEffectorRotateBase();
    m_moveArm = new ArmControlCommand(level, true);
    m_moveEndEffector = new EndEffectorControlCommand(level, true);
    addCommands(m_moveArm, m_moveEndEffector);
    m_isSBSD = Config4905.getConfig4905().isSBSD();
  }

  @Override
  public void additionalInitialize() {
    if (m_isSBSD) {
      CommandScheduler.getInstance().removeDefaultCommand(m_sbsdArmBase.getSubsystemBase());
      CommandScheduler.getInstance().removeDefaultCommand(m_endEffector.getSubsystemBase());
      CommandScheduler.getInstance().setDefaultCommand(m_sbsdArmBase.getSubsystemBase(),
          new ArmControlCommand(m_level, false));
      CommandScheduler.getInstance().setDefaultCommand(m_endEffector.getSubsystemBase(),
          new EndEffectorControlCommand(m_level, false));
    }

  }

  @Override
  public void additionalEnd(boolean moved) {
    moved = true;
    m_moved = moved;
    System.out.println("moved: " + moved);
  }
}