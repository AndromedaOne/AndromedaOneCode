package frc.robot.actuators;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.typesafe.config.Config;

public class SparkMaxController extends CANSparkMax {
  private boolean m_hasEncoder = false;
  private boolean m_hasForwardLimitSwitch = false;
  private boolean m_hasReverseLimitSwitch = false;
  private RelativeEncoder m_sparkMaxEncoder;
  private String m_configString;
  private Config m_subsystemConfig;
  private SparkMaxLimitSwitch m_forwardLimitSwitch;
  private SparkMaxLimitSwitch m_reverseLimitSwitch;

  public SparkMaxController(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString), MotorType.kBrushless);
    System.out.println("Enabling SparkMaxController \"" + configString + "\" for port "
        + subsystemConfig.getInt("ports." + configString));
    m_hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
    if (hasEncoder()) {
      m_sparkMaxEncoder = this.getEncoder();
    }
    m_hasForwardLimitSwitch = subsystemConfig.getBoolean(configString + ".hasForwardLimitSwitch");
    if (m_hasForwardLimitSwitch) {
      m_forwardLimitSwitch = this.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
      m_forwardLimitSwitch.enableLimitSwitch(false);
    }
    m_hasReverseLimitSwitch = subsystemConfig.getBoolean(configString + ".hasReverseLimitSwitch");
    if (m_hasReverseLimitSwitch) {
      m_reverseLimitSwitch = this.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
      m_reverseLimitSwitch.enableLimitSwitch(false);
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
      double encoderTicksPerRotation = subsystemConfig
          .getDouble(configString + ".encoderTicksPerRotation");
      m_sparkMaxEncoder.setPositionConversionFactor(encoderTicksPerRotation);
      m_sparkMaxEncoder.setVelocityConversionFactor(encoderTicksPerRotation);
    }
  }

  public double getEncoderPositionTicks() {
    if (!hasEncoder()) {
      return 0;
    }
    double sensorPosition = m_sparkMaxEncoder.getPosition();
    sensorPosition = (m_subsystemConfig.getBoolean(m_configString + ".encoderInverted")
        ? -sensorPosition
        : sensorPosition);
    return sensorPosition;
  }

  public double getEncoderVelocityTicks() {
    if (!hasEncoder()) {
      return 0;
    }
    double sensorVelocity = m_sparkMaxEncoder.getVelocity();
    sensorVelocity = (m_subsystemConfig.getBoolean(m_configString + ".encoderInverted")
        ? -sensorVelocity
        : sensorVelocity);
    return sensorVelocity;
  }

  public boolean hasEncoder() {
    return m_hasEncoder;
  }

  public boolean isForwardLimitSwitchOn() {
    return m_forwardLimitSwitch.isPressed();
  }

  public boolean isReverseLimitSwitchOn() {
    return m_reverseLimitSwitch.isPressed();
  }

  public void setCoastMode() {
    this.setIdleMode(CANSparkMax.IdleMode.kCoast);
    System.out.println("SparxMax set to coast");
  }

  public void setBrakeMode() {
    this.setIdleMode(CANSparkMax.IdleMode.kBrake);
    System.out.println("SparxMax set to brake");
  }
}