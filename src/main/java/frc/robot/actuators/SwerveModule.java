package frc.robot.actuators;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

  private RelativeEncoder driveEncoder;
  private AbsoluteEncoder absoluteAngleEncoder;

  private final SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(
      SwerveDriveConstarts.Swerve.driveKS, SwerveDriveConstarts.Swerve.driveKV,
      SwerveDriveConstarts.Swerve.driveKA);

  public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {
    m_moduleNumber = moduleNumber;

    /* Angle Motor Config */
    Trace.getInstance().logInfo("Construct Mod: " + m_moduleNumber);
    m_angleMotor = new CANSparkMax(moduleConstants.getAngleMotorID(),
        CANSparkLowLevel.MotorType.kBrushless);
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new CANSparkMax(moduleConstants.getDriveMotorID(),
        CANSparkLowLevel.MotorType.kBrushless);
    configDriveMotor();
  }

  public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop) {
    setAngle(desiredState);
    setSpeed(desiredState, isOpenLoop);
  }

  private void configAngleMotor() {
    m_angleMotor.restoreFactoryDefaults();
    absoluteAngleEncoder = m_angleMotor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
    m_angleController = m_angleMotor.getPIDController();
    //CANSparkMaxUtil.setCANSparkMaxBusUsage(m_angleMotor, Usage.kAll);
    m_angleMotor.setSmartCurrentLimit(SwerveDriveConstarts.Swerve.angleContinuousCurrentLimit);
    m_angleMotor.setInverted(SwerveDriveConstarts.Swerve.angleInvert);
    m_angleMotor.setIdleMode(SwerveDriveConstarts.Swerve.angleNeutralMode);
    absoluteAngleEncoder
        .setPositionConversionFactor(SwerveDriveConstarts.Swerve.angleConversionFactor);
    // since we're using an abolute encoder for motor angle, need to set that as the
    // input
    // to the onboard PID controller
    m_angleController.setFeedbackDevice(absoluteAngleEncoder);
    m_angleController.setP(SwerveDriveConstarts.Swerve.angleKP);
    m_angleController.setI(SwerveDriveConstarts.Swerve.angleKI);
    m_angleController.setD(SwerveDriveConstarts.Swerve.angleKD);
    m_angleController.setFF(SwerveDriveConstarts.Swerve.angleKFF);
    m_angleController.setPositionPIDWrappingEnabled(true);
    m_angleController.setPositionPIDWrappingMinInput(0);
    m_angleController.setPositionPIDWrappingMaxInput(360);
    m_angleMotor.enableVoltageCompensation(SwerveDriveConstarts.Swerve.voltageComp);
    m_angleController.setOutputRange(-1.0, 1.0);
    //m_angleMotor.burnFlash();
  }

  private void configDriveMotor() {
    m_driveMotor.restoreFactoryDefaults();
    driveEncoder = m_driveMotor.getEncoder();
    m_driveController = m_driveMotor.getPIDController();
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
    double angle = desiredState.angle.getDegrees();
    if(angle < 0) {
      angle += 360;
    }
    m_angleController.setReference(angle, ControlType.kPosition);
    SmartDashboard.putNumber("mod " + m_moduleNumber + " setAngle", angle);
  }

  public int getModuleNumber() {
    return m_moduleNumber;
  }

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(absoluteAngleEncoder.getPosition());
  }

  public double getRawAngle() {
    return absoluteAngleEncoder.getPosition();
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
