package frc.robot.lib;

public enum POVDirectionNames {

  // The POV is the D-Pad, in case you were wondering.

  NORTH(0), NORTHEAST(45), EAST(90), SOUTHEAST(135), SOUTH(180), SOUTHWEST(225), WEST(270),
  NORTHWEST(315);

  private int m_POVValue;

  private POVDirectionNames(int povDirection) {
    m_POVValue = povDirection;
  }

  public int getValue() {
    return m_POVValue;
  }
}