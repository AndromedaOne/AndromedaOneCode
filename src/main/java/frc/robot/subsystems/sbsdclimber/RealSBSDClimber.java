// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdclimber;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.HitecHS322HDpositionalServoMotor;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealSBSDClimber extends SubsystemBase implements SBSDClimberBase {
  private SparkMaxController m_climberWinchMotor;
  private HitecHS322HDpositionalServoMotor m_climberServoMotor;
  private Config m_climberConfig;
  private double m_climbSpeed = 0.0;
  private double m_reverseClimbSpeed = 0.0;
  private double m_unlatchedServoMotorAngle = 0.0;
  private double m_servoMotorInitialAngle = 0.0;
  private double m_climberOffset = 0.0;

  public RealSBSDClimber() {
    m_climberConfig = Config4905.getConfig4905().getSBSDClimberConfig();
    m_climberWinchMotor = new SparkMaxController(m_climberConfig, "winchMotor", false, false);
    m_climberServoMotor = new HitecHS322HDpositionalServoMotor(m_climberConfig, "servoMotor");
    m_climbSpeed = m_climberConfig.getDouble("climbSpeed");
    m_reverseClimbSpeed = m_climberConfig.getDouble("reverseClimbSpeed");
    m_unlatchedServoMotorAngle = m_climberConfig.getDouble("unlatchedServoMotorAngle");
    m_servoMotorInitialAngle = m_climberConfig.getDouble("servoMotorInitialAngle");
    m_climberOffset = m_climberWinchMotor.getBuiltInEncoderPositionTicks();
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void climb() {
    m_climberWinchMotor.setSpeed(m_climbSpeed);
  }

  @Override
  public void setServoInitialPosition() {
    m_climberServoMotor.setAngle(m_servoMotorInitialAngle);
  }

  @Override
  public void reverseClimb() {
    m_climberWinchMotor.setSpeed(m_reverseClimbSpeed);
  }

  @Override
  public void stop() {
    m_climberWinchMotor.setSpeed(0);
  }

  @Override
  public void unlatchTrident() {
    m_climberServoMotor.setAngle(m_unlatchedServoMotorAngle);
  }

  private double getCurrentClimberRotation(){
    return m_climberWinchMotor.getBuiltInEncoderPositionTicks() - m_climberOffset;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Climber Rotation: ", getCurrentClimberRotation());
  }
}
