package frc.robot.sensors.infrareddistancesensor;

import edu.wpi.first.wpilibj.AnalogInput;

public class RealInfraredDistanceSensor extends InfraredDistanceSensor {
  private AnalogInput infraredDistanceSensor;

  public RealInfraredDistanceSensor(int portNumber) {
    infraredDistanceSensor = new AnalogInput(portNumber);
  }

  public double getInfraredDistance() {
    return infraredDistanceSensor.getVoltage();
  }
}