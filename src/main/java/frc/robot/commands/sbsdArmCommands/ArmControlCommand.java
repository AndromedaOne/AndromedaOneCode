// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.ArmSetpoints.ArmSetpointsSupplier;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;

/** Add your docs here. */
public class ArmControlCommand extends Command {
  private SBSDArmBase m_sbsdArmBase;
  private boolean m_useSmartDashboard = false;
  private double m_setpoint = 0;
  private boolean m_useLevel = false;
  private ArmSetpointsSupplier m_level = () -> ArmSetpoints.CORAL_LOAD;

  public ArmControlCommand(boolean useSmartDashboard) {
    m_useSmartDashboard = useSmartDashboard;
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    addRequirements(m_sbsdArmBase.getSubsystemBase());
    if (m_useSmartDashboard) {
      SmartDashboard.putNumber("SBSD Arm goal degrees", 0);
    }
  }

  public ArmControlCommand(double setpoint) {
    this(false);
    m_setpoint = setpoint;
  }

  public ArmControlCommand(ArmSetpointsSupplier level) {
    this(false);
    m_level = level;
    m_useLevel = true;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_useLevel) {
      m_setpoint = m_level.getAsArmSetpoints().getArmAngleInDeg();
    }
    if (m_useSmartDashboard) {
      m_sbsdArmBase.setGoalDeg(SmartDashboard.getNumber("SBSD Arm goal degrees", 0));
    } else {
      m_sbsdArmBase.setGoalDeg(m_setpoint);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_useSmartDashboard) {
      m_sbsdArmBase.setGoalDeg(SmartDashboard.getNumber("SBSD Arm goal degrees", 0));
    }
    m_sbsdArmBase.calculateSpeed();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_sbsdArmBase.limitSwitchActive();
  }
}
