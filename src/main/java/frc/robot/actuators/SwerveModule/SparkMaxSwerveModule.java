package frc.robot.actuators.SwerveModule;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.ControlType;
import com.typesafe.config.Config;

import org.wpilib.math.controller.SimpleMotorFeedforward;
import org.wpilib.math.kinematics.SwerveModuleVelocity;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class SparkMaxSwerveModule extends SwerveModuleBase {
  private SparkMaxController m_angleMotor;
  private SparkMaxController m_driveMotor;
  private SparkClosedLoopController m_angleController;
  private SparkClosedLoopController m_driveController;
  private double m_lastAngle = 0;
  private Config m_config;
  private double m_driveMotorPositionOffset = 0;

  // needs to be attached to drive motor
  private RelativeEncoder m_driveEncoder;
  // needs to be attached to angle motor
  private AbsoluteEncoder m_absoluteAngleEncoder;

  private SimpleMotorFeedforward m_feedForward;

  public SparkMaxSwerveModule(int moduleNumber) {
    super(moduleNumber);
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");
    m_feedForward = new SimpleMotorFeedforward(m_config.getDouble("driveKS"),
        m_config.getDouble("driveKV"), m_config.getDouble("driveKA"));
    /* Angle Motor Config */
    m_angleMotor = new SparkMaxController(m_config, "Mod" + getModuleNumber() + ".angleMotorID",
        true, false);

    /* drive motor config */
    m_driveMotor = new SparkMaxController(m_config, "Mod" + getModuleNumber() + ".driveMotorID",
        true, true);

    m_driveMotorPositionOffset = m_driveMotor.getBuiltInEncoderPositionTicks();
  }

  protected void setSpeed(SwerveModuleVelocity desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.velocity / m_config.getDouble("maxSpeed");
      m_driveMotor.getMotorController().setThrottle(percentOutput);
    } else {
      m_driveController.setSetpoint(desiredState.velocity, ControlType.kVelocity,
          ClosedLoopSlot.kSlot0, m_feedForward.calculate(desiredState.velocity));
    }
  }

  protected void setAngle(SwerveModuleVelocity desiredState, boolean overRide) {
    double angle = desiredState.angle.getDegrees();
    if (!overRide
        && Math.abs(desiredState.velocity) <= (m_config.getDouble("maxSpeed") * 0.01)) {
      angle = m_lastAngle;
    }
    m_lastAngle = angle;
    // need to invert the angle because inverting the motor through the motor
    // controller causes issues with the onboard pidcontroller
    if (angle < 0) {
      angle += 360;
    }
    m_angleController.setSetpoint(angle, ControlType.kPosition);
  }

  @Override
  protected double getAngleMotorRawAngle() {
    double angle = m_absoluteAngleEncoder.getPosition().get();
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  public void setCoast(boolean value) {
    if (value) {
      m_angleMotor.setCoastMode();
      m_driveMotor.setCoastMode();
    } else {
      m_angleMotor.setBrakeMode();
      m_driveMotor.setBrakeMode();
    }
  }

  public void disableAccelerationLimiting() {
    m_driveMotor.disableAccelerationLimiting();
  }

  public void enableAccelerationLimiting() {
    m_driveMotor.enableAccelerationLimiting(m_config.getDouble("drivekRampRate"));
  }

  @Override
  public double getDriveEncoderPosition() {
    return m_driveEncoder.getPosition().get() - m_driveMotorPositionOffset;
  }

  @Override
  public double getDriveEncoderVelocity() {
    return m_driveEncoder.getVelocity().get();
  }

}
