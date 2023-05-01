// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.subsystems.drivetrain.DriveTrain;

/** Add your docs here. */
public class BillsLEDs extends RealLEDs {
  private DigitalOutput m_red;
  private DigitalOutput m_green;
  private DigitalOutput m_blue;

  public BillsLEDs(Config conf, DriveTrain driveTrain) {
    super(driveTrain);
    m_red = new DigitalOutput(conf.getInt("Red"));
    m_red.enablePWM(0);
    m_green = new DigitalOutput(conf.getInt("Green"));
    m_green.enablePWM(0);
    m_blue = new DigitalOutput(conf.getInt("Blue"));
    m_blue.enablePWM(0);
  }

  @Override
  protected void updateRGBcolor(Color color) {
    m_red.updateDutyCycle(color.red);
    m_blue.updateDutyCycle(color.blue);
    m_green.updateDutyCycle(color.green);
  }
}
