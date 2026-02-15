package frc.robot.sensors.distanceSensor.ultrasonicsensor;

public interface UltrasonicSensor {

  /**
   * @return the distance in inches from whatever is in front of the Ultrasonic
   */
  public double getDistanceInches();
}