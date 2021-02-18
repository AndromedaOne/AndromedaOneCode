package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.Config4905;

public class RealLEDs extends LEDs {
  public RealLEDs(String led) {

    Config conf = Config4905.getConfig4905().getLEDConfig().getConfig(led);

    red = new DigitalOutput(conf.getInt("Red"));
    red.enablePWM(0);
    green = new DigitalOutput(conf.getInt("Green"));
    green.enablePWM(0);
    blue = new DigitalOutput(conf.getInt("Blue"));
    blue.enablePWM(0);
    setPurple(1.0);

  }
}
