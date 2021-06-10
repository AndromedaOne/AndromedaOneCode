package frc.robot.subsystems.romishooter;

import frc.robot.Config4905;
import frc.robot.actuators.ServoMotor;

public class RealRomiShooter extends RomiShooterBase {

  private ServoMotor m_motor;

  public RealRomiShooter() {
    int port = Config4905.getConfig4905().getRomiShooterConfig().getInt("port");
    System.out.println("Port for Romi shooter: " + port);
    m_motor = new ServoMotor(port);

    m_motor.setPosition(0);
  }

  @Override
  public void setSpeed(double speed) {
    m_motor.setPosition(speed);

  }

}
