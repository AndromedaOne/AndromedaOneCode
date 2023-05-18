// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.gyro;

import java.util.function.DoubleSupplier;

/** Add your docs here. */
public interface Gyro4905 {

  public double getZAngle();

  public double getXAngle();

  public double getYAngle();

  public double getCompassHeading();

  public void calibrate();

  public void reset();

  public double getAngle();

  public double getRate();

  public void close() throws Exception;

  public double getRawZAngle();

  public double getRawXAngle();

  public double getRawYAngle();

  public void setInitialZangleOffset(double offset);

  public DoubleSupplier getYangleDoubleSupplier();

  public DoubleSupplier getZangleDoubleSupplier();

  public DoubleSupplier getCompassHeadingDoubleSupplier();
}
