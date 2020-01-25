package frc.robot.sensors.ultrasonicsensor;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RealUltrasonicSensorPair extends UltrasonicSensor {
  private Ultrasonic leftUltrasonic;
  private Ultrasonic rightUltrasonic;
  private double outlierDetectionThreshold = 10;

  protected String subsystemName;
  protected String sensorName;

  /**
   * Creates an ultrasonic pair which takes in two ultrasonics and assumes one is
   * on the left and one is on the right
   * 
   * @param leftPing  left ultrasonic ping port
   * @param leftEcho  left ultrasonic echo port
   * @param rightPing right ultrasonic ping port
   * @param rightEcho right ultrasonic echo port
   */
  public RealUltrasonicSensorPair(int leftPing, int leftEcho, int rightPing, int rightEcho) {
    // Everytime we add new ultrasonics we need to disable automatic mode and re
    // enable it
    // rightUltrasonic.setAutomaticMode(false);
    leftUltrasonic = new Ultrasonic(leftPing, leftEcho);
    leftUltrasonic.setEnabled(true);
    rightUltrasonic = new Ultrasonic(rightPing, rightEcho);
    rightUltrasonic.setEnabled(true);
    rightUltrasonic.setAutomaticMode(true);
  }

  public double getLeftUltrasonicDistanceInches() {
    return leftUltrasonic.getRangeInches();
  }

  public double getRightUltrasonicDistanceInches() {
    return rightUltrasonic.getRangeInches();
  }

  /**
   * This will default to return the average between the two ultrasonics, but if
   * the difference between the two ultrasonics are above our threshold then it
   * will return the ultrasonic with the lowest value.
   */
  @Override
  public double getDistanceInches() {
    double leftUltrasonicDist = leftUltrasonic.getRangeInches();
    double rightUltrasonicDist = rightUltrasonic.getRangeInches();
    SmartDashboard.putNumber("Left Ultrasonic", leftUltrasonicDist);
    SmartDashboard.putNumber("Right Ultrasonic", rightUltrasonicDist);
    // This defaults to average the two ultrasonics to get a distance
    double distance = (leftUltrasonicDist + rightUltrasonicDist) / 2;
    if (Math.abs(leftUltrasonicDist - rightUltrasonicDist) > outlierDetectionThreshold) {
      distance = Math.min(leftUltrasonicDist, rightUltrasonicDist);
    }
    SmartDashboard.putNumber("Ultrasonic", distance);
    return distance;
  }

  public double getMinDistanceInches() {
    return Math.min(leftUltrasonic.getRangeInches(), rightUltrasonic.getRangeInches());
  }
}