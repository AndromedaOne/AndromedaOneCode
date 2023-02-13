package frc.robot.subsystems.romiwings;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.ServoMotorContinuous;

public class RealRomiWings extends RomiWingsBase {
  private ServoMotorContinuous wingsServo;

  public RealRomiWings() {
    Config wingsConfig = Config4905.getConfig4905().getWingsConfig();
    wingsServo = new ServoMotorContinuous(wingsConfig, true);
  }

  @Override
  public void letWingsDown() {
    wingsServo.setSpeed(1.0);
  }

  @Override
  public void bringWingsUp() {
    wingsServo.setSpeed(-1.0);
    ;
  }

  @Override
  public void stop() {
    wingsServo.stop();

  }

}
