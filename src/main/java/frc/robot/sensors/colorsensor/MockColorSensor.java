package frc.robot.sensors.colorsensor;

import com.revrobotics.ColorMatchResult;

public class MockColorSensor extends ColorSensor {

  public MockColorSensor() {

  }

  @Override
  public ColorMatchResult getColor() {
    return null;
  }
}