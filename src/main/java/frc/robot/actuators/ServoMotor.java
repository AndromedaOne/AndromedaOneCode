package frc.robot.actuators;

import edu.wpi.first.wpilibj.Servo;

public class ServoMotor {

  private Servo m_servoMotor;

  public ServoMotor(int port, double maxPWMpulseWidthMS, double deadbandMax,
      double centerPWMpusleWithMS, double deadbandMin, double minPWMpulseWidthMS) {
    m_servoMotor = new Servo(port);
    m_servoMotor.setBounds(maxPWMpulseWidthMS, deadbandMax, centerPWMpusleWithMS, deadbandMin,
        minPWMpulseWidthMS);
  }

  public ServoMotor(int port) {
    m_servoMotor = new Servo(port);
  }

  public void runForward() {
    m_servoMotor.set(1.0);
  }

  public void runBackward() {
    m_servoMotor.set(1.0);
  }

  public void stop() {
    m_servoMotor.set(0);
  }

  public void setPosition(double position) {
    m_servoMotor.set(position);
  }
}