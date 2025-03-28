// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpoints;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpointsSupplier;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;
  private CoralIntakeEjectBase m_intakeEject;
  private boolean m_hasSetLevel = false;
    m_intakeEject = Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class EndEffectorControlCommand extends SequentialCommandGroup4905 {
  /** Creates a new EndEffectorControlCommand. */
  public EndEffectorControlCommand(ArmSetpointsSupplier level, boolean doesEnd) {
    addCommands(new InitializeEERotate(), new EndEffectorControlCommandInternal(level, doesEnd));
    m_hasSetLevel = false;
    if (!m_hasSetLevel) {
    if (m_useSmartDashboard) {
      m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector goal degrees", 0));
      if (m_useSmartDashboard) {
        m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector goal degrees", 0));
        m_hasSetLevel = true;
      } else if (m_endEffector.getLastSavedLevel() != ArmSetpoints.CORAL_LOAD
          || !m_intakeEject.intakeDetector()) {
        m_endEffector.setAngleDeg(m_level.getAsArmSetpoints());
        m_hasSetLevel = true;
      }

  }

  public EndEffectorControlCommand(boolean useSmartDashboard, boolean doesEnd) {
    addCommands(new InitializeEERotate(),
        new EndEffectorControlCommandInternal(useSmartDashboard, doesEnd));
    return m_doesEnd && m_endEffector.atSetPoint() && m_hasSetLevel;
  }
}
