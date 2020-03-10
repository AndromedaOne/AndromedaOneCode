package frc.robot.sensors.colorsensor;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.util.Color;

public abstract class ColorSensor {
  protected static Color red = ColorMatch.makeColor(1, 0, 0);
  protected static Color green = ColorMatch.makeColor(0, 1, 0);
  protected static Color blue = ColorMatch.makeColor(0, 0, 1);
  protected static Color yellow = ColorMatch.makeColor(1, 1, 0);

  public abstract ColorMatchResult getColor();

  public abstract Color getRawColor();

  public static Color makeColorFromConfig(Config config) {
    double red = config.getDouble("r");
    double green = config.getDouble("g");
    double blue = config.getDouble("b");
    return ColorMatch.makeColor(red, green, blue);
  }

  public static String colormatchtostring(ColorMatchResult result, Color rawColor) {
    String matchedColorString = "???";

    if (result.color.equals(red)) {
      matchedColorString = "RED";
    } else if (result.color.equals(green)) {
      matchedColorString = "GREEN";
    } else if (result.color.equals(blue)) {
      matchedColorString = "BLUE";
    } else if (result.color.equals(yellow)) {
      matchedColorString = "YELLOW";
    }

    String rawString = String.format("%.3f %.3f %.3f", rawColor.red, rawColor.green, rawColor.blue);
    return String.format("%s    %s", matchedColorString, rawString);
  }
}