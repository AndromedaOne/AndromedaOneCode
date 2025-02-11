// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorBase;

/** Add your docs here. */
public class EndEffectorControlCommand extends Command {
  private CoralEndEffectorBase m_endEffector;
  private boolean m_useSmartDashboard = false;
  private double m_setpoint = 0;

  public EndEffectorControlCommand(boolean useSmartDashboard) {
    m_useSmartDashboard = useSmartDashboard;
    m_endEffector = Robot.getInstance().getSubsystemsContainer().getSBSDCoralEndEffectorBase();
    addRequirements(m_endEffector.getSubsystemBase());
    if (m_useSmartDashboard) {
      SmartDashboard.putNumber("SBSD End Effector goal degrees", 0);
    }
  }

  public EndEffectorControlCommand(double setpoint) {
    this(false);
    m_setpoint = setpoint;
  }

  public EndEffectorControlCommand(ArmSetpoints setpoint) {
    this(setpoint.getEndEffectorAngleInDeg());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_useSmartDashboard) {
      m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector Angle degrees", 0));
    } else {
      m_endEffector.setAngleDeg(m_setpoint);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_useSmartDashboard) {
      m_endEffector.setAngleDeg(SmartDashboard.getNumber("SBSD End Effector Angle degrees", 0));
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
    return false;
  }
}
