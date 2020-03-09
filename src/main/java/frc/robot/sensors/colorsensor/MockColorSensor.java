package frc.robot.sensors.colorsensor;

import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.util.Color;

public class MockColorSensor extends ColorSensor {

  public MockColorSensor() {

  }

  @Override
  public ColorMatchResult getColor() {
    // Color is not actually being used on the color wheel
    return new ColorMatchResult(Color.kDarkGray, 20);
  }

  @Override
  public Color getRawColor() {
    // TODO Auto-generated method stub
    return Color.kDarkGray;
  }
}