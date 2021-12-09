package frc.robot.sensors.gyro;

import java.util.function.DoubleSupplier;

public class MockGyro extends Gyro4905 {

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
  public void updateSmartDashboardReadings() {
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
  protected double getRawZAngle() {
    return 0;
  }

  @Override
  protected double getRawXAngle() {
    return 0;
  }

  @Override
  protected double getRawYAngle() {
    return 0;
  }

  private class MockGyroCompassHeadingDoubleSupplier implements DoubleSupplier {
    @Override
    public double getAsDouble() {
      return 0;
    }
  }

  @Override
  public DoubleSupplier getCompassHeadingDoubleSupplier() {
    return new MockGyroCompassHeadingDoubleSupplier();
  }

  private class MockGyroZangleDoubleSupplier implements DoubleSupplier {
    @Override
    public double getAsDouble() {
      return 0;
    }
  }

  @Override
  public DoubleSupplier getZangleDoubleSupplier() {
    return new MockGyroZangleDoubleSupplier();
  }
}