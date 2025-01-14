package frc.robot.actuators;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.typesafe.config.Config;

public class SparkMaxController {
  private SparkMax m_sparkMax;
  private boolean m_hasAbsoluteEncoder = false;
  private boolean m_hasForwardLimitSwitch = false;
  private boolean m_hasReverseLimitSwitch = false;
  private RelativeEncoder m_builtInEncoder;
  private AbsoluteEncoder m_absoluteEncoder;
  private SparkLimitSwitch m_forwardLimitSwitch;
  private SparkLimitSwitch m_reverseLimitSwitch;

  public SparkMaxController(Config subsystemConfig, String configString, boolean isSwerve,
      boolean isDrive) {
    m_sparkMax = new SparkMax(subsystemConfig.getInt("ports." + configString),
        MotorType.kBrushless);
    m_builtInEncoder = m_sparkMax.getEncoder();
    System.out.println("Enabling SparkMaxController \"" + configString + "\" for port "
        + subsystemConfig.getInt("ports." + configString));
    m_hasAbsoluteEncoder = subsystemConfig.getBoolean(configString + ".hasAbsoluteEncoder");
    configure(subsystemConfig, configString, isSwerve, isDrive);
  }

  private void configure(Config subsystemConfig, String configString, boolean isSwerve,
      boolean isDrive) {
    SparkMaxConfig sparkConfig = new SparkMaxConfig();
    sparkConfig.smartCurrentLimit(subsystemConfig.getInt(configString + ".currentLimit"));
    if (subsystemConfig.getBoolean(configString + ".brakeMode")) {
      sparkConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
    } else {
      sparkConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);
    }
    if (hasAbsoluteEncoder()) {
      m_absoluteEncoder = m_sparkMax.getAbsoluteEncoder();
      sparkConfig.absoluteEncoder
          .inverted(subsystemConfig.getBoolean(configString + ".absoluteEncoderInverted"));
      sparkConfig.absoluteEncoder
          .zeroOffset(subsystemConfig.getDouble(configString + ".absoluteEncoderZeroOffset"));
    }
    /*********
     * NOTE: you CANNOT disable the hard limit capability on sparkmax controllers
     * there is a bug in the API, no matter what "enableLimitSwitch" is set to, the
     * motor will stop moving if the switch is activated.....
     ******************/
    m_hasForwardLimitSwitch = subsystemConfig.getBoolean(configString + ".hasForwardLimitSwitch");
    if (m_hasForwardLimitSwitch) {
      m_forwardLimitSwitch = m_sparkMax.getForwardLimitSwitch();
      sparkConfig.limitSwitch.forwardLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen);
    }
    m_hasReverseLimitSwitch = subsystemConfig.getBoolean(configString + ".hasReverseLimitSwitch");
    if (m_hasReverseLimitSwitch) {
      m_reverseLimitSwitch = m_sparkMax.getReverseLimitSwitch();
      sparkConfig.limitSwitch.reverseLimitSwitchType(LimitSwitchConfig.Type.kNormallyOpen);
    }
    if (isSwerve && isDrive) {
      sparkConfig.closedLoop.p(subsystemConfig.getDouble("driveKP"));
      sparkConfig.closedLoop.i(subsystemConfig.getDouble("driveKI"));
      sparkConfig.closedLoop.d(subsystemConfig.getDouble("driveKD"));
      sparkConfig.closedLoop.velocityFF(subsystemConfig.getDouble("driveKFF"));
      sparkConfig.openLoopRampRate(subsystemConfig.getDouble("drivekRampRate"));
      sparkConfig.voltageCompensation(subsystemConfig.getDouble("voltageComp"));
      sparkConfig.smartCurrentLimit(subsystemConfig.getInt("driveContinuousCurrentLimit"));
      sparkConfig.inverted(subsystemConfig.getBoolean("driveInvert"));
      double positionConversionFactor = subsystemConfig.getDouble("wheelDiameter") * Math.PI
          / subsystemConfig.getDouble("driveGearRatio");
      sparkConfig.encoder.positionConversionFactor(positionConversionFactor / 39.3701);
      sparkConfig.encoder.velocityConversionFactor(positionConversionFactor * 39.3701 / 60.0);

    } else if (isSwerve && !isDrive) {
      sparkConfig.smartCurrentLimit(subsystemConfig.getInt("angleContinuousCurrentLimit"));
      sparkConfig.closedLoop.p(subsystemConfig.getDouble("angleKP"));
      sparkConfig.closedLoop.i(subsystemConfig.getDouble("angleKI"));
      sparkConfig.closedLoop.d(subsystemConfig.getDouble("angleKD"));
      sparkConfig.closedLoop.velocityFF(subsystemConfig.getDouble("angleKFF"));
      sparkConfig.closedLoop.positionWrappingEnabled(true);
      sparkConfig.closedLoop.positionWrappingMinInput(0);
      sparkConfig.closedLoop.positionWrappingMaxInput(360);
      sparkConfig.closedLoop.outputRange(-1.0, 1.0);
      sparkConfig.voltageCompensation(subsystemConfig.getDouble("voltageComp"));
      sparkConfig.closedLoopRampRate(subsystemConfig.getDouble("anglekRampRate"));
      sparkConfig.inverted(subsystemConfig.getBoolean("angleInvert"));

      sparkConfig.absoluteEncoder
          .positionConversionFactor(subsystemConfig.getInt("angleDegreesPerRotation")
              / subsystemConfig.getInt("angleGearRatio"));
      sparkConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);
    }

    m_sparkMax.configure(sparkConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  public SparkMax getMotorController() {
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
    SparkMaxConfig sparkConfig = new SparkMaxConfig();
    sparkConfig.idleMode(SparkBaseConfig.IdleMode.kCoast);
    m_sparkMax.configure(sparkConfig, ResetMode.kNoResetSafeParameters,
        PersistMode.kNoPersistParameters);
    System.out.println("SparkMax set to coast");
  }

  public void setBrakeMode() {
    SparkMaxConfig sparkConfig = new SparkMaxConfig();
    sparkConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
    m_sparkMax.configure(sparkConfig, ResetMode.kNoResetSafeParameters,
        PersistMode.kNoPersistParameters);
    System.out.println("SparkMax set to brake");
  }

  public void disableAccelerationLimiting() {
    SparkMaxConfig sparkConfig = new SparkMaxConfig();
    sparkConfig.openLoopRampRate(0);
    m_sparkMax.configure(sparkConfig, ResetMode.kNoResetSafeParameters,
        PersistMode.kNoPersistParameters);
  }

  public void enableAccelerationLimiting(double rate) {
    SparkMaxConfig sparkConfig = new SparkMaxConfig();
    sparkConfig.openLoopRampRate(rate);
    m_sparkMax.configure(sparkConfig, ResetMode.kNoResetSafeParameters,
        PersistMode.kNoPersistParameters);
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

  // Returns the position between 0 and 1 - It rolls over at 1
  public double getAbsoluteEncoderPosition() {
    return m_absoluteEncoder.getPosition();
  }

}