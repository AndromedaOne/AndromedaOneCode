package frc.robot.sensors.ultrasonicsensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;

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
  public RealUltrasonicSensor(String confString) {
    Config conf = Config4905.getConfig4905().getSensorConfig();
    int ping = conf.getInt("sensors." + confString + ".ping");
    int echo = conf.getInt("sensors." + confString + ".echo");
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