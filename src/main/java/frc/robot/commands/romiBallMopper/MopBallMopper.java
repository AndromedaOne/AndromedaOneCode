// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.romiBallMopper;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.romiShooter.StopRomiShooter;
import frc.robot.subsystems.romiBallMopper.RomiBallMopperBase;
import frc.robot.telemetries.Trace;

public class MopBallMopper extends CommandBase {

  private RomiBallMopperBase m_romiBallMopper;

  public MopBallMopper() {
    m_romiBallMopper = Robot.getInstance().getSubsystemsContainer().getRomiBallMopper();
    addRequirements(m_romiBallMopper);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_romiBallMopper.mop();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    m_romiBallMopper.setResetState(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
