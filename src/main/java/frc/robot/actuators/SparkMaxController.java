package frc.robot.actuators;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkLimitSwitch;
import com.typesafe.config.Config;

public class SparkMaxController {
  private CANSparkMax m_sparkMax;
  private boolean m_hasEncoder = false;
  private boolean m_hasForwardLimitSwitch = false;
  private boolean m_hasReverseLimitSwitch = false;
  private RelativeEncoder m_sparkMaxEncoder;
  private String m_configString;
  private Config m_subsystemConfig;
  private SparkLimitSwitch m_forwardLimitSwitch;
  private SparkLimitSwitch m_reverseLimitSwitch;

  public SparkMaxController(Config subsystemConfig, String configString) {
    m_sparkMax = new CANSparkMax(subsystemConfig.getInt("ports." + configString),
        MotorType.kBrushless);
    System.out.println("Enabling SparkMaxController \"" + configString + "\" for port "
        + subsystemConfig.getInt("ports." + configString));
    m_hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
    if (hasEncoder()) {
      m_sparkMaxEncoder = m_sparkMax.getEncoder();
    }
    /*********
     * NOTE: you CANNOT disable the hard limit capability on sparkmax controllers
     * there is a bug in the API, no matter what "enableLimitSwitch" is set to, the
     * motor will stop moving if the switch is activated.....
     ******************/
    m_hasForwardLimitSwitch = subsystemConfig.getBoolean(configString + ".hasForwardLimitSwitch");
    if (m_hasForwardLimitSwitch) {
      m_forwardLimitSwitch = m_sparkMax.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
    }
    m_hasReverseLimitSwitch = subsystemConfig.getBoolean(configString + ".hasReverseLimitSwitch");
    if (m_hasReverseLimitSwitch) {
      m_reverseLimitSwitch = m_sparkMax.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
    }
    m_configString = configString;
    m_subsystemConfig = subsystemConfig;
    configure(subsystemConfig, configString);
  }

  private void configure(Config subsystemConfig, String configString) {
    m_sparkMax.restoreFactoryDefaults();
    m_sparkMax.setInverted(subsystemConfig.getBoolean(configString + ".inverted"));
    m_sparkMax.setSmartCurrentLimit(subsystemConfig.getInt(configString + ".currentLimit"));
    if (subsystemConfig.getBoolean(configString + ".brakeMode")) {
      m_sparkMax.setIdleMode(CANSparkMax.IdleMode.kBrake);
    } else {
      m_sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }
    if (subsystemConfig.hasPath(configString + ".encoderTicksPerRotation")) {
      double encoderTicksPerRotation = subsystemConfig
          .getDouble(configString + ".encoderTicksPerRotation");
      m_sparkMaxEncoder.setPositionConversionFactor(encoderTicksPerRotation);
      m_sparkMaxEncoder.setVelocityConversionFactor(encoderTicksPerRotation);
    }
  }

  public CANSparkMax getMotorController() {
    return m_sparkMax;
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
    m_sparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
    System.out.println("SparxMax set to coast");
  }

  public void setBrakeMode() {
    m_sparkMax.setIdleMode(CANSparkMax.IdleMode.kBrake);
    System.out.println("SparxMax set to brake");
  }

  public void setIdleMode(IdleMode mode) {
    m_sparkMax.setIdleMode(mode);
  }

  public void resetEncoder() {
    m_sparkMax.getEncoder().setPosition(0);
  }

  public void setSpeed(double speed) {
    m_sparkMax.set(speed);
  }

  public SparkAbsoluteEncoder getAbsoluteEncoder(SparkAbsoluteEncoder.Type type) {
    return m_sparkMax.getAbsoluteEncoder(type);
  }

  public double getSpeed() {
    return m_sparkMax.get();
  }
}