// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.feederCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.feeder.FeederBase;

public class RunFeeder extends CommandBase {
  private FeederBase m_feeder;
  private double m_speed = 0;
  private boolean m_runInReverse;

  /** Creates a new RunFeeder. */
  public RunFeeder(FeederBase feeder, double speed, boolean runInReverse) {
    m_feeder = feeder;
    m_speed = speed;
    m_runInReverse = runInReverse;
    addRequirements(m_feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_runInReverse) {
      m_feeder.runFeeder(-m_speed);
    } else {
      m_feeder.runFeeder(m_speed);
    }
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
    return true;
  }
}
