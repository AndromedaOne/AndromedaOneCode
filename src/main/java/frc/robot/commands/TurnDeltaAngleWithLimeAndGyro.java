/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.pidcommands.TurnDeltaAngle;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class TurnDeltaAngleWithLimeAndGyro extends CommandBase {
  /**
   * Creates a new TurnDeltaAngleWithLimeAndGyro.
   */
  LimeLightCameraBase m_limelight;

  public TurnDeltaAngleWithLimeAndGyro(DriveTrain drivetrain, LimeLightCameraBase limeLight) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
    m_limelight = limeLight;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this.getClass().getName());
    double horizontalRadiansToTarget = m_limelight.horizontalRadiansToTarget();
    double horizontalDegreesToTarget = Math.toDegrees(horizontalRadiansToTarget);
    CommandScheduler.getInstance().schedule(new TurnDeltaAngle(horizontalDegreesToTarget));

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this.getClass().getName());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
