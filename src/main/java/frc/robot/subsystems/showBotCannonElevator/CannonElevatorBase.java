// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannonElevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class CannonElevatorBase extends SubsystemBase {
  /** Creates a new ShowBotCannonElevatorBase. */
  public CannonElevatorBase() {
  }

  public abstract void changeElevation(double speed);

  public abstract void holdElevation();

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
