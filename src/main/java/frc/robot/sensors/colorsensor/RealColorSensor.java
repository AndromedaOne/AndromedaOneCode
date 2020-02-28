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

  private Color red;
  private Color green;
  private Color blue;
  private Color yellow;

  private boolean isOnboard;
  private ColorMatch matcher;

  public RealColorSensor() {
    sensorConf = Config4905.getConfig4905().getSensorConfig();
    isOnboard = sensorConf.getBoolean("sensors.colorSensor.isOnboard");

    red = ColorMatch.makeColor(255, 0, 0);
    green = ColorMatch.makeColor(0, 255, 0);
    blue = ColorMatch.makeColor(0, 255, 255);
    yellow = ColorMatch.makeColor(255, 255, 0);

    if (isOnboard) {
      colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    } else {
      colorSensor = new ColorSensorV3(I2C.Port.kMXP);
    }

    matcher = new ColorMatch();

    matcher.addColorMatch(red);
    matcher.addColorMatch(blue);
    matcher.addColorMatch(green);
    matcher.addColorMatch(yellow);

  }

  @Override
  public Color getColor() {
    return colorSensor.getColor();
  }

}