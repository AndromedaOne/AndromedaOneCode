package frc.robot.actuators;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkLimitSwitch;
import com.typesafe.config.Config;

public class SparkMaxController {
  private CANSparkMax m_sparkMax;
  private boolean m_hasAbsoluteEncoder = false;
  private boolean m_hasForwardLimitSwitch = false;
  private boolean m_hasReverseLimitSwitch = false;
  private RelativeEncoder m_builtInEncoder;
  private AbsoluteEncoder m_absoluteEncoder;
  private SparkLimitSwitch m_forwardLimitSwitch;
  private SparkLimitSwitch m_reverseLimitSwitch;

  public SparkMaxController(Config subsystemConfig, String configString) {
    m_sparkMax = new CANSparkMax(subsystemConfig.getInt("ports." + configString),
        MotorType.kBrushless);
    m_builtInEncoder = m_sparkMax.getEncoder();
    System.out.println("Enabling SparkMaxController \"" + configString + "\" for port "
        + subsystemConfig.getInt("ports." + configString));
    m_hasAbsoluteEncoder = subsystemConfig.getBoolean(configString + ".hasAbsoluteEncoder");
    if (hasAbsoluteEncoder()) {
      m_absoluteEncoder = m_sparkMax.getAbsoluteEncoder(Type.kDutyCycle);
      m_absoluteEncoder
          .setInverted(subsystemConfig.getBoolean(configString + ".absoluteEncoderInverted"));
      // m_absoluteEncoder
      // .setZeroOffset(subsystemConfig.getDouble(configString +
      // ".absoluteEncoderZeroOffset"));
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
      m_builtInEncoder.setPositionConversionFactor(encoderTicksPerRotation);
      m_builtInEncoder.setVelocityConversionFactor(encoderTicksPerRotation);
    }
  }

  public CANSparkMax getMotorController() {
    return m_sparkMax;
  }

  public double getBuiltInEncoderPositionTicks() {
    return m_builtInEncoder.getPosition();
  }

  public double getBuiltInEncoderVelocityTicks() {
    return m_builtInEncoder.getVelocity();
  }

  public boolean hasAbsoluteEncoder() {
    return m_hasAbsoluteEncoder;
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

  public double getSpeed() {
    return m_sparkMax.get();
  }

  public REVLibError setMotorDirectionInverted(boolean inverted) {
    m_sparkMax.setInverted(inverted);
    return REVLibError.kOk;
  }

  // Returns the position between 0 and 1 - It rolls over at 1
  public double getAbsoluteEncoderPosition() {
    return m_absoluteEncoder.getPosition();
  }

  public double getAbsoluteZeroOffset() {
    return m_absoluteEncoder.getZeroOffset();
  }

  public void setAbsoluteZeroOffset(double offset) {
    m_absoluteEncoder.setZeroOffset(offset);
  }

  public void setAbsoluteInverted(boolean inverted) {
    m_absoluteEncoder.setInverted(inverted);
  }
}