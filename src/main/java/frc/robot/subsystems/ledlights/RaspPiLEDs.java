// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

/** Add your docs here. */
public class RaspPiLEDs extends RealLEDs {
  private Color m_oldColor = null;

  public RaspPiLEDs(Config raspPiConfig, DriveTrainBase driveTrain) {
    super(driveTrain);
  }

  @Override
  protected void updateRGBcolor(Color color) {
    if (m_oldColor != color) {
      SmartDashboard.getNumber("RaspPiLEDred", color.red);
      SmartDashboard.getNumber("RaspPiLEDgreen", color.green);
      SmartDashboard.getNumber("RaspPiLEDblue", color.blue);

    }
  }

}
