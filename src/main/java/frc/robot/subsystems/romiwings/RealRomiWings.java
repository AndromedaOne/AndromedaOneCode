package frc.robot.subsystems.romiwings;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.ServoMotor;

public class RealRomiWings extends RomiWingsBase{
    private ServoMotor wingsServo;

    public RealRomiWings() {
        Config wingsConfig = Config4905.getConfig4905().getWingsConfig();
        wingsServo = new ServoMotor(wingsConfig.getInt("Port"));
    }

    @Override
    public void letWingsDown() {
        wingsServo.runForward();
    }

    @Override
    public void bringWingsUp() {
        wingsServo.runBackward();
    }

    @Override
    public void stop() {
        wingsServo.stop();

    }
    
}
