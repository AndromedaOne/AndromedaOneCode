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
    double deltaAngle = 0;
    if (initialAngle > finalAngle) {
      if ((initialAngle - finalAngle) > 180) {
        deltaAngle = (360 - initialAngle) + finalAngle;
      } else {
        deltaAngle = initialAngle - finalAngle;
      }
    } else {
      if ((finalAngle - initialAngle) > 180) {
        deltaAngle = (360 - finalAngle) + initialAngle;
      } else {
        deltaAngle = finalAngle - initialAngle;
      }
    }
    return deltaAngle > 90;
  }

}