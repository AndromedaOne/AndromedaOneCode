package frc.robot.sensors.infrareddistancesensor;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Config4905;

public class RealInfraredDistanceSensor extends InfraredDistanceSensor {
  private AnalogInput infraredDistanceSensor;

  public RealInfraredDistanceSensor(String configString) {
    infraredDistanceSensor = new AnalogInput(
        Config4905.getConfig4905().getSensorConfig().getInt("sensors." + configString + ".port"));
  }

  public double getInfraredDistance() {
    return infraredDistanceSensor.getVoltage();
  }
}