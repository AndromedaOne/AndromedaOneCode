// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpoints;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpointsSupplier;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class EndEffectorControlCommand extends Command {
  private CoralEndEffectorRotateBase m_endEffector;
  private CoralIntakeEjectBase m_intakeEject;
  private boolean m_useSmartDashboard = false;
  private double m_setpoint = 0;
  private boolean m_useLevel = false;
  private boolean m_doesEnd = false;
  private boolean m_hasSetLevel = false;
  private ArmSetpointsSupplier m_level = () -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD;

  public EndEffectorControlCommand(boolean useSmartDashboard, boolean doesEnd) {
    m_useSmartDashboard = useSmartDashboard;
    m_endEffector = Robot.getInstance().getSubsystemsContainer()
        .getSBSDCoralEndEffectorRotateBase();
    m_intakeEject = Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();
    addRequirements(m_endEffector.getSubsystemBase());
    if (m_useSmartDashboard) {
      SmartDashboard.putNumber("SBSD End Effector goal degrees", 0);
    }
    m_doesEnd = doesEnd;
  }

  public EndEffectorControlCommand(double setpoint, boolean doesEnd) {
    this(false, doesEnd);
    m_setpoint = setpoint;
  }

  public EndEffectorControlCommand(ArmSetpointsSupplier level, boolean doesEnd) {
    this(false, doesEnd);
    m_level = level;
    m_useLevel = true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_hasSetLevel = false;
    if (m_useLevel) {
      Trace.getInstance().logCommandInfo(this, "EE Level: "
          + SBSDArmSetpoints.getInstance().getEndEffectorAngleInDeg(m_level.getAsArmSetpoints()));
      m_setpoint = SBSDArmSetpoints.getInstance()
          .getEndEffectorAngleInDeg(m_level.getAsArmSetpoints());
      if (m_level.getAsArmSetpoints() == SBSDArmSetpoints.ArmSetpoints.LEVEL_4) {
        Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase().scoreL4();
        Trace.getInstance().logCommandInfo(this,
            "Score Level 4, going to Level: " + m_level.getAsArmSetpoints());
      } else {
        Trace.getInstance().logCommandInfo(this,
            "Exit Score, going to Level: " + m_level.getAsArmSetpoints());
        Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase()
            .exitL4ScoringPosition();
      }
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!m_hasSetLevel) {
      if (m_useSmartDashboard) {
        m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector goal degrees", 0));
        m_hasSetLevel = true;
      } else if (m_endEffector.getLastSavedLevel() != ArmSetpoints.CORAL_LOAD
          || !m_intakeEject.intakeDetector()
          || m_level.getAsArmSetpoints() == ArmSetpoints.CLIMBER_POSITION) {
        m_endEffector.setAngleDeg(m_level.getAsArmSetpoints());
        m_hasSetLevel = true;
      }
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_endEffector.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_doesEnd && m_endEffector.atSetPoint() && m_hasSetLevel;
  }
}
