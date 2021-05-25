// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.romiAnalog41IRSensor;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Add your docs here. */
public class RealRomiAnalog41IRSensor extends RomiAnalog41IRSensor {
  private AnalogInput m_irSensor;

  public RealRomiAnalog41IRSensor(int port) {
    m_irSensor = new AnalogInput(port);
  }

  @Override
  public double getDistance() {

    return m_irSensor.getValue();
  }

  @Override
  public boolean isReal() {
    return true;
  }

  @Override
  public void updateSmartDashboardReadings() {
    SmartDashboard.putNumber("Romi41IRsensor", getDistance());
  }

}
