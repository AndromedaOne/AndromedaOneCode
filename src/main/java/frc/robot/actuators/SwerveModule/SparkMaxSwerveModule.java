package frc.robot.actuators.SwerveModule;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkPIDController;
import com.typesafe.config.Config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.utils.CANSparkMaxUtil;
import frc.robot.utils.CANSparkMaxUtil.Usage;

public class SparkMaxSwerveModule extends SwerveModuleBase {
  private SparkMaxController m_angleMotor;
  private SparkMaxController m_driveMotor;
  private SparkPIDController m_angleController;
  private SparkPIDController m_driveController;
  private double m_lastAngle = 0;
  private Config m_config;

  private RelativeEncoder m_driveEncoder;
  private AbsoluteEncoder m_absoluteAngleEncoder;

  private SimpleMotorFeedforward m_feedForward;

  public SparkMaxSwerveModule(int moduleNumber) {
    super(moduleNumber);
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");
    m_feedForward = new SimpleMotorFeedforward(m_config.getDouble("driveKS"),
        m_config.getDouble("driveKV"), m_config.getDouble("driveKA"));
    /* Angle Motor Config */
    m_angleMotor = new SparkMaxController(m_config, "Mod" + getModuleNumber() + ".angleMotorID");
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new SparkMaxController(m_config, "Mod" + getModuleNumber() + ".driveMotorID");
    configDriveMotor();
  }

  private void configAngleMotor() {
    m_angleMotor.getMotorController().restoreFactoryDefaults();
    m_absoluteAngleEncoder = m_angleMotor.getMotorController()
        .getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
    m_angleController = m_angleMotor.getMotorController().getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_angleMotor.getMotorController(), Usage.kPositionOnly);
    m_angleMotor.getMotorController()
        .setSmartCurrentLimit(m_config.getInt("angleContinuousCurrentLimit"));
    m_absoluteAngleEncoder.setPositionConversionFactor(
        m_config.getInt("angleDegreesPerRotation") / m_config.getInt("angleGearRatio"));
    // since we're using an abolute encoder for motor angle, need to set that as the
    // input
    // to the onboard PID controller
    m_angleController.setFeedbackDevice(m_absoluteAngleEncoder);
    m_angleController.setP(m_config.getDouble("angleKP"));
    m_angleController.setI(m_config.getDouble("angleKI"));
    m_angleController.setD(m_config.getDouble("angleKD"));
    m_angleController.setFF(m_config.getDouble("angleKFF"));
    m_angleController.setPositionPIDWrappingEnabled(true);
    m_angleController.setPositionPIDWrappingMinInput(0);
    m_angleController.setPositionPIDWrappingMaxInput(360);
    m_angleController.setOutputRange(-1.0, 1.0);

    m_angleMotor.getMotorController().enableVoltageCompensation(m_config.getDouble("voltageComp"));
    m_angleMotor.getMotorController().setInverted(m_config.getBoolean("angleInvert"));
    m_angleMotor.getMotorController().setClosedLoopRampRate(m_config.getDouble("anglekRampRate"));
    m_absoluteAngleEncoder.setInverted(m_config.getBoolean("absoluteAngleEncoderInvert"));
    m_angleMotor.getMotorController().burnFlash();
  }

  private void configDriveMotor() {
    m_driveMotor.getMotorController().restoreFactoryDefaults();
    m_driveEncoder = m_driveMotor.getMotorController().getEncoder();
    m_driveController = m_driveMotor.getMotorController().getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_driveMotor.getMotorController(), Usage.kVelocityOnly);
    m_driveMotor.getMotorController()
        .setSmartCurrentLimit(m_config.getInt("driveContinuousCurrentLimit"));
    m_driveMotor.getMotorController().setInverted(m_config.getBoolean("driveInvert"));
    double positionConversionFactor = m_config.getDouble("wheelDiameter") * Math.PI
        / m_config.getDouble("driveGearRatio");
    // These conversion factors are in meters.
    m_driveEncoder.setPositionConversionFactor(positionConversionFactor / 39.3701);
    m_driveEncoder.setVelocityConversionFactor(positionConversionFactor * 39.3701 / 60.0);

    m_driveController.setP(m_config.getDouble("driveKP"));
    m_driveController.setI(m_config.getDouble("driveKI"));
    m_driveController.setD(m_config.getDouble("driveKD"));
    m_driveController.setFF(m_config.getDouble("driveKFF"));
    m_driveMotor.getMotorController().setOpenLoopRampRate(m_config.getDouble("drivekRampRate"));
    m_driveMotor.getMotorController().enableVoltageCompensation(m_config.getDouble("voltageComp"));
    m_driveMotor.getMotorController().burnFlash();
    m_driveEncoder.setPosition(0.0);

  }

  protected void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.speedMetersPerSecond / m_config.getDouble("maxSpeed");
      m_driveMotor.getMotorController().set(percentOutput);
    } else {
      m_driveController.setReference(desiredState.speedMetersPerSecond, ControlType.kVelocity, 0,
          m_feedForward.calculate(desiredState.speedMetersPerSecond));
    }
  }

  protected void setAngle(SwerveModuleState desiredState, boolean overRide) {
    double angle = desiredState.angle.getDegrees();
    if (!overRide
        && Math.abs(desiredState.speedMetersPerSecond) <= (m_config.getDouble("maxSpeed") * 0.01)) {
      angle = m_lastAngle;
    }
    m_lastAngle = angle;
    // need to invert the angle because inverting the motor through the motor
    // controller causes issues with the onboard pidcontroller
    if (angle < 0) {
      angle += 360;
    }
    m_angleController.setReference(angle, ControlType.kPosition);
  }

  @Override
  protected double getAngleMotorRawAngle() {
    double angle = m_absoluteAngleEncoder.getPosition();
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  public void setCoast(boolean value) {
    IdleMode mode = CANSparkMax.IdleMode.kBrake;
    if (value) {
      mode = CANSparkMax.IdleMode.kCoast;
    }
    m_angleMotor.setIdleMode(mode);
    m_driveMotor.setIdleMode(mode);
  }

  public void disableAccelerationLimiting() {
    m_driveMotor.getMotorController().setOpenLoopRampRate(0);
  }

  public void enableAccelerationLimiting() {
    m_driveMotor.getMotorController().setOpenLoopRampRate(m_config.getDouble("drivekRampRate"));
  }

  @Override
  protected double getDriveEncoderPosition() {
    return m_driveEncoder.getPosition();
  }

}
