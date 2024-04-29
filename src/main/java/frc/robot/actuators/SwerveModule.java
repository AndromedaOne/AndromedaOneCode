package frc.robot.actuators;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkPIDController;
import com.typesafe.config.Config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Config4905;
import frc.robot.telemetries.Trace;
import frc.robot.utils.CANSparkMaxUtil;
import frc.robot.utils.CANSparkMaxUtil.Usage;

public class SwerveModule {
  private int m_moduleNumber;
  private SparkMaxController m_angleMotor;
  private SparkMaxController m_driveMotor;
  private SparkPIDController m_angleController;
  private SparkPIDController m_driveController;
  private double m_lastAngle = 0;
  private Config m_config;

  private RelativeEncoder driveEncoder;
  private AbsoluteEncoder absoluteAngleEncoder;

  private SimpleMotorFeedforward m_feedForward;

  public SwerveModule(int moduleNumber) {
    m_moduleNumber = moduleNumber;
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");
    m_feedForward = new SimpleMotorFeedforward(m_config.getDouble("driveKS"),
        m_config.getDouble("driveKV"), m_config.getDouble("driveKA"));
    /* Angle Motor Config */
    Trace.getInstance().logInfo("Construct Mod: " + m_moduleNumber);
    m_angleMotor = new SparkMaxController(m_config, "Mod" + m_moduleNumber + ".angleMotorID");
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new SparkMaxController(m_config, "Mod" + m_moduleNumber + ".driveMotorID");
    configDriveMotor();
  }

  public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop,
      boolean overRide) {
    SwerveModuleState optState = SwerveModuleState.optimize(desiredState,
        Rotation2d.fromDegrees(getRawAngle()));
    setAngle(optState, overRide);
    setSpeed(optState, isOpenLoop);
  }

  private void configAngleMotor() {
    m_angleMotor.getMotorController().restoreFactoryDefaults();
    absoluteAngleEncoder = m_angleMotor.getMotorController()
        .getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
    m_angleController = m_angleMotor.getMotorController().getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_angleMotor.getMotorController(), Usage.kPositionOnly);
    m_angleMotor.getMotorController()
        .setSmartCurrentLimit(m_config.getInt("angleContinuousCurrentLimit"));
    absoluteAngleEncoder.setPositionConversionFactor(
        m_config.getInt("angleDegreesPerRotation") / m_config.getInt("angleGearRatio"));
    // since we're using an abolute encoder for motor angle, need to set that as the
    // input
    // to the onboard PID controller
    m_angleController.setFeedbackDevice(absoluteAngleEncoder);
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
    absoluteAngleEncoder.setInverted(m_config.getBoolean("absoluteAngleEncoderInvert"));
    m_angleMotor.getMotorController().burnFlash();
  }

  private void configDriveMotor() {
    m_driveMotor.getMotorController().restoreFactoryDefaults();
    driveEncoder = m_driveMotor.getMotorController().getEncoder();
    m_driveController = m_driveMotor.getMotorController().getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_driveMotor.getMotorController(), Usage.kVelocityOnly);
    m_driveMotor.getMotorController()
        .setSmartCurrentLimit(m_config.getInt("driveContinuousCurrentLimit"));
    m_driveMotor.getMotorController().setInverted(m_config.getBoolean("driveInvert"));
    double positionConversionFactor = m_config.getDouble("wheelDiameter") * Math.PI
        / m_config.getDouble("driveGearRatio");
    // These conversion factors are in meters.
    driveEncoder.setPositionConversionFactor(positionConversionFactor / 39.3701);
    driveEncoder.setVelocityConversionFactor(positionConversionFactor * 39.3701 / 60.0);

    m_driveController.setP(m_config.getDouble("driveKP"));
    m_driveController.setI(m_config.getDouble("driveKI"));
    m_driveController.setD(m_config.getDouble("driveKD"));
    m_driveController.setFF(m_config.getDouble("driveKFF"));
    m_driveMotor.getMotorController().setOpenLoopRampRate(m_config.getDouble("drivekRampRate"));
    m_driveMotor.getMotorController().enableVoltageCompensation(m_config.getDouble("voltageComp"));
    m_driveMotor.getMotorController().burnFlash();
    driveEncoder.setPosition(0.0);

  }

  private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.speedMetersPerSecond / m_config.getDouble("maxSpeed");
      m_driveMotor.getMotorController().set(percentOutput);
    } else {
      m_driveController.setReference(desiredState.speedMetersPerSecond, ControlType.kVelocity, 0,
          m_feedForward.calculate(desiredState.speedMetersPerSecond));
    }
  }

  private void setAngle(SwerveModuleState desiredState, boolean overRide) {
    double angle = desiredState.angle.getDegrees();
    if (!overRide
        && Math.abs(desiredState.speedMetersPerSecond) <= (m_config.getDouble("maxSpeed") * 0.01)) {
      angle = m_lastAngle;
    }
    m_lastAngle = angle;

    // need to invert the angle because inverting the motor through the motor
    // controller causes
    // issues with the onboard pidcontroller
    // angle -= 180;
    if (angle < 0) {
      angle += 360;
    }

    m_angleController.setReference(angle, ControlType.kPosition);
  }

  public int getModuleNumber() {
    return m_moduleNumber;
  }

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(getRawAngle());
  }

  public double getRawAngle() {
    double angle = absoluteAngleEncoder.getPosition();
    // angle -= 180;
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(driveEncoder.getPosition(), getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(driveEncoder.getPosition(), getAngle());
  }

  public double getDriveMotorCurrentSpeed() {
    return m_driveMotor.getMotorController().get();
  }

  public void setCoast(boolean value) {
    Trace.getInstance().logInfo("setCoast: " + value);
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

}
