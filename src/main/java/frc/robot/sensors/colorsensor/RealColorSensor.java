package frc.robot.sensors.colorsensor;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Config4905;

public class RealColorSensor extends ColorSensor {
  private Config sensorConf;

  public ColorSensorV3 colorSensor;

  private boolean isOnboard;
  private ColorMatch matcher;

  public RealColorSensor() {
    sensorConf = Config4905.getConfig4905().getSensorConfig();
    isOnboard = sensorConf.getBoolean("sensors.colorSensor.isOnboard");

    if (isOnboard) {
      colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    } else {
      colorSensor = new ColorSensorV3(I2C.Port.kMXP);
    }

    matcher = new ColorMatch();

    matcher.addColorMatch(red);
    matcher.addColorMatch(green);
    matcher.addColorMatch(blue);
    matcher.addColorMatch(yellow);

  }

  @Override
  public ColorMatchResult getColor() {
    // Color c = colorSensor.getColor();
    // System.out.println(String.format("%2f %2f %2f",c.red,c.green, c.blue));
    return matcher.matchClosestColor(getRawColor());
  }

  @Override
  public Color getRawColor() {
    // TODO Auto-generated method stub
    return colorSensor.getColor();
  }

}