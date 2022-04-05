package frc.robot.actuators;

import edu.wpi.first.wpilibj.Servo;

public class ServoMotor {

  private Servo m_servoMotor;

  public ServoMotor(int port, double maxPWMpulseWidthMS, double centerPWMpusleWithMS,
      double minPWMpulseWidthMS) {
    m_servoMotor = new Servo(port);
    m_servoMotor.setBounds(maxPWMpulseWidthMS, m_servoMotor.getRawBounds().deadbandMax,
        centerPWMpusleWithMS, m_servoMotor.getRawBounds().deadbandMin, minPWMpulseWidthMS);
  }

  public ServoMotor(int port) {
    /*
     * values from motor data sheet
     * https://docs.revrobotics.com/15mm/actuators/servos/smart-robot-servo
     */
    this(port, 2.5, 1.5, 0.5);
  }

  public void runForward() {
    m_servoMotor.setSpeed(1);
  }

  public void runBackward() {
    m_servoMotor.setSpeed(-1);
  }

  public void stop() {
    m_servoMotor.setSpeed(0);
  }

  public void setPosition(double position) {
    m_servoMotor.setSpeed(position);
  }
}