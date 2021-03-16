/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class TuneShooterFeedForward extends CommandBase {
  /**
   * Creates a new TuneShooterFeedForward.
   */
  private ShooterBase m_shooter;

  public TuneShooterFeedForward(ShooterBase shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = shooter;
    SmartDashboard.putNumber("Shooter Feed Forward Value", 0.00025);
    SmartDashboard.putNumber("Shooter p Value", 0.001);
    System.out.println("end constructor of TuneShooterFeedForward");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("TuneShooterInitialize");
    double feedForward = SmartDashboard.getNumber("Shooter Feed Forward Value", 0.00025);
    double pValue = SmartDashboard.getNumber("Shooter p Value", 0.001);
    double shootRPM = SmartDashboard.getNumber("ShooterRPMTarget", 3000);
    System.out.println("Scheduling  RunShooterWheelVelocity");
    CommandScheduler.getInstance()
        .schedule(new RunShooterWheelVelocity(m_shooter, () -> shootRPM, true, feedForward, pValue));
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
