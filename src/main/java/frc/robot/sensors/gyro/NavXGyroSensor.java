package frc.robot.sensors.gyro;

public abstract class NavXGyroSensor {

  /**
   * Gets the Z angle and subtracts the initial angle member variable from it.
   * 
   * @return gyro.getAngle() - initialAngleReading
   */
  public abstract double getZAngle();

  public abstract double getXAngle();

  public abstract double getYAngle();

  /**
   * Returns the current compass heading of the robot Between 0 - 360
   */
  public abstract double getCompassHeading();

  public abstract void updateSmartDashboardReadings();

  public abstract void setAngleAdjustment(double angle);
}
