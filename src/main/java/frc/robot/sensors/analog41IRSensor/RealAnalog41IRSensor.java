// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.analog41IRSensor;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.RealSensorBase;

/** Add your docs here. */
public class RealAnalog41IRSensor extends RealSensorBase implements Analog41IRSensor {
  private AnalogInput m_irSensor;

  public RealAnalog41IRSensor(int port) {
    m_irSensor = new AnalogInput(port);
  }

  @Override
  public double getDistance() {
    // this equation is from adafruit's github arduino code
    // https://github.com/qub1750ul/Arduino_SharpIR/blob/master/src/SharpIR.cpp
    double distance = 2076.0 / (m_irSensor.getValue() - 11.0);
    if (distance > 30) {
      return 31;
    } else if (distance < 4) {
      return 3;
    } else {
      return distance;
    }
  }

  @Override
  public boolean isReal() {
    return true;
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber("analog41IRsensor", getDistance());
  }

}
