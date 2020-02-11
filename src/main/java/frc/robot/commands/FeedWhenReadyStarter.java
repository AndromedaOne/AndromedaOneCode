/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.groupcommands.sequentialgroup.FeedWhenReady;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class FeedWhenReadyStarter extends CommandBase {
  /**
   * Creates a new FeedWhenReadyStarter.
   */
  private ShooterBase m_shooterBase;
  private FeederBase m_feederBase;
  private boolean commandGroupScheduledFlag = false;

  public FeedWhenReadyStarter(ShooterBase shooterBase, FeederBase feederBase) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooterBase = shooterBase;
    m_feederBase = feederBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    commandGroupScheduledFlag = true;
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(m_shooterBase.shooterIsReady() && !commandGroupScheduledFlag){
      CommandScheduler.getInstance().schedule(new FeedWhenReady(m_shooterBase, m_feederBase));
      commandGroupScheduledFlag = true;
    }
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
