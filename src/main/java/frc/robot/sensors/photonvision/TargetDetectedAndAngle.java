// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

/** Add your docs here. */
public class TargetDetectedAndAngle {
  private double m_angle = 0;
  private boolean m_detected = false;

  public TargetDetectedAndAngle(double angle, boolean detected) {
    m_angle = angle;
    m_detected = detected;

  }

  public double getAngle() {
    return m_angle;
  }

  public boolean getDetected() {
    return m_detected;
  }

  public void setAngle(double angle) {
    m_angle = angle;
  }

  public void setDetected(boolean value) {
    m_detected = value;
  }
}
