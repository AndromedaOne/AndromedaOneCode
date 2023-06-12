// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import frc.robot.subsystems.SubsystemInterface;

public interface ShooterAlignmentBase extends SubsystemInterface {
  public void rotateShooter(double speed);

  public boolean atTopLimitSwitch();

  public boolean atBottomLimitSwitch();

  public double getAngle();

  public String getShooterName();

  public void stopShooterAlignment();

  public void setCoastMode();

  public void setBrakeMode();

  public boolean getInitialized();

  public void setInitialized();

  public void setOffset(double offset);

  public double getOffset();

  public void extendShooterArms();

  public void stowShooterArms();
}
