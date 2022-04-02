// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;

/** Add your docs here. */
public class TopGunLEDs extends RealLEDs {

  public TopGunLEDs() {
    super("TopGunLEDs");
  }

  @Override
  public void periodic() {
    if (Robot.getInstance().isTeleop()) {
      double matchTime = DriverStation.getMatchTime();
      if (matchTime <= 30 && matchTime > 20) {
        setGreen(1);
        setSolid();
      } else if (matchTime <= 20 && matchTime > 10) {
        setYellow(1);
        setSolid();
      } else if (matchTime <= 10 && matchTime >= 0) {
        setRed(1);
        setBlinking();
      } else {
        switch (getTeleOpMode()) {
        case SLOW:
          setBlue(1);
          setSolid();
          break;

        case MID:
          setPurple(1);
          setSolid();
          break;

        case FAST:
          setGreen(1);
          setSolid();
          break;

        default:
          setRed(1);
          setSolid();
        }
      }
    } else if (Robot.getInstance().isAutonomous()) {
      setWhite(1);
      setSolid();
    } else {
      setRainbow();
    }
    super.periodic();
  }
}
