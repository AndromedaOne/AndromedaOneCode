/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.telemetries.Trace;

public class FindTarget extends CommandBase {
  private SensorsContainer m_sensorsContainer;
  private LimeLightCameraBase limelight;
  private boolean targetFound;
  private int counter;
  /**
   * Creates a new FindTarget.
   */
  public FindTarget(SensorsContainer sensorsContainer) {
    limelight = sensorsContainer.getLimeLight();
    m_sensorsContainer = sensorsContainer;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    limelight.enableLED();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    counter++;
    targetFound = m_sensorsContainer.getLimeLight().targetLock();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Commands run @ 50hz, so counter should be 50 @ t=1 second
    if (counter > 50) {
      Trace.getInstance().logCommandInfo(this, "No target found for one second.");
      return true;
    }
    if (targetFound) {
      Trace.getInstance().logCommandInfo(this, "Target found");
      return true;
    }
    return false;
  }
}
