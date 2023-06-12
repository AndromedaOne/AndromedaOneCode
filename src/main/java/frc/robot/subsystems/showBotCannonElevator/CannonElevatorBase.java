// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannonElevator;

import frc.robot.subsystems.SubsystemInterface;

public interface CannonElevatorBase extends SubsystemInterface {
  public abstract void changeElevation(double speed);

  public abstract void holdElevation();

}
