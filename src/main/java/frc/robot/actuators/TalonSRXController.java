package frc.robot.actuators;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.typesafe.config.Config;

public class TalonSRXController extends WPI_TalonSRX {
  private boolean hasEncoder = false;

  public TalonSRXController(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString));
    configure(subsystemConfig, configString);
    hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
    System.out.println("Creating new TalonSRX from port: " + configString);
  }

  private void configure(Config subsystemConfig, String configString) {
    this.configFactoryDefault();
    this.setInverted(subsystemConfig.getBoolean(configString + ".inverted"));
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