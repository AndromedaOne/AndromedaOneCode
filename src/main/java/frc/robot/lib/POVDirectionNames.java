package frc.robot.lib;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public enum POVDirectionNames {

  // The POV is the D-Pad, in case you were wondering.

  NORTH(0), NORTHEAST(45), EAST(90), SOUTHEAST(135), SOUTH(180), SOUTHWEST(225), WEST(270), NORTHWEST(315);

  private int m_POVValue;

  private POVDirectionNames(int povDirection) {
    m_POVValue = povDirection;
  }

  public int getValue() {
    return m_POVValue;
  }

  public static boolean getPOVNorth(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == NORTH.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVNorthEast(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == NORTHEAST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVEast(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == EAST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVSouthEast(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == SOUTHEAST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVSouth(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == SOUTH.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVSouthWest(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == SOUTHWEST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVWest(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == WEST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVNorthWest(Joystick gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == NORTHWEST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVNorth(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == NORTH.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVNorthEast(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == NORTHEAST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVEast(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == EAST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVSouthEast(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == SOUTHEAST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVSouth(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == SOUTH.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVSouthWest(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == SOUTHWEST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVWest(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == WEST.getValue()) {
      return true;
    }
    return false;
  }

  public static boolean getPOVNorthWest(XboxController gamepad) {
    int povReading = gamepad.getPOV();

    if (povReading == NORTHWEST.getValue()) {
      return true;
    }
    return false;
  }

}