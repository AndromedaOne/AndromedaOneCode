// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.function.DoubleSupplier;

/** Add your docs here. */
public class TargetDistanceAndAngle {
  private double m_x = 0;
  private double m_theta = 0;
  private boolean m_detected = false;

  public TargetDistanceAndAngle(double x, double theta, boolean detected) {
    m_x = x;
    m_theta = theta;
    m_detected = detected;
  }

  public double getDistance() {
    return m_x;
  }

  public double getAngle() {
    return m_theta;
  }

  public boolean getDetected() {
    return m_detected;
  }

  public void setDistance(double distance) {
    m_x = distance;
  }

  public void setAngle(double angle) {
    m_theta = angle;
  }

  public void setDetected(boolean value) {
    m_detected = value;
  }

  public TargetDistanceSupplier getTargetDistanceSupplier() {
    return new TargetDistanceSupplier();
  }

  public TargetAngleSupplier getTargetAngleSupplier() {
    return new TargetAngleSupplier();
  }

  public class TargetDistanceSupplier implements DoubleSupplier {

    @Override
    public double getAsDouble() {
      return m_x;
    }

  }

  public class TargetAngleSupplier implements DoubleSupplier {

    @Override
    public double getAsDouble() {
      return m_theta;
    }

  }
}
