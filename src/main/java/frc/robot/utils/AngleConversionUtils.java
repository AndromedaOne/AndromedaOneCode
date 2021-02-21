package frc.robot.utils;

public class AngleConversionUtils {
  public enum Direction {
    CW, CCW
  }

  public static double ConvertAngleToCompassHeading(double angle) {
    double correctedAngle = angle % 360;
    if (correctedAngle < 0) {
      correctedAngle += 360;
    }
    return correctedAngle;
  }

  public static boolean isTurnToCompassHeadingGreaterThan90(double initialAngle, double finalAngle) {
    double deltaAngle = Math.abs(initialAngle - finalAngle);
    if (deltaAngle > 180) {
      deltaAngle = 360 - deltaAngle;
    }
    return deltaAngle > 90;
  }

}