/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.limeLightCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;

public class ToggleLimelightLED extends Command {
  private boolean m_ledStatus;
  private LimeLightCameraBase limelight;

  /**
   * Creates a new ToggleLimelightLED.
   */
  public ToggleLimelightLED(boolean ledStatus, SensorsContainer sensorsContainer) {
    // Use addRequirements() here to declare subsystem dependencies.
    limelight = sensorsContainer.getLimeLight();
    m_ledStatus = ledStatus;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_ledStatus) {
      limelight.enableLED();
    } else {
      limelight.disableLED();
    }
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
