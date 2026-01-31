// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealAHI extends SubsystemBase implements AHIBase {

  private SparkMaxController m_armMotor;
  private SparkMaxController m_hopperMotor;
  // these are for safety reasons and represent the min/max angles able to be
  // safely reached
  // these would require the arm to have an absolute encoder (maybe?), which we
  // are assuming
  // make sure angle 0 can't be reached! this will make our lives much easier :D
  private double m_minArmAngle;
  private double m_maxArmAngle;
  // these may not require an absolute encoder, assuming the hopper starts in the
  // same position every time
  // are these even angles????? whatever
  // 99% chance these are actually in rotations... shouldnt be too bad tbh
  private double m_minHopperAngle;
  private double m_maxHopperAngle;
  private Config m_AHIConfig = Config4905.getConfig4905().getAHIConfig();

  public RealAHI() {
    // both of these are gonna need PID commands :D
    // basically, there will be one command which calls two methods
    // one method moves the arm, the other moves the hopper
    // these will happen simultaniously
    // if there are issues with the two mechanisms colliding, the method should
    // handle it
    // maybe the issue can be solved in the command itself? idk
    // arm will PID on angle, hopper will PID on angle but
    // effectively it will be distance
    // probably? not too sure how the hopper mechanism is supposed to work
    // the PID will be done in the command
    // assume the following:
    // arm - pos is down, neg is up -- hopper - pos is extend, neg is retract
    // arm - higher angles are down, lower angles are up
    // hopper - higher angles are extend, lower angles are retract
    m_armMotor = new SparkMaxController(m_AHIConfig, "intakeArmMotor", false, false);
    m_hopperMotor = new SparkMaxController(m_AHIConfig, "hopperExtensionMotor", false, false);
    m_minArmAngle = m_AHIConfig.getDouble("intakeArmMotor.minArmAngle");
    m_maxArmAngle = m_AHIConfig.getDouble("intakeArmMotor.maxArmAngle");
    m_minHopperAngle = m_AHIConfig.getDouble("hopperExtensionMotor.minHopperAngle");
    m_maxHopperAngle = m_AHIConfig.getDouble("hopperExtensionMotor.maxHopperAngle");
  }

  @Override
  public void rotateArm(double speed) {
    if ((speed > 0) && (getArmAngle() >= m_maxArmAngle)) {
      m_armMotor.setSpeed(0);
    } else if ((speed < 0) && (getArmAngle() <= m_minArmAngle)) {
      m_armMotor.setSpeed(0);
    } else {
      m_armMotor.setSpeed(speed);
    }
  }

  @Override
  public void moveHopper(double speed) {
    // IS THIS IN ROTATIONS OR ANGLES????
    if ((speed > 0) && (getHopperAngle() >= m_maxHopperAngle)) {
      m_hopperMotor.setSpeed(0);
    } else if ((speed < 0) && (getHopperAngle() <= m_minHopperAngle)) {
      m_hopperMotor.setSpeed(0);
    } else {
      m_hopperMotor.setSpeed(speed);
    }
  }

  @Override
  public double getArmAngle() {
    // may want to add an offset
    return m_armMotor.getAbsoluteEncoderPosition();
  }

  @Override
  public double getHopperAngle() {
    // may want to add an offset
    // again, is this an angle? do we want to say it's an angle?????
    return m_hopperMotor.getAbsoluteEncoderPosition();
  }

  @Override
  public void stop() {
    rotateArm(0);
    moveHopper(0);
  }

  @Override
  public void setBrakeMode() {
    m_armMotor.setBrakeMode();
    m_hopperMotor.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_armMotor.setCoastMode();
    m_hopperMotor.setCoastMode();
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

}
