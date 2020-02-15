package frc.robot.actuators;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SparkMaxController extends CANSparkMax {
  private boolean m_hasEncoder = false;
  private CANEncoder m_sparkMaxEncoder;
  private String m_configString;
  private Config m_subsystemConfig;

  public SparkMaxController(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString), MotorType.kBrushless);
    System.out.println("Enabling SparkMaxController \"" + configString + "\" for port "
        + subsystemConfig.getInt("ports." + configString));
    m_hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
    if (hasEncoder()) {
      m_sparkMaxEncoder = new CANEncoder(this);
    }
    m_configString = configString;
    m_subsystemConfig = subsystemConfig;
    configure(subsystemConfig, configString);
  }

  private void configure(Config subsystemConfig, String configString) {
    this.restoreFactoryDefaults();
    this.setInverted(subsystemConfig.getBoolean(configString + ".inverted"));
    this.setSmartCurrentLimit(subsystemConfig.getInt(configString + ".currentLimit"));
    if (subsystemConfig.getBoolean(configString + ".brakeMode")) {
      this.setIdleMode(CANSparkMax.IdleMode.kBrake);
    } else {
      this.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }
    if (subsystemConfig.hasPath(configString + ".encoderTicksPerRotation")) {
      double encoderTicksPerRotation = subsystemConfig.getDouble(configString + ".encoderTicksPerRotation");
      m_sparkMaxEncoder.setPositionConversionFactor(encoderTicksPerRotation);
      m_sparkMaxEncoder.setVelocityConversionFactor(encoderTicksPerRotation);
    }
  }

  public double getEncoderPositionTicks() {
    if (!hasEncoder()) {
      return 0;
    }
    double sensorPosition = m_sparkMaxEncoder.getPosition();
    sensorPosition = (m_subsystemConfig.getBoolean(m_configString + ".encoderInverted") ? -sensorPosition
        : sensorPosition);
    SmartDashboard.putNumber(m_configString + " position", sensorPosition);
    return sensorPosition;
  }

  public double getEncoderVelocityTicks() {
    if (!hasEncoder()) {
      return 0;
    }
    double sensorVelocity = m_sparkMaxEncoder.getVelocity();
    sensorVelocity = (m_subsystemConfig.getBoolean(m_configString + ".encoderInverted") ? -sensorVelocity
        : sensorVelocity);
    SmartDashboard.putNumber(m_configString + " velocity", sensorVelocity);
    return sensorVelocity;
  }

  public boolean hasEncoder() {
    return m_hasEncoder;
  }
}
