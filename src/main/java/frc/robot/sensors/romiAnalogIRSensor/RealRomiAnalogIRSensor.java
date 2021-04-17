// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.romiAnalogIRSensor;

import edu.wpi.first.wpilibj.AnalogInput;

/** Add your docs here. */
public class RealRomiAnalogIRSensor extends RomiAnalogIRSensor {
  private AnalogInput m_irSensor;

  public RealRomiAnalogIRSensor(int port) {
    m_irSensor = new AnalogInput(port);
  }

  @Override
  public double getDistance() {

    return m_irSensor.getValue();
  }

}
