package frc.robot.sensors.limitswitchsensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

public class RealLimitSwitchSensor extends RealSensorBase implements LimitSwitchSensor {
  private DigitalInput m_limitSwitch;
  private boolean m_reversedPolarity;
  private String m_name;

  /**
   * Sets the limit switch to a new DigitalInput with the port specified and
   * records whether or not the limit switch has reversed polarity
   * 
   * @param confString name of the sensor
   */
  public RealLimitSwitchSensor(String confString) {
    Config conf = Config4905.getConfig4905().getSensorConfig();
    m_limitSwitch = new DigitalInput(conf.getInt("sensors." + confString + ".port"));
    m_reversedPolarity = conf.getBoolean("sensors." + confString + ".reversedPolarity");
    m_name = confString;
  }

  @Override
  public boolean isAtLimit() {
    if (m_reversedPolarity) {
      return !m_limitSwitch.get();
    }
    return m_limitSwitch.get();
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putBoolean(m_name, isAtLimit());
  }
}