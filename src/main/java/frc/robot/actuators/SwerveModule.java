package frc.robot.actuators;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.lib.math.OnboardModuleState;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveConstarts;
import frc.robot.utils.CANSparkMaxUtil;
import frc.robot.utils.CANSparkMaxUtil.Usage;

public class SwerveModule {
  private int m_moduleNumber;
  private Rotation2d m_lastAngle;
  private CANSparkMax m_angleMotor;
  private CANSparkMax m_driveMotor;
  private SparkMaxPIDController m_angleController;
  private SparkMaxPIDController m_driveController;

  private RelativeEncoder driveEncoder;
  private AbsoluteEncoder intergratedAngleEncoder;

  private final SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(
      SwerveDriveConstarts.Swerve.driveKS, SwerveDriveConstarts.Swerve.driveKV,
      SwerveDriveConstarts.Swerve.driveKA);

  public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {
    m_moduleNumber = moduleNumber;

    /* Angle Motor Config */
    m_angleMotor = new CANSparkMax(moduleConstants.getAngleMotorID(), MotorType.kBrushless);
    intergratedAngleEncoder = m_angleMotor.getAbsoluteEncoder(Type.kDutyCycle);
    m_angleController = m_angleMotor.getPIDController();
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new CANSparkMax(moduleConstants.getDriveMotorID(), MotorType.kBrushless);
    driveEncoder = m_driveMotor.getEncoder();
    m_driveController = m_driveMotor.getPIDController();
    configDriveMotor();

    m_lastAngle = getState().angle;
  }

  public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
    desiredState = OnboardModuleState.optimize(desiredState, getState().angle);
    setAngle(desiredState);
    setSpeed(desiredState, isOpenLoop);
  }

  private void configAngleMotor() {
    m_angleMotor.restoreFactoryDefaults();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_angleMotor, Usage.kPositionOnly);
    m_angleMotor.setSmartCurrentLimit(SwerveDriveConstarts.Swerve.angleContinuousCurrentLimit);
    m_angleMotor.setInverted(SwerveDriveConstarts.Swerve.angleInvert);
    m_angleMotor.setIdleMode(SwerveDriveConstarts.Swerve.angleNeutralMode);
    intergratedAngleEncoder
        .setPositionConversionFactor(SwerveDriveConstarts.Swerve.angleConversionFactor);
    m_angleController.setP(SwerveDriveConstarts.Swerve.angleKP);
    m_angleController.setI(SwerveDriveConstarts.Swerve.angleKI);
    m_angleController.setD(SwerveDriveConstarts.Swerve.angleKD);
    m_angleController.setFF(SwerveDriveConstarts.Swerve.angleKFF);
    m_angleMotor.enableVoltageCompensation(SwerveDriveConstarts.Swerve.voltageComp);
    m_angleMotor.burnFlash();
  }

  private void configDriveMotor() {
    m_driveMotor.restoreFactoryDefaults();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_driveMotor, Usage.kAll);
    m_driveMotor.setSmartCurrentLimit(SwerveDriveConstarts.Swerve.driveContinuousCurrentLimit);
    m_driveMotor.setInverted(SwerveDriveConstarts.Swerve.driveInvert);
    m_driveMotor.setIdleMode(SwerveDriveConstarts.Swerve.driveNeutralMode);
    driveEncoder
        .setVelocityConversionFactor(SwerveDriveConstarts.Swerve.driveConversionVelocityFactor);
    driveEncoder
        .setPositionConversionFactor(SwerveDriveConstarts.Swerve.driveConversionPositionFactor);
    m_driveController.setP(SwerveDriveConstarts.Swerve.angleKP);
    m_driveController.setI(SwerveDriveConstarts.Swerve.angleKI);
    m_driveController.setD(SwerveDriveConstarts.Swerve.angleKD);
    m_driveController.setFF(SwerveDriveConstarts.Swerve.angleKFF);
    m_driveMotor.enableVoltageCompensation(SwerveDriveConstarts.Swerve.voltageComp);
    m_driveMotor.burnFlash();
    driveEncoder.setPosition(0.0);

  }

  private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.speedMetersPerSecond
          / SwerveDriveConstarts.Swerve.maxSpeed;
      m_driveMotor.set(percentOutput);
    } else {
      m_driveController.setReference(desiredState.speedMetersPerSecond, ControlType.kVelocity, 0,
          feedForward.calculate(desiredState.speedMetersPerSecond));
    }
  }

  private void setAngle(SwerveModuleState desiredState) {
    Rotation2d angle = (Math
        .abs(desiredState.speedMetersPerSecond) <= (SwerveDriveConstarts.Swerve.maxSpeed * 0.01))
            ? m_lastAngle
            : desiredState.angle;
    m_angleController.setReference(angle.getDegrees(), ControlType.kPosition);
    m_lastAngle = angle;
  }

  public int getModuleNumber() {
    return m_moduleNumber;
  }

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(intergratedAngleEncoder.getPosition());
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(driveEncoder.getVelocity(), getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(driveEncoder.getVelocity(), getAngle());
  }

}
