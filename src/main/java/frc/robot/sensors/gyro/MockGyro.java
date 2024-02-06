package frc.robot.sensors.gyro;

import java.util.function.DoubleSupplier;

public class MockGyro implements Gyro4905 {

  @Override
  public double getZAngle() {
    return 0;
  }

  @Override
  public double getXAngle() {
    return 0;
  }

  @Override
  public double getYAngle() {
    return 0;
  }

  @Override
  public double getCompassHeading() {
    return 0;
  }

  @Override
  public void calibrate() {
  }

  @Override
  public void reset() {
  }

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public double getRate() {
    return 0;
  }

  @Override
  public void close() throws Exception {
  }

  @Override
  public double getRawZAngle() {
    return 0;
  }

  @Override
  public double getRawXAngle() {
    return 0;
  }

  @Override
  public double getRawYAngle() {
    return 0;
  }

  @Override
  public void setInitialZangleOffset(double offset) {
  }

  @Override
  public DoubleSupplier getYangleDoubleSupplier() {
    return (() -> 0);
  }

  @Override
  public DoubleSupplier getZangleDoubleSupplier() {
    return (() -> 0);
  }

  @Override
  public DoubleSupplier getCompassHeadingDoubleSupplier() {
    return (() -> 0);
  }

  @Override
  public boolean getIsCalibrated() {
    return true;
  }

}