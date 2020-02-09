/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SparkMaxController;

public abstract class ClimberBase extends SubsystemBase {
  /**
   * Creates a new ClimberBase.
   */
  public ClimberBase() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public abstract void extendArms();

  public abstract void retractArms();

  public abstract void stopArms();

  public abstract void driveLeftWinch();

  public abstract void driveRightWinch();

  public abstract void ascend();

  public abstract void stopWinch();

  public abstract void adjustLeftWinch(double adjust);
  public abstract void adjustRightWinch(double adjust);

  public abstract SparkMaxController getLeftWinch();

  public abstract SparkMaxController getRightWinch();
}
