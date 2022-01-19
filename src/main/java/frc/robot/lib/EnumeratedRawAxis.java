package frc.robot.lib;

import edu.wpi.first.wpilibj.Joystick;

public enum EnumeratedRawAxis {
  LEFTSTICKHORIZONTAL(0), LEFTSTICKVERTICAL(1), LEFTTRIGGER(2), RIGHTTRIGGER(3),
  RIGHTSTICKHORIZONTAL(4), RIGHTSTICKVERTICAL(5);

  private int m_rawAxisValue;

  private EnumeratedRawAxis(int value) {
    m_rawAxisValue = value;
  }

  public int getValue() {
    return m_rawAxisValue;
  }

  public double getRawAxis(Joystick gamepad) {
    return gamepad.getRawAxis(getValue());

  }

  public boolean pressedBeyond(Joystick gamepad, double threshold) {
    return getRawAxis(gamepad) > threshold;
  }
}