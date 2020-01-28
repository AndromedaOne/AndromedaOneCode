package frc.robot.actuators;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.typesafe.config.Config;

public class TalonSRXController extends WPI_TalonSRX {
  private boolean hasEncoder = false;

  public TalonSRXController(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString));
    configure();
    hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
  }

  private void configure() {
    this.configFactoryDefault();
  }

  public double getEncoderPositionTicks() {
    return this.getSelectedSensorPosition();
  }

  public double getEncoderVelocityTicks() {
    return this.getSelectedSensorVelocity();
  }

  public boolean hasEncoder() {
    return hasEncoder;
  }
}