// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;

/** Add your docs here. */
public class RealAHI extends SubsystemBase implements AHIBase {

  private SparkMaxController m_armMotor;
  // these are for safety reasons and represent the min/max angles able to be
  // safely reached
  // these would require the arm to have an absolute encoder (maybe?), which we
  // are assuming
  // arm does NOT have abs encoder BUT it has a limit switch!
  // the arm will go back until it hits the switch, where it will reset to a known
  // value.
  // look at topgun/showbot for code examples when we implement this.
  // make sure angle 0 can't be reached! this will make our lives much easier :D
  private double m_minArmAngle;
  private double m_maxArmAngle;
  private Config m_AHIConfig = Config4905.getConfig4905().getAHIConfig();
  private PIDController4905 m_armController = new PIDController4905("armControllerPID");

  private double m_KpA = 0.0;
  private double m_KiA = 0.0;
  private double m_KdA = 0.0;

  // gets set after the arm moves up
  private double m_offset = 0.0;

  // base offset will end up being the angle at the limit switch.
  // should probably be set in the config.
  private double m_baseOffset = 0.0;

  // ahi does not know what state it is in. uh. not sure if that's a problem.
  // okay now it does
  private State m_state = State.LIMITSWITCHSET;

  // dumb thing for testing with SmDb
  // gets set in the SmDb commands, then will be read by AHIDefault
  // once AHIdefault reads it, it will be set to false or smth idk
  private boolean m_goingToRetracted = false;
  private boolean m_goingToExtended = false;

  public enum State {
    EXTENDED, RETRACTED, EXTENDEDSTART, RETRACTEDSTART, LIMITSWITCHSET
  }

  public RealAHI() {
    // arm will PID on angle
    // assume the following:
    // arm - pos is down, neg is up
    // arm - higher angles are down, lower angles are up
    m_armMotor = new SparkMaxController(m_AHIConfig, "intakeArmMotor", false, false);
    m_minArmAngle = m_AHIConfig.getDouble("intakeArmMotor.minArmAngle");
    m_maxArmAngle = m_AHIConfig.getDouble("intakeArmMotor.maxArmAngle");

    m_KpA = m_AHIConfig.getDouble("kpa");
    m_KiA = m_AHIConfig.getDouble("kia");
    m_KdA = m_AHIConfig.getDouble("kda");
    m_armController.setPID(m_KpA, m_KiA, m_KdA);
  }

  // may want to consider making this private
  @Override
  public void rotateArm(double speed, boolean override) {
    // override allows you to disable the code stops if needbe
    if (!override) {
      if ((speed > 0) && (getArmAngle() >= m_maxArmAngle)) {
        m_armMotor.setSpeed(0);
      } else if ((speed < 0) && (getArmAngle() <= m_minArmAngle)) {
        m_armMotor.setSpeed(0);
      } else {
        m_armMotor.setSpeed(speed);
      }
    } else {
      m_armMotor.setSpeed(speed);
    }

  }

  public void rotateArm(double speed) {
    rotateArm(speed, false);
  }

  @Override
  public double getArmAngle() {
    // may want to add an offset
    return (m_armMotor.getBuiltInEncoderPositionTicks() * 360) + m_offset;
  }

  @Override
  public void stop() {
    rotateArm(0);
  }

  @Override
  public void setBrakeMode() {
    m_armMotor.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_armMotor.setCoastMode();
  }

  @Override
  public void periodic() {

  }

  // private to ensure this only gets run in periodic
  // yknow what? its probably fine, just ONLY RUN THIS IN DEFAULT COMMAND
  /**
   * ONLY RUN THIS IN AHI DEFAULT COMMAND!!!! I think??? it controls the PID so
   * please just. use rotate arm if you want to do silly things.
   */
  @Override
  public void rotateArmPID() {
    double pidCalc = m_armController.calculate(getArmAngle());
    rotateArm(pidCalc);
  }

  @Override
  public void setArmSetpoint(double setpoint) {
    m_armController.setSetpoint(setpoint);
  }

  @Override
  public void setState(State state) {
    m_state = state;
  }

  @Override
  public State getState() {
    return m_state;
  }

  @Override
  public boolean isLimitSwitchSet() {
    return m_armMotor.isReverseLimitSwitchOn();
  }

  /**
   * this should only ever be run ONCE. it is run ONLY in AHIdefaultcommand.
   */
  @Override
  public void setOffset() {
    m_offset = m_baseOffset - (m_armMotor.getBuiltInEncoderPositionTicks() * 360);
  }

  public boolean atSetpoint() {
    return m_armController.atSetpoint();
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
  public void setRetracting() {
    m_goingToRetracted = true;
  }

  @Override
  public void setExtending() {
    m_goingToExtended = true;
  }

  @Override
  public boolean isRetracting() {
    if (m_goingToRetracted) {
      m_goingToRetracted = false;
      return true;
    }
    return false;
  }

  @Override
  public boolean isExtending() {
    if (m_goingToExtended) {
      m_goingToExtended = false;
      return true;
    }
    return false;
  }

  public void setArmPID(double kP, double kI, double kD) {
    m_armController.setPID(kP, kI, kD);
  }

}
