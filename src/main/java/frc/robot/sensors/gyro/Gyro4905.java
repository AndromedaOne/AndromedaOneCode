package frc.robot.sensors.gyro;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;
import frc.robot.utils.AngleConversionUtils;

public abstract class Gyro4905 implements Gyro {

  private double m_initialZAngleReading = 0.0;
  private double m_initialXAngleReading = 0.0;
  private double m_initialYAngleReading = 0.0;

  protected void setInitialZAngleReading(double value) {
    m_initialZAngleReading = value;

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
    double correctedAngle = getRawZAngle() - m_initialZAngleReading;
    Trace.getInstance().addTrace(true, "Gyro", new TracePair<>("Raw Angle", getRawZAngle()),
        new TracePair<>("Corrected Angle", correctedAngle));
    return correctedAngle;
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

  public abstract Gyro4905 cloneMe();

  private class GetCompassHeadingDoubleSupplier implements DoubleSupplier {
    private Gyro4905 m_gyro = cloneMe();

    @Override
    public double getAsDouble() {
      return m_gyro.getCompassHeading();
    }
  }

  public DoubleSupplier getCompassHeadingDoubleSupplier() {
    return new GetCompassHeadingDoubleSupplier();
  }

  private class GetXangleDoubleSupplier implements DoubleSupplier {
    private Gyro4905 m_gyro = cloneMe();

    @Override
    public double getAsDouble() {
      return m_gyro.getXAngle();
    }
  }

  public DoubleSupplier getXangleDoubleSupplier() {
    return new GetXangleDoubleSupplier();
  }

  private class GetYangleDoubleSupplier implements DoubleSupplier {
    private Gyro4905 m_gyro = cloneMe();

    @Override
    public double getAsDouble() {
      return m_gyro.getYAngle();
    }
  }

  public DoubleSupplier getYangleDoubleSupplier() {
    return new GetYangleDoubleSupplier();
  }

  private class GetZangleDoubleSupplier implements DoubleSupplier {
    private Gyro4905 m_gyro = cloneMe();

    @Override
    public double getAsDouble() {
      return m_gyro.getZAngle();
    }
  }

  public DoubleSupplier getZangleDoubleSupplier() {
    return new GetZangleDoubleSupplier();
  }

  public void updateSmartDashboardReadings() {
    SmartDashboard.putNumber("Z Angle", getZAngle());
    SmartDashboard.putNumber("Robot Compass Angle", getCompassHeading());
  }
}
