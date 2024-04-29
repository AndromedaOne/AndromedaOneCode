// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

/** Add your docs here. */
public class TargetDetectedAndDistance {
  private double m_distance = 0;
  private boolean m_detected = false;

  public TargetDetectedAndDistance(double distance, boolean detected) {
    m_distance = distance;
    m_detected = detected;

  }

  public double getDistance() {
    return m_distance;
  }

  public boolean getDetected() {
    return m_detected;
  }

  public void setDistance(double distance) {
    m_distance = distance;
  }

  public void setDetected(boolean value) {
    m_detected = value;
  }
}
