/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.sequentialgroup;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.FeedBothStagesIntoShooter;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class FeedWhenReady extends SequentialCommandGroup {
  /**
   * Creates a new FeedWhenReady.
   */
  ShooterBase m_shooterBase;
  BooleanSupplier m_endCondition;
  public FeedWhenReady(ShooterBase shooterBase, FeederBase feederBase, BooleanSupplier endCondition) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    addCommands(new FeedBothStagesIntoShooter(feederBase));
    m_shooterBase = shooterBase;
    m_endCondition = endCondition
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    super.initialize();
    m_shooterBase.openShooterHood();
  }

  @Override
  public void end(boolean interrupted) {
    // TODO Auto-generated method stub
    super.end(interrupted);
    m_shooterBase.closeShooterHood();
  }

  @Override
  public boolean isFinished() {
    // TODO Auto-generated method stub
    return m_endCondition.getAsBoolean();
  }
}
