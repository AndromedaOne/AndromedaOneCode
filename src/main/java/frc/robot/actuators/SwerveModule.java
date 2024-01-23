package frc.robot.actuators;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkPIDController;
import com.typesafe.config.Config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveConstarts;
import frc.robot.telemetries.Trace;
import frc.robot.utils.CANSparkMaxUtil;
import frc.robot.utils.CANSparkMaxUtil.Usage;

public class SwerveModule {
  private int m_moduleNumber;
  private CANSparkMax m_angleMotor;
  private CANSparkMax m_driveMotor;
  private SparkPIDController m_angleController;
  private SparkPIDController m_driveController;
  private double m_lastAngle = 0;
  private Config m_config;

  private RelativeEncoder driveEncoder;
  private AbsoluteEncoder absoluteAngleEncoder;

  private final SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(
      SwerveDriveConstarts.Swerve.driveKS, SwerveDriveConstarts.Swerve.driveKV,
      SwerveDriveConstarts.Swerve.driveKA);

  public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {
    m_moduleNumber = moduleNumber;
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");

    /* Angle Motor Config */
    Trace.getInstance().logInfo("Construct Mod: " + m_moduleNumber);
    m_angleMotor = new CANSparkMax(m_config.getInt("Mod" + m_moduleNumber + ".angleMotorID"),
        CANSparkLowLevel.MotorType.kBrushless);
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new CANSparkMax(m_config.getInt("Mod" + m_moduleNumber + ".driveMotorID"),
        CANSparkLowLevel.MotorType.kBrushless);
    configDriveMotor();
  }

  public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
    SwerveModuleState optState = SwerveModuleState.optimize(desiredState,
        Rotation2d.fromDegrees(getRawAngle()));
    setAngle(optState);
    setSpeed(optState, isOpenLoop);
  }

  private void configAngleMotor() {
    m_angleMotor.restoreFactoryDefaults();
    absoluteAngleEncoder = m_angleMotor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
    m_angleController = m_angleMotor.getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_angleMotor, Usage.kPositionOnly);
    m_angleMotor.setSmartCurrentLimit(m_config.getInt("angleContinuousCurrentLimit"));
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

    m_angleMotor.enableVoltageCompensation(m_config.getDouble("voltageComp"));
    m_angleMotor.setInverted(m_config.getBoolean("angleInvert"));
    absoluteAngleEncoder.setInverted(m_config.getBoolean("absoluteAngleEncoderInvert"));
    m_angleMotor.burnFlash();
  }

  private void configDriveMotor() {
    m_driveMotor.restoreFactoryDefaults();
    driveEncoder = m_driveMotor.getEncoder();
    m_driveController = m_driveMotor.getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_driveMotor, Usage.kVelocityOnly);
    m_driveMotor.setSmartCurrentLimit(m_config.getInt("driveContinuousCurrentLimit"));
    m_driveMotor.setInverted(m_config.getBoolean("driveInvert"));
    double positionConversionFactor = m_config.getDouble("wheelDiameter") * Math.PI
        / m_config.getDouble("driveGearRatio");
    driveEncoder.setPositionConversionFactor(positionConversionFactor);
    driveEncoder.setVelocityConversionFactor(positionConversionFactor / 60.0);

    m_driveController.setP(m_config.getDouble("driveKP"));
    m_driveController.setI(m_config.getDouble("driveKI"));
    m_driveController.setD(m_config.getDouble("driveKD"));
    m_driveController.setFF(m_config.getDouble("driveKFF"));
    m_driveMotor.enableVoltageCompensation(m_config.getDouble("voltageComp"));
    m_driveMotor.burnFlash();
    driveEncoder.setPosition(0.0);

  }

  private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.speedMetersPerSecond / m_config.getDouble("maxSpeed");
      m_driveMotor.set(percentOutput);
    } else {
      m_driveController.setReference(desiredState.speedMetersPerSecond, ControlType.kVelocity, 0,
          feedForward.calculate(desiredState.speedMetersPerSecond));
    }
  }

  private void setAngle(SwerveModuleState desiredState) {
    double angle = (Math
        .abs(desiredState.speedMetersPerSecond) <= (m_config.getDouble("maxSpeed") * 0.01))
            ? m_lastAngle
            : desiredState.angle.getDegrees();
    m_lastAngle = angle;
    // need to invert the angle because inverting the motor through the motor
    // controller causes
    // issues with the onboard pidcontroller
    // angle -= 180;
    if (angle < 0) {
      angle += 360;
    }

    m_angleController.setReference(angle, ControlType.kPosition);
    SmartDashboard.putNumber("mod " + m_moduleNumber + " setAngle", angle);
    SmartDashboard.putNumber("Mod " + m_moduleNumber + " desiredstate angle",
        desiredState.angle.getDegrees());
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
    return new SwerveModuleState(driveEncoder.getVelocity(), getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(driveEncoder.getVelocity(), getAngle());
  }

  public double getDriveMotorCurrentSpeed() {
    return m_driveMotor.get();
  }

  public void setCoast(boolean value) {
    IdleMode mode = CANSparkMax.IdleMode.kBrake;
    if (value) {
      mode = CANSparkMax.IdleMode.kCoast;
    }
    m_angleMotor.setIdleMode(mode);
    m_driveMotor.setIdleMode(mode);
  }

}
