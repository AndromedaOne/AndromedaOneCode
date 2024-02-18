package frc.robot.sensors.distanceSensor.ultrasonicsensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

public class RealUltrasonicSensorPair extends RealSensorBase implements UltrasonicSensor {
  private Ultrasonic m_leftUltrasonic;
  private Ultrasonic m_rightUltrasonic;
  private double m_outlierDetectionThreshold = 10;
  private String m_sensorName;

  /**
   * Creates an ultrasonic pair which takes in two ultrasonics and assumes one is
   * on the left and one is on the right
   * 
   * @param leftPing  left ultrasonic ping port
   * @param leftEcho  left ultrasonic echo port
   * @param rightPing right ultrasonic ping port
   * @param rightEcho right ultrasonic echo port
   */
  public RealUltrasonicSensorPair(String confString) {
    // Everytime we add new ultrasonics we need to disable automatic mode and re
    // enable it
    // rightUltrasonic.setAutomaticMode(false);
    Config conf = Config4905.getConfig4905().getSensorConfig();
    int leftPing = conf.getInt("sensors." + confString + ".leftPing");
    int leftEcho = conf.getInt("sensors." + confString + ".leftEcho");
    int rightPing = conf.getInt("sensors." + confString + ".rightPing");
    int rightEcho = conf.getInt("sensors." + confString + ".rightEcho");
    m_leftUltrasonic = new Ultrasonic(leftPing, leftEcho);
    m_leftUltrasonic.setEnabled(true);
    m_rightUltrasonic = new Ultrasonic(rightPing, rightEcho);
    m_rightUltrasonic.setEnabled(true);
    Ultrasonic.setAutomaticMode(true);
    m_sensorName = confString;
  }

  public double getLeftUltrasonicDistanceInches() {
    return m_leftUltrasonic.getRangeInches();
  }

  public double getRightUltrasonicDistanceInches() {
    return m_rightUltrasonic.getRangeInches();
  }

  /**
   * This will default to return the average between the two ultrasonics, but if
   * the difference between the two ultrasonics are above our threshold then it
   * will return the ultrasonic with the lowest value.
   */
  @Override
  public double getDistanceInches() {
    double leftUltrasonicDist = m_leftUltrasonic.getRangeInches();
    double rightUltrasonicDist = m_rightUltrasonic.getRangeInches();

    // This defaults to average the two ultrasonics to get a distance
    double distance = (leftUltrasonicDist + rightUltrasonicDist) / 2;
    if (Math.abs(leftUltrasonicDist - rightUltrasonicDist) > m_outlierDetectionThreshold) {
      distance = Math.min(leftUltrasonicDist, rightUltrasonicDist);
    }
    return distance;
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber(m_sensorName + ": Left Ultrasonic", getLeftUltrasonicDistanceInches());
    SmartDashboard.putNumber(m_sensorName + ": Right Ultrasonic",
        getRightUltrasonicDistanceInches());
    SmartDashboard.putNumber(m_sensorName + "Ultrasonic", getDistanceInches());
  }
}