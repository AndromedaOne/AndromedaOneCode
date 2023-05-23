package frc.robot.sensors.limitswitchsensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;

public class RealLimitSwitchSensor extends LimitSwitchSensor {
  private DigitalInput limitSwitch;
  private boolean reversedPolarity;

  /**
   * Sets the limit switch to a new DigitalInput with the port specified and
   * records whether or not the limit switch has reversed polarity
   * 
   * @param confString name of the sensor
   */
  public RealLimitSwitchSensor(String confString) {
    Config conf = Config4905.getConfig4905().getSensorConfig();
    limitSwitch = new DigitalInput(conf.getInt("sensors." + confString + ".port"));
    reversedPolarity = conf.getBoolean("sensors." + confString + ".reversedPolarity");
  }

  @Override
  public boolean isAtLimit() {
    if (reversedPolarity) {
      return !limitSwitch.get();
    }
    return limitSwitch.get();
  }

  @Override
  public void updateSmartDashboardReadings() {
    SmartDashboard.putBoolean("Cannon home switch", isAtLimit());
  }
}