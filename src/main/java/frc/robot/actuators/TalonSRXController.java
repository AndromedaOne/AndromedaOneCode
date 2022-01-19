package frc.robot.actuators;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TalonSRXController extends WPI_TalonSRX {
  private boolean hasEncoder = false;
  private String m_configString;
  private Config m_subsystemConfig;

  public TalonSRXController(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString));
    m_configString = configString;
    m_subsystemConfig = subsystemConfig;
    configure(subsystemConfig, configString);
    hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
    System.out.println("Creating new TalonSRX from port: " + configString);
  }

  private void configure(Config subsystemConfig, String configString) {
    this.configFactoryDefault();
    if (hasEncoder()) {
      this.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    }
    if (subsystemConfig.getBoolean(configString + ".brakeMode")) {
      this.setNeutralMode(NeutralMode.Brake);
    } else {
      this.setNeutralMode(NeutralMode.Coast);
    }
    this.setInverted(subsystemConfig.getBoolean(configString + ".inverted"));
  }

  public double getEncoderPositionTicks() {
    if (!hasEncoder()) {
      return 0;
    }
    double sensorPosition = this.getSelectedSensorPosition();
    sensorPosition = (m_subsystemConfig.getBoolean(m_configString + ".encoderInverted")
        ? -sensorPosition
        : sensorPosition);
    SmartDashboard.putNumber(m_configString + " position", sensorPosition);
    return sensorPosition;
  }

  public double getEncoderVelocityTicks() {
    if (!hasEncoder()) {
      return 0;
    }
    double sensorVelocity = this.getSelectedSensorVelocity();
    sensorVelocity = (m_subsystemConfig.getBoolean(m_configString + ".encoderInverted")
        ? -sensorVelocity
        : sensorVelocity);
    SmartDashboard.putNumber(m_configString + " velocity", sensorVelocity);
    return sensorVelocity;
  }

  public boolean hasEncoder() {
    return hasEncoder;
  }
}