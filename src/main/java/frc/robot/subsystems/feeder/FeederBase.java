/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.feeder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class FeederBase extends SubsystemBase {
  /**
   * Creates a new FeederBase.
   */
  public FeederBase() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public abstract void runStageOne(double speed);

  public abstract void runStagesTwoAndThree(double stageTwoSpeed, double stageThreeSpeed);

  public abstract void runBothStages(double stageOneAndTwoSpeed, double stageThreeSpeed);

  public abstract void stopStageOne();

  public abstract void stopStageTwo();

  public abstract void stopBothStages();

  public abstract void runReverseBothStages(double stageOneAndTwoSpeed, double stageThreeSpeed);

  public abstract void runReverseStageOne(double speed);

  public abstract void runReverseStageTwo(double stageTwoSpeed, double stageThreeSpeed);

  public abstract void runStageThree(double speed);
}
