/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.topGunShooterCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.commands.topGunFeederCommands.RunFeeder;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class TuneShooterFeedForward extends Command {
  /**
   * Creates a new TuneShooterFeedForward.
   */
  private ShooterWheelBase m_topShooterWheel;
  private ShooterWheelBase m_bottomShooterWheel;
  private FeederBase m_feeder;
  private double m_feederSetpoint = 1.0;

  public TuneShooterFeedForward(ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, FeederBase feeder) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_feeder = feeder;
    SmartDashboard.putNumber("Top Shooter Feed Forward Value", 0.00025);
    SmartDashboard.putNumber("Top Shooter p Value", 0.0001);
    SmartDashboard.putNumber("TopShooterRPMTarget", 3000);
    SmartDashboard.putNumber("Bottom Shooter Feed Forward Value", 0.00025);
    SmartDashboard.putNumber("Bottom Shooter p Value", 0.0001);
    SmartDashboard.putNumber("BottomShooterRPMTarget", 3000);
    System.out.println("end constructor of TuneShooterFeedForward");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    System.out.println("TuneShooterInitialize");
    double topFeedForward = SmartDashboard.getNumber("Top Shooter Feed Forward Value", 0.00025);
    double topPValue = SmartDashboard.getNumber("Top Shooter p Value", 0.0001);
    double topShootRPM = SmartDashboard.getNumber("TopShooterRPMTarget", 3000);
    double bottomFeedForward = SmartDashboard.getNumber("Bottom Shooter Feed Forward Value",
        0.00025);
    double bottomPValue = SmartDashboard.getNumber("Bottom Shooter p Value", 0.0001);
    double bottomShootRPM = SmartDashboard.getNumber("BottomShooterRPMTarget", 3000);
    System.out.println("Scheduling  RunShooterWheelVelocity");
    CommandScheduler.getInstance()
        .schedule(new RunOneShooterWheelVelocity(m_topShooterWheel, () -> topShootRPM, true,
            topFeedForward, topPValue, Config4905.getConfig4905().getShooterConfig(), () -> false),
            new RunOneShooterWheelVelocity(m_bottomShooterWheel, () -> bottomShootRPM, true,
                bottomFeedForward, bottomPValue, Config4905.getConfig4905().getShooterConfig(),
                () -> false),
            new RunFeeder(m_feeder, () -> m_feederSetpoint, false, () -> true));
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
