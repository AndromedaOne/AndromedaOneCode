// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.DriverStation;

/** Add your docs here. */
public class TopGunLEDs extends RealLEDs {

  public TopGunLEDs() {
    super("TopGunLEDs");
  }

  @Override
  public void periodic() {
    double matchTime = DriverStation.getMatchTime();
    if (matchTime <= 30 && matchTime > 20) {
      setGreen(1);
    } else if (matchTime <= 20 && matchTime > 10) {
      setYellow(1);
    } else if (matchTime <= 10) {
      setRed(1);
      setBlinking();
    }
    super.periodic();
  }
}
