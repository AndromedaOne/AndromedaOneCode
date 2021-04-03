// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivetrain;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkController;

public class RomiDriveTrain extends RealDriveTrain {
  private static final double METERSPERINCH = 0.0254;
  private final SparkController m_leftMotor;
  private final SparkController m_rightMotor;

  private final SpeedControllerGroup m_left;
  private final SpeedControllerGroup m_right;
  private Timer timer;
  private double m_previousLeftPositionMeters;
  private double m_previousRightPositionMeters;
  private double m_currentLeftVelocityMetersPerSecond;
  private double m_currentRightVelocityMetersPerSecond;
  private double m_previousTime;
  public final double m_ticksPerInch;
  private double numberOfTicksLeft;
  private double numberOfTicksRight;
  DescriptiveStatistics leftRollingAverage;
  DescriptiveStatistics rightRollingAverage;

  public RomiDriveTrain() {
    super();
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    m_leftMotor = new SparkController(drivetrainConfig, "left");
    m_rightMotor = new SparkController(drivetrainConfig, "right");

    m_left = new SpeedControllerGroup(m_leftMotor);
    m_right = new SpeedControllerGroup(m_rightMotor);

    double ticksPerRevolution = drivetrainConfig.getInt("ticksPerRevolution");
    double wheelDiameterInch = drivetrainConfig.getDouble("wheelDiameterInch");
    m_ticksPerInch = ticksPerRevolution / (wheelDiameterInch * Math.PI);
    m_leftMotor.getEncoder().setDistancePerPulse(1.0);
    m_rightMotor.getEncoder().setDistancePerPulse(1.0);
    m_previousLeftPositionMeters = 0;
    m_previousRightPositionMeters = 0;
    m_currentLeftVelocityMetersPerSecond = 0;
    m_currentRightVelocityMetersPerSecond = 0;
    m_previousTime = 0;
    timer = new Timer();
    leftRollingAverage = new DescriptiveStatistics(10);
    rightRollingAverage = new DescriptiveStatistics(10);
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub
    super.init();
    timer.start();
    m_previousLeftPositionMeters = 0;
    m_previousRightPositionMeters = 0;
    m_previousTime = 0;
    numberOfTicksLeft = m_leftMotor.getEncoderPositionTicks();
    numberOfTicksRight = m_rightMotor.getEncoderPositionTicks();
    //m_leftMotor.getEncoder().setDistancePerPulse(3.28 * METERSPERINCH / m_ticksPerInch) ;
    //m_rightMotor.getEncoder().setDistancePerPulse(3.28 * METERSPERINCH / m_ticksPerInch);
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block

    SmartDashboard.putNumber("Romi Speed", getRobotVelocityInches());

    double currentTime = Timer.getFPGATimestamp();
    double currentLeftPoseMeters = getLeftSideMeters();
    double currentRightPoseMeters = getRightsSideMeters();
    double deltaT = (currentTime - m_previousTime);
    SmartDashboard.putNumber("DeltaT", deltaT);

    leftRollingAverage.addValue((currentLeftPoseMeters - m_previousLeftPositionMeters) / deltaT);
    m_currentLeftVelocityMetersPerSecond = leftRollingAverage.getMean();
    rightRollingAverage.addValue((currentRightPoseMeters - m_previousRightPositionMeters) / deltaT);
    m_currentRightVelocityMetersPerSecond = rightRollingAverage.getMean();

    m_previousLeftPositionMeters = currentLeftPoseMeters;
    m_previousRightPositionMeters = currentRightPoseMeters;
    SmartDashboard.putNumber("Odometry Ratio",
        m_currentLeftVelocityMetersPerSecond / ((m_leftMotor.getEncoder().getRate() / m_ticksPerInch) * METERSPERINCH));
    m_previousTime = currentTime;

    double t = timer.get();
    double leftRate = (m_leftMotor.getEncoder().getRate() == 0.0) ? 0.0001 : (m_leftMotor.getEncoder().getRate());
    double currentLeftRatio = (m_leftMotor.getEncoderPositionTicks() - numberOfTicksLeft) / (leftRate * t);
    double rightRate = (m_rightMotor.getEncoder().getRate() == 0.0) ? 0.0001 : (m_rightMotor.getEncoder().getRate());
    double currentRightRatio = (m_rightMotor.getEncoderPositionTicks() - numberOfTicksRight) / (rightRate * t);
    SmartDashboard.putNumber("AAA Left Motor Ratio", currentLeftRatio);
    SmartDashboard.putNumber("AAA Right Motor Ratio", currentRightRatio);

    super.periodic();
    SmartDashboard.putNumber("AAA Distance Per Pulse", m_leftMotor.getEncoder().getDistancePerPulse());
    SmartDashboard.putNumber("AAA Ticks Per Inch", m_ticksPerInch);
    SmartDashboard.putNumber("AAA Left From Encoder", m_leftMotor.getEncoder().getRate());
    SmartDashboard.putNumber("AAA Left V", getLeftRateMetersPerSecond());
  }

  @Override
  protected SpeedControllerGroup getLeftSpeedControllerGroup() {
    return m_left;
  }

  @Override
  protected SpeedControllerGroup getRightSpeedControllerGroup() {
    return m_right;
  }

  @Override
  public double getRobotPositionInches() {
    double encoderPositionAvg = 0;
    int encoders = 0;
    if (m_leftMotor.hasEncoder()) {
      ++encoders;
      encoderPositionAvg += m_leftMotor.getEncoderPositionTicks();
      SmartDashboard.putNumber("left encoder", m_leftMotor.getEncoderPositionTicks());
    }
    if (m_rightMotor.hasEncoder()) {
      ++encoders;
      encoderPositionAvg += m_rightMotor.getEncoderPositionTicks();
      SmartDashboard.putNumber("right encoder", m_rightMotor.getEncoderPositionTicks());
    }
    if (encoders > 0) {
      encoderPositionAvg = encoderPositionAvg / (m_ticksPerInch * encoders);
    }
    return encoderPositionAvg;
  }

  @Override
  public double getLeftRateMetersPerSecond() {
    return m_leftMotor.getEncoder().getRate();
  }

  @Override
  public double getRightRateMetersPerSecond() {
    return m_rightMotor.getEncoder().getRate();
  }

  @Override
  public void resetEncoders() {
    // TODO Auto-generated method stub
    m_leftMotor.getEncoder().reset();
    m_rightMotor.getEncoder().reset();
  }

  @Override
  protected double getLeftSideMeters() {
    return (m_leftMotor.getEncoderPositionTicks() / m_ticksPerInch) * METERSPERINCH;
  }

  @Override
  protected double getRightsSideMeters() {
    return (m_rightMotor.getEncoderPositionTicks() / m_ticksPerInch) * METERSPERINCH;
  }

  public double getLeftRightAverageTicks() {
    return (m_leftMotor.getEncoderPositionTicks() + m_rightMotor.getEncoderPositionTicks()) / 2.0;
  }


  @Override
  public double getRobotVelocityInches() {
    double leftSpeed = m_leftMotor.getEncoder().getRate();
    double rightSpeed = m_rightMotor.getEncoder().getRate();
    return (rightSpeed + leftSpeed) / 2.0;
  }

}