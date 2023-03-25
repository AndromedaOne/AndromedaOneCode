package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;

/**
 * Standard hobby style servo.
 *
 * <p>
 * The range parameters default to the appropriate values for the Hitec HS-322HD
 * servo provided in the FIRST Kit of Parts in 2008.
 */
public class ServoMotorPositional {
  private PWM m_servoMotor;
  private double m_maxServoAngle = 0;
  private double m_minServoAngle = 0;

  /**
   * Constructor.<br>
   *
   * <p>
   * By default {@value #kDefaultMaxServoPWM} ms is used as the maxPWM value<br>
   * By default {@value #kDefaultMinServoPWM} ms is used as the minPWM value<br>
   *
   * @param channel The PWM channel to which the servo is attached. 0-9 are
   *                on-board, 10-19 are on the MXP port
   */
  public ServoMotorPositional(Config motorConfig, String servoName) {
    // wasn't sure the best way to change this but needed to have a way to get
    // the port for a specified servo motor out of the configuration file
    m_servoMotor = new PWM(motorConfig.getInt(servoName + ".port"));
    HAL.report(tResourceType.kResourceType_Servo, m_servoMotor.getChannel() + 1);
  }

  public void setBounds(double maxPWM, double deadbandMax, double centerPWM, double deadbandMin,
      double minPWM) {
    m_servoMotor.setBounds(maxPWM, deadbandMax, centerPWM, deadbandMin, minPWM);
  }

  public void setPeriodMultiplier(PeriodMultiplier mult) {
    m_servoMotor.setPeriodMultiplier(mult);
  }

  public void setMinMaxServoAngle(double minAngle, double maxAngle) {
    m_minServoAngle = minAngle;
    m_maxServoAngle = maxAngle;
  }

  /**
   * Set the servo position.
   *
   * <p>
   * Servo values range from 0.0 to 1.0 corresponding to the range of full left to
   * full right.
   *
   * @param value Position from 0.0 to 1.0.
   */
  public void set(double value) {
    m_servoMotor.setPosition(value);
  }

  /**
   * Get the servo position.
   *
   * <p>
   * Servo values range from 0.0 to 1.0 corresponding to the range of full left to
   * full right. This returns the commanded position, not the position that the
   * servo is actually at, as the servo does not report its own position.
   *
   * @return Position from 0.0 to 1.0.
   */
  public double get() {
    return m_servoMotor.getPosition();
  }

  /**
   * Set the servo angle.
   *
   * <p>
   * Servo angles that are out of the supported range of the servo simply
   * "saturate" in that direction In other words, if the servo has a range of (X
   * degrees to Y degrees) than angles of less than X result in an angle of X
   * being set and angles of more than Y degrees result in an angle of Y being
   * set.
   *
   * @param degrees The angle in degrees to set the servo.
   */
  public void setAngle(double degrees) {
    if (degrees < m_minServoAngle) {
      degrees = m_minServoAngle;
    } else if (degrees > m_maxServoAngle) {
      degrees = m_maxServoAngle;
    }
    m_servoMotor.setPosition(((degrees - m_minServoAngle)) / getServoAngleRange());
  }

  /**
   * Get the servo angle.
   *
   * <p>
   * This returns the commanded angle, not the angle that the servo is actually
   * at, as the servo does not report its own angle.
   *
   * @return The angle in degrees to which the servo is set.
   */
  public double getAngle() {
    return m_servoMotor.getPosition() * getServoAngleRange() + m_minServoAngle;
  }

  private double getServoAngleRange() {
    return m_maxServoAngle - m_minServoAngle;
  }
}