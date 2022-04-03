package frc.robot.sensors.gyro;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.utils.AngleConversionUtils;

public abstract class Gyro4905 implements Gyro {

  private double m_initialZAngleReading = 0.0;
  private double m_initialXAngleReading = 0.0;
  private double m_initialYAngleReading = 0.0;
  private boolean m_gyroOffsetDone = false;

  protected void setInitialZAngleReading(double value) {
    m_initialZAngleReading = value;

  }

  public double getInitialZAngleReading() {
    return m_initialZAngleReading;
  }

  protected void setInitialXAngleReading(double value) {
    m_initialXAngleReading = value;
  }

  protected void setInitialYAngleReading(double value) {
    m_initialYAngleReading = value;
  }

  protected abstract double getRawZAngle();

  protected abstract double getRawXAngle();

  protected abstract double getRawYAngle();

  public double getZAngle() {
    return getRawZAngle() - m_initialZAngleReading;
  }

  public double getXAngle() {
    return (getRawXAngle() - m_initialXAngleReading);
  }

  public double getYAngle() {
    return (getRawYAngle() - m_initialYAngleReading);
  }

  /**
   * Returns the current compass heading of the robot Between 0 - 360
   */
  public double getCompassHeading() {
    return AngleConversionUtils.ConvertAngleToCompassHeading(getZAngle());
  }

  private class GetCompassHeadingDoubleSupplier implements DoubleSupplier {

    @Override
    public double getAsDouble() {
      return getCompassHeading();
    }
  }

  public DoubleSupplier getCompassHeadingDoubleSupplier() {
    return new GetCompassHeadingDoubleSupplier();
  }

  private class GetXangleDoubleSupplier implements DoubleSupplier {

    @Override
    public double getAsDouble() {
      return getXAngle();
    }
  }

  public DoubleSupplier getXangleDoubleSupplier() {
    return new GetXangleDoubleSupplier();
  }

  private class GetYangleDoubleSupplier implements DoubleSupplier {

    @Override
    public double getAsDouble() {
      return getYAngle();
    }
  }

  public DoubleSupplier getYangleDoubleSupplier() {
    return new GetYangleDoubleSupplier();
  }

  private class GetZangleDoubleSupplier implements DoubleSupplier {

    @Override
    public double getAsDouble() {
      return getZAngle();
    }
  }

  public DoubleSupplier getZangleDoubleSupplier() {
    return new GetZangleDoubleSupplier();
  }

  public void setInitialOffset(double offset) {
    if (!m_gyroOffsetDone) {
      m_initialZAngleReading = -offset + m_initialZAngleReading;
      m_gyroOffsetDone = true;
    }
  }

  public void updateSmartDashboardReadings() {
    SmartDashboard.putNumber("Z Angle", getZAngle());
    SmartDashboard.putNumber("Robot Compass Angle", getCompassHeading());
    SmartDashboard.putNumber("CurrentGyroOffset", m_initialZAngleReading);
    SmartDashboard.putNumber("Raw Z Value", getRawZAngle());
  }
}
