// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdclimber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealSBSDClimber extends SubsystemBase implements SBSDClimberBase {
  private SparkMaxController m_climberWinchMotor; 

  public RealSBSDClimber(){

  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this; 
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void climb(double speed) {
  }

  @Override
  public void reverseClimb(double speed) {
  }

}
