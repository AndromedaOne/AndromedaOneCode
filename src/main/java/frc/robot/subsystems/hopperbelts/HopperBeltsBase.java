// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.hopperbelts;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface HopperBeltsBase extends SubsystemInterface {

  public void stop();

  public void intake();

  public void eject();

  public void ejectThroughIntake();

  public void setBrakeMode();

  public void setCoastMode();

}
