// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpoints;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.subsystems.sbsdclimber.ClimberMode;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class MoveArmToClimberMode extends Command {
  private SBSDArmBase m_sbsdArmBase;
  private boolean m_isAtSetpoint = false;

  public MoveArmToClimberMode() {
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    addRequirements(m_sbsdArmBase.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_sbsdArmBase.setGoalDeg(SBSDArmSetpoints.ArmSetpoints.CLIMBER_POSITION);
    CommandScheduler.getInstance().removeDefaultCommand(m_sbsdArmBase.getSubsystemBase());
    CommandScheduler.getInstance().setDefaultCommand(m_sbsdArmBase.getSubsystemBase(),
        new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CLIMBER_POSITION, false));
    Trace.getInstance().logCommandInfo(this, "Arm Angle :"
        + SBSDArmSetpoints.getInstance().getArmAngleInDeg(ArmSetpoints.CLIMBER_POSITION));

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_sbsdArmBase.atSetPoint()) {
      ClimberMode.getInstance().setArmInClimberMode();
      m_sbsdArmBase.stop();
      m_isAtSetpoint = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_sbsdArmBase.stop();
    ClimberMode.getInstance().setArmInClimberMode();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_isAtSetpoint);
  }
}
