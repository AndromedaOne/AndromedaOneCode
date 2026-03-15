// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class RealAHI extends SubsystemBase implements AHIBase {

  private SparkMaxController m_armMotor;
  // these are for safety reasons and represent the min/max angles able to be
  // safely reached
  // these would require the arm to have an absolute encoder (maybe?), which we
  // are assuming
  // ahi has absolute encoder woo hoo
  // make sure angle 0 can't be reached! this will make our lives much easier :D
  // btw, although the encoder is measured in rotations (0-1), we only use
  // degrees for calculations. make sure to always multiply by 360 when calling
  // get absolute encoder position, or just use the get arm angle method.
  private double m_minArmAngle;
  private double m_maxArmAngle;
  private Config m_AHIConfig = Config4905.getConfig4905().getAHIConfig();
  private PIDController4905 m_armController = new PIDController4905("armControllerPID");

  private double m_KpA = 0.0;
  private double m_KiA = 0.0;
  private double m_KdA = 0.0;

  // gets set in the config if you want
  private double m_offset = 0.0;

  // ahi does not know what state it is in. uh. not sure if that's a problem.
  // okay now it does
  private State m_state = State.RETRACTEDSTART;

  // dumb thing for testing with SmDb
  // gets set in the SmDb commands, then will be read by AHIDefault
  // once AHIdefault reads it, it will be set to false or smth idk
  private boolean m_goingToRetracted = false;
  private boolean m_goingToExtended = false;

  // table for smart dashboard
  private String m_tableName = "ahi/";

  private ArmFeedForward m_feedForward;

  public enum State {
    EXTENDED, RETRACTED, EXTENDEDSTART, RETRACTEDSTART, HOLDEXTENDED
  }

  private class ArmFeedForward implements FeedForward {

    private double m_kG = 0.04;

    @Override
    public double calculate() {
      return (m_kG * Math.cos(Math.toRadians(getArmAngle())));
    }
  }

  public RealAHI() {
    // arm will PID on angle
    // arm angle will be lower in extended, higher in retracted
    // pos is up, neg is down
    m_offset = m_AHIConfig.getDouble("offset");
    m_armMotor = new SparkMaxController(m_AHIConfig, "intakeArmMotor", false, false);
    m_minArmAngle = m_AHIConfig.getDouble("intakeArmMotor.minArmAngle") - m_offset;
    m_maxArmAngle = m_AHIConfig.getDouble("intakeArmMotor.maxArmAngle") - m_offset;

    m_feedForward = new ArmFeedForward();

    m_KpA = m_AHIConfig.getDouble("kpa");
    m_KiA = m_AHIConfig.getDouble("kia");
    m_KdA = m_AHIConfig.getDouble("kda");
    m_armController.setPID(m_KpA, m_KiA, m_KdA);
    // do we want this????
    m_armController.disableContinuousInput();
    m_armController.setTolerance(3);
    m_armController.setFeedforward(m_feedForward);
    m_armController.setMinAndMaxOutput(m_AHIConfig.getDouble("intakeArmMotor.minOutput"),
        m_AHIConfig.getDouble("intakeArmMotor.maxOutput"));
  }

  // may want to consider making this private
  @Override
  public void rotateArm(double speed, boolean override) {
    // override allows you to disable the code stops if need be
    // SHOULD be correct, with inc angle = pos velocity
    // and dec angle = neg velocity
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
    // NOTE: at this point in time (11:21 sunday), the abs encoder is 0
    // at the extended position, and 0.3/108 at the retracted position.
    // no offset is currently applied, but do note the abs encoder 0 position
    // is planned to be changed in the near future to prevent wrap around.
    double angle = (m_armMotor.getAbsoluteEncoderPosition() * 360) - m_offset;
    if (angle < 0) {
      angle += 360;
    }
    return angle;
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
    SmartDashboard.putNumber(m_tableName + "arm angle", getArmAngle());
    SmartDashboard.putNumber(m_tableName + "raw encoder value",
        m_armMotor.getAbsoluteEncoderPosition());
    SmartDashboard.putNumber(m_tableName + "raw encoder value in degrees",
        m_armMotor.getAbsoluteEncoderPosition() * 360);
    SmartDashboard.putNumber(m_tableName + "arm PID setpoint", m_armController.getSetpoint());
    SmartDashboard.putBoolean(m_tableName + "arm PID is at setpoint", atSetpoint());
    SmartDashboard.putString(m_tableName + "AHI state", getState().toString());
    SmartDashboard.putNumber(m_tableName + "arm velocity", m_armMotor.getSpeed());
    SmartDashboard.putNumber(m_tableName + "P value", m_KpA);
    SmartDashboard.putNumber(m_tableName + "I value", m_KiA);
    SmartDashboard.putNumber(m_tableName + "D value", m_KdA);
    SmartDashboard.putNumber(m_tableName + "min angle", m_minArmAngle);
    SmartDashboard.putNumber(m_tableName + "max angle", m_maxArmAngle);
    SmartDashboard.putNumber(m_tableName + "arm offset", m_offset);
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
    Trace.getInstance().logInfo("setting arm state to: " + m_state.toString());
  }

  @Override
  public State getState() {
    return m_state;
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

  @Override
  public void setArmPID(double kP, double kI, double kD) {
    m_armController.setPID(kP, kI, kD);
    m_KpA = kP;
    m_KiA = kI;
    m_KdA = kD;
  }

}
