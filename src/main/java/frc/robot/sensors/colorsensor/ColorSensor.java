package frc.robot.sensors.colorsensor;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.util.Color;

public abstract class ColorSensor {
  protected static final Color red = ColorMatch.makeColor(0.61, 0.30, 0.08);
  protected static final Color green = ColorMatch.makeColor(0.30, 0.53, 0.18);
  protected static final Color blue = ColorMatch.makeColor(0.26, 0.48, 0.26);
  protected static final Color yellow = ColorMatch.makeColor(0.44, 0.47, 0.07);

  public abstract ColorMatchResult getColor();

  public abstract Color getRawColor();

  public static String colormatchtostring(ColorMatchResult result, Color rawColor) {
    String matchedColorString = "???";
    
    if (result.color.equals(red)) {
      matchedColorString =  "RED";
    } else if (result.color.equals(green)) {
      matchedColorString = "GREEN";
    } else if (result.color.equals(blue)) {
      matchedColorString = "BLUE";
    } else if (result.color.equals(yellow)) {
      matchedColorString = "YELLOW";
    } 

    String rawString = String.format ("%.3f %.3f %.3f", rawColor.red, rawColor.green, rawColor.blue);
    return String.format("%s    %s",matchedColorString, rawString);
  }
}