package frc.robot.sensors.ultrasonicsensor;

public abstract class UltrasonicSensor {

  /**
   * @return the distance in inches from whatever is in front of the Ultrasonic
   */
  public abstract double getDistanceInches();

  public double getMinDistanceInches() {
    return getDistanceInches();
  }
}