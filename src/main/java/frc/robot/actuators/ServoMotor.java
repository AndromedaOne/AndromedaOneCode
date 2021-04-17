package frc.robot.actuators;

import edu.wpi.first.wpilibj.Servo;

public class ServoMotor {

  private Servo m_servoMotor;

  public ServoMotor(int port) {
    m_servoMotor = new Servo(port);
  }

  public void runForward() {
    m_servoMotor.set(1.0);
  }

  public void runBackward() {
    m_servoMotor.set(0.0);
  }

  public void stop() {
    m_servoMotor.set(0.5);
  }

}