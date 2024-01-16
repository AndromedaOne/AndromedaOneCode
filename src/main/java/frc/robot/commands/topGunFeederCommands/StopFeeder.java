// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.topGunFeederCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.topGunFeeder.FeederBase;

public class StopFeeder extends Command {
  private FeederBase m_feeder;

  /** Creates a new StopFeeder. */
  public StopFeeder(FeederBase feeder) {
    m_feeder = feeder;
    addRequirements(m_feeder.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_feeder.stopFeeder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

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
