package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.Config4905;

public class RealLEDs extends LEDs {
  public RealLEDs(String led) {

    System.out.println("Trying to make this LED:" + led);
    Config conf = Config4905.getConfig4905().getLEDConfig().getConfig(led);

    m_red = new DigitalOutput(conf.getInt("Red"));
    m_red.enablePWM(0);
    m_green = new DigitalOutput(conf.getInt("Green"));
    m_green.enablePWM(0);
    m_blue = new DigitalOutput(conf.getInt("Blue"));
    m_blue.enablePWM(0);
    setPurple(1.0);

    m_readyForPeriodic = true;
  }
}
