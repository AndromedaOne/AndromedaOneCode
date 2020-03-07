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

public class ReverseFeederAndShooter extends CommandBase {
  /**
   * Creates a new ReverseFeeder.
   */
  private FeederBase m_feederBase;
  private ShooterBase m_shooterBase;
  private BooleanSupplier m_endCondition;
  private static final double STAGE_ONE_AND_TWO_SPEED = 0.5;
  private static final double STAGE_THREE_SPEED = 0.5;
  private static final double REVERSE_SHOOTER_SPEED = -0.3;

  public ReverseFeederAndShooter(FeederBase feederbase, ShooterBase shooterBase, BooleanSupplier endCondition) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_feederBase = feederbase;
    m_endCondition = endCondition;
    m_shooterBase = shooterBase;
    addRequirements(feederbase, shooterBase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    DefaultFeederCommand.setFeederStateToThreeLoaded();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooterBase.setShooterSeriesPower(REVERSE_SHOOTER_SPEED);
    m_shooterBase.setShooterWheelPower(REVERSE_SHOOTER_SPEED);
    m_feederBase.runReverseBothStages(STAGE_ONE_AND_TWO_SPEED, STAGE_THREE_SPEED);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooterBase.setShooterSeriesPower(0);
    m_shooterBase.setShooterWheelPower(0);
    m_feederBase.stopBothStages();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_endCondition.getAsBoolean();
  }
}
