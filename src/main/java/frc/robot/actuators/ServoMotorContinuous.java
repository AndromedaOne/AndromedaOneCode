package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;

/**
 * Standard hobby style servo that rotates continuously, ie you cannot set this
 * servo to a specific angle/position. currently this supports the servo motors
 * on the Romi (harvestor...)
 */
public class ServoMotorContinuous {

  private PWM m_servoMotor;
  boolean m_invertDirection = false;

  public ServoMotorContinuous(Config motorConfig, boolean useDefaultDeadband) {
    m_servoMotor = new PWM(motorConfig.getInt("port"));
    m_servoMotor.setBounds(motorConfig.getDouble("maxPWMpulseWidthMS"),
        useDefaultDeadband ? m_servoMotor.getRawBounds().deadbandMax
            : motorConfig.getDouble("deadbandMax"),
        motorConfig.getDouble("centerPWMpusleWithMS"),
        useDefaultDeadband ? m_servoMotor.getRawBounds().deadbandMax
            : motorConfig.getDouble("deadbandMin"),
        motorConfig.getDouble("minPWMpulseWidthMS"));
    if (motorConfig.hasPath("invertDirection")) {
      m_invertDirection = true;
    }
  }

  // speed is a value between -1 and 1
  public void setSpeed(double speed) {
    if (m_invertDirection) {
      speed = -speed;
    }
    m_servoMotor.setSpeed(speed);
  }

  public void stop() {
    m_servoMotor.setSpeed(0);
  }

  // this can be used to slow down the PWM signal for older devices. see
  // PWM::setPeriodMultiplier
  public void setPeriodMultiplier(PeriodMultiplier mult) {
    m_servoMotor.setPeriodMultiplier(mult);
  }
}