package frc.robot.sensors.encodersensor.sparkmaxencodersensor;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

public class RealSparkMaxEncoderSensor extends SparkMaxEncoderSensor {
  // TODO Update ticks per inch, it's 1 for now so we don't divide by 0
  int ticksPerInch = 1;   
  private CANEncoder m_SparkMaxEncoder;

  public RealSparkMaxEncoderSensor(CANSparkMax device) {
    m_SparkMaxEncoder = new CANEncoder(device);
  }

  @Override
  public double getDistanceTicks() {
    return m_SparkMaxEncoder.getPosition();
  }

  @Override
  public double getDistanceInches() {
    return getDistanceTicks() / ticksPerInch;
  }

  @Override
  public double getVelocity() {
    return m_SparkMaxEncoder.getVelocity();
  }
}