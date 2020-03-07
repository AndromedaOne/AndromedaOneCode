/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.groupcommands.sequentialgroup.FireAllPowerCellsWithLimeDistance;
import frc.robot.sensors.limelightcamera.LimeLEDRegistrar;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class FireAllPowerCellsWithLimeDistanceScheduler extends CommandBase {
  /**
   * Creates a new FireAllPowerCellsWithLimeDistanceScheduler.
   */
  private final double m_timeout = 1.0;
  private Timer m_timer;
  private LimeLightCameraBase m_limelight;
  private boolean isDone;
  FeederBase m_feeder;
  ShooterBase m_shooter;
  private FireAllPowerCellsWithLimeDistance fireAllPowerCellsWithLimeDistance;

  public FireAllPowerCellsWithLimeDistanceScheduler(ShooterBase shooter, FeederBase feeder, LimeLightCameraBase limeLight) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_timer = new Timer();
    m_limelight = limeLight;
    isDone = false;
    m_shooter = shooter;
    m_feeder = feeder;
    LimeLEDRegistrar.getInstance().addCommands(this);
    fireAllPowerCellsWithLimeDistance = new FireAllPowerCellsWithLimeDistance(m_shooter, m_feeder, m_limelight);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
    isDone =false;
    m_limelight.disableLED();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_limelight.targetLock()) {
      CommandScheduler.getInstance().schedule(fireAllPowerCellsWithLimeDistance);
      isDone = true;
    } else if (m_timer.hasPeriodPassed(m_timeout)) {
      isDone = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isDone;
  }
}
