/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class TuneShooterFeedForward extends CommandBase {
  /**
   * Creates a new TuneShooterFeedForward.
   */
  private ShooterBase m_topShooterWheel;
  private ShooterBase m_bottomShooterWheel;

  public TuneShooterFeedForward(ShooterBase topShooterWheel, ShooterBase bottomShooterWheel) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    SmartDashboard.putNumber("Top Shooter Feed Forward Value", 0.00025);
    SmartDashboard.putNumber("Top Shooter p Value", 0.001);
    SmartDashboard.putNumber("Bottom Shooter Feed Forward Value", 0.00025);
    SmartDashboard.putNumber("Bottom Shooter p Value", 0.001);
    System.out.println("end constructor of TuneShooterFeedForward");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("TuneShooterInitialize");
    double topFeedForward = SmartDashboard.getNumber("Top Shooter Feed Forward Value", 0.00025);
    double topPValue = SmartDashboard.getNumber("Top Shooter p Value", 0.001);
    double topShootRPM = SmartDashboard.getNumber("TopShooterRPMTarget", 3000);
    double bottomFeedForward = SmartDashboard.getNumber("Bottom Shooter Feed Forward Value",
        0.00025);
    double bottomPValue = SmartDashboard.getNumber("Bottom Shooter p Value", 0.001);
    double bottomShootRPM = SmartDashboard.getNumber("BottomShooterRPMTarget", 3000);
    System.out.println("Scheduling  RunShooterWheelVelocity");
    CommandScheduler.getInstance().schedule(
        new RunOneShooterWheelVelocity(m_topShooterWheel, () -> topShootRPM, true, topFeedForward,
            topPValue, Config4905.getConfig4905().getShooterConfig()),
        new RunOneShooterWheelVelocity(m_bottomShooterWheel, () -> bottomShootRPM, true,
            bottomFeedForward, bottomPValue, Config4905.getConfig4905().getShooterConfig()));
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
