package frc.robot.actuators;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.typesafe.config.Config;

public class SparkMaxController extends CANSparkMax {
  private CANEncoder m_sparkMaxEncoder;

  public SparkMaxController(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString), MotorType.kBrushless);
    m_sparkMaxEncoder = new CANEncoder(this);
    configure();
  }

  private void configure() {
    this.restoreFactoryDefaults();
  }

  public double getEncoderPositionTicks() {
    return m_sparkMaxEncoder.getPosition();
  }

  public double getEncoderVelocityTicks() {
    return m_sparkMaxEncoder.getVelocity();
  }
}
