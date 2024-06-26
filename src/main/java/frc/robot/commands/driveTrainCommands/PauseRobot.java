// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import java.time.*;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class PauseRobot extends Command {
  private long m_pauseTimeInMS = 0;
  private Instant m_startTime;
  private DriveTrainBase m_driveTrain;

  /** Creates a new PauseRobot. */
  public PauseRobot(long pauseTimeInMS, DriveTrainBase driveTrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveTrain.getSubsystemBase());
    m_pauseTimeInMS = pauseTimeInMS;
    m_driveTrain = driveTrain;
  }

  public PauseRobot(DriveTrainBase driveTrain) {
    this(Long.MAX_VALUE, driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_startTime = Instant.now();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrain.stop();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Duration.between(m_startTime, Instant.now()).toMillis() > m_pauseTimeInMS) {
      return true;
    }
    return false;
  }
}
