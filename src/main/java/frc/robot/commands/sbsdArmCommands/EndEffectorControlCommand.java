// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.ArmSetpoints.ArmSetpointsSupplier;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

/** Add your docs here. */
public class EndEffectorControlCommand extends Command {
  private CoralEndEffectorRotateBase m_endEffector;
  private boolean m_useSmartDashboard = false;
  private double m_setpoint = 0;
  private boolean m_useLevel = false;
  private boolean m_doesEnd = false;
  private ArmSetpointsSupplier m_level = () -> ArmSetpoints.CORAL_LOAD;

  public EndEffectorControlCommand(boolean useSmartDashboard, boolean doesEnd) {
    m_useSmartDashboard = useSmartDashboard;
    m_endEffector = Robot.getInstance().getSubsystemsContainer().getSBSDCoralEndEffectorBase();
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
    if (m_useLevel) {
      m_setpoint = m_level.getAsArmSetpoints().getEndEffectorAngleInDeg();
    }
    if (m_useSmartDashboard) {
      m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector goal degrees", 0));
    } else {
      m_endEffector.setAngleDeg(m_setpoint);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_useSmartDashboard) {
      m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector goal degrees", 0));
    }
    m_endEffector.calculateSpeed();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_doesEnd && m_endEffector.atSetPoint();
  }
}
