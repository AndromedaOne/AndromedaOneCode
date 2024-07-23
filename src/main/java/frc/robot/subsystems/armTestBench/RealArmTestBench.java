// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBench;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/**
 * 0 angle is poining straight down
 */
public class RealArmTestBench extends SubsystemBase implements ArmTestBenchBase {
  private final SparkMaxController m_motor;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;

  public RealArmTestBench() {
    Config armrotateConfig = Config4905.getConfig4905().getArmTestBenchConfig();
    m_motor = new SparkMaxController(armrotateConfig, "motor");
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
  public void rotate(double speed) {
    // Positive value for speed akes the angle go down and the arm rotate up
    if ((speed > 0) && (getAngle() <= m_minAngle)) {
      m_motor.setSpeed(0);
    } else if ((speed < 0) && (getAngle() >= m_maxAngle)) {
      m_motor.setSpeed(0);
    } else {
      m_motor.setSpeed(speed);
    }
  }

  @Override
  public void stop() {
    m_motor.setSpeed(0);
  }

  @Override
  public double getAngle() {
    return 360 - (m_motor.getAbsoluteEncoderPosition() * 360);
  }

  @Override
  public void enableMotorBrake() {
    m_motor.setBrakeMode();
  }

  @Override
  public void disableMotorBrake() {
    m_motor.setCoastMode();
  }

  @Override
  public void periodic(){
    SmartDashboard.putNumber("Arm test Bench Angle", getAngle());
  }
}
