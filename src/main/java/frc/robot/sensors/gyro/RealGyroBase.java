package frc.robot.sensors.gyro;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.RealSensorBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AngleConversionUtils;

public abstract class RealGyroBase extends RealSensorBase implements Gyro4905 {

  private double m_initialZAngleReading = 0.0;
  private double m_initialXAngleReading = 0.0;
  private double m_initialYAngleReading = 0.0;
  private boolean m_isZangleOffsetinitialized = false;

  protected void setInitialZAngleReading(double value) {
    m_initialZAngleReading = value;
  }

  public void setInitialZangleOffset(double offset, boolean override) {
    if (!m_isZangleOffsetinitialized || override) {
      System.out.print("orig init Z Angle: " + m_initialZAngleReading);
      m_initialZAngleReading = m_initialZAngleReading + offset;
      m_isZangleOffsetinitialized = true;
      System.out.println(" updated init Z: " + m_initialZAngleReading);
    }
  }

  public void setVisionPoseOffset(double visionAngle) {
    // Adding an offset to the angle based on vision - There's already an offset set
    // somewhere so that's why it's added and not replaced
    m_initialZAngleReading += visionAngle;
    Trace.getInstance().logInfo("Set offset: " + m_initialZAngleReading);
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

  public abstract double getRawZAngle();

  public abstract double getRawXAngle();

  public abstract double getRawYAngle();

  public double getZAngle() {
    return (getRawZAngle() - m_initialZAngleReading);
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

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber("Z Angle", getZAngle());
    SmartDashboard.putNumber("Robot Compass Angle", getCompassHeading());
    SmartDashboard.putNumber("CurrentGyroOffset", m_initialZAngleReading);
    SmartDashboard.putNumber("Raw Z Value", getRawZAngle());
    SmartDashboard.putNumber("Roll", getXAngle());
    SmartDashboard.putNumber("Pitch", getYAngle());
  }
}
