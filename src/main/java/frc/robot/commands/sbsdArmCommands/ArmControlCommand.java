// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpointsSupplier;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class ArmControlCommand extends Command {
  private SBSDArmBase m_sbsdArmBase;
  private boolean m_useSmartDashboard = false;
  private boolean m_doesEnd = false;
  private ArmSetpointsSupplier m_level = () -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD;

  public ArmControlCommand(boolean useSmartDashboard, boolean doesEnd) {
    m_useSmartDashboard = useSmartDashboard;
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    addRequirements(m_sbsdArmBase.getSubsystemBase());
    if (m_useSmartDashboard) {
      SmartDashboard.putNumber("SBSD Arm goal degrees", 0);
    }
    m_doesEnd = doesEnd;
  }

  public ArmControlCommand(ArmSetpointsSupplier level, boolean doesEnd) {
    this(false, doesEnd);
    m_level = level;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_useSmartDashboard) {
      m_sbsdArmBase.setGoalDeg(SmartDashboard.getNumber("SBSD Arm goal degrees", 0));
    } else {
      m_sbsdArmBase.setGoalDeg(m_level.getAsArmSetpoints());
      Trace.getInstance().logCommandInfo(this, "Arm Set Goal Deg: "
          + SBSDArmSetpoints.getInstance().getArmAngleInDeg(m_level.getAsArmSetpoints()));
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_useSmartDashboard) {
      m_sbsdArmBase.setGoalDeg(SmartDashboard.getNumber("SBSD Arm goal degrees", 0));
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_sbsdArmBase.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_doesEnd && m_sbsdArmBase.atSetPoint());
  }
}
