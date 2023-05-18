// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors;

import java.util.ArrayList;

/**
 * Base class for actual (Real) sensors. do not extend Mock sensors from this
 * class. this class is used to update smartdashboard outputs automatically and
 * we don't want to call mock sensors as these should have no updates
 */
public abstract class RealSensorBase {
  /* shared list of real sensors to allow for updating real sensors */
  private static ArrayList<RealSensorBase> m_realSensors = new ArrayList<>();

  protected RealSensorBase() {
    m_realSensors.add(this);
  }

  protected abstract void updateSmartDashboard();

  public static void periodic() {
    for (RealSensorBase sensor : m_realSensors) {
      sensor.updateSmartDashboard();
    }
  }
}
