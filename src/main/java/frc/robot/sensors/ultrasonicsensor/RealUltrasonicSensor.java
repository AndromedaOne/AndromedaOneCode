package frc.robot.sensors.ultrasonicsensor;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RealUltrasonicSensor extends UltrasonicSensor {
  private Ultrasonic ultrasonic;

  protected String subsystemName;
  protected String sensorName;

  /**
   * Creates the ultrasonic with the ping and echo ports passed in
   * 
   * @param ping Port for the ping
   * @param echo Port for the echo
   */
  public RealUltrasonicSensor(int ping, int echo) {
    ultrasonic = new Ultrasonic(ping, echo);
    ultrasonic.setEnabled(true);
    ultrasonic.setAutomaticMode(true);
  }

  @Override
  public double getDistanceInches() {
    SmartDashboard.putNumber("Front Ultrasonic", ultrasonic.getRangeInches());
    double distance = ultrasonic.getRangeInches();
    return distance;
  }
}