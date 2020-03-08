/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class FeedBothStagesIntoShooter extends CommandBase {
  /**
   * Creates a new FeedWhenReady.
   */
  FeederBase m_feederBase;
  ShooterBase m_shooterBase;
  BooleanSupplier m_endCondition;
  double counter = 0;
  private static final double kStageOneAndTwoSpeed = 0.4;
  private static final double kStageThreeSpeed = 1.0;

  public FeedBothStagesIntoShooter(FeederBase feederBase, ShooterBase shooterBase, BooleanSupplier endCondition) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_feederBase = feederBase;
    m_shooterBase = shooterBase;
    addRequirements(m_feederBase);
    m_endCondition = endCondition;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
    System.out.println("FeedBothStagesIntoShooter Start");
    counter = 0;
    m_shooterBase.openShooterHood();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean shooterIsReady = m_shooterBase.shooterIsReady();
    if (shooterIsReady && (counter > 20)) {
      m_feederBase.runBothStages(kStageOneAndTwoSpeed, kStageThreeSpeed);
    } else {
      m_feederBase.stopBothStages();
    }
    counter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    System.out.println("FeedBothStagesIntoShooter End");
    m_feederBase.stopBothStages();
    m_shooterBase.closeShooterHood();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_endCondition.getAsBoolean();
  }
}
