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

  public static boolean isTurnToCompassHeadingGreaterThan90(double initialAngle,
      double finalAngle) {
    double deltaAngle = Math.abs(initialAngle - finalAngle);
    if (deltaAngle > 180) {
      deltaAngle = 360 - deltaAngle;
    }
    return deltaAngle > 90.1;
  }

  // this function will calculate the minimal difference between two
  // compass headings. for example, heading1 = 5, heading2 = 356, the minimal
  // difference is 9 degrees, not 351.
  public static double calculateMinimalCompassHeadingDifference(double heading1, double heading2) {
    assert ((heading1 >= 0.0) && (heading2 >= 0.0) && (heading1 < 360) && (heading2 < 360));
    double nonZeroCrossingDiff = heading1 - heading2;
    double zeroCrossingDiff = 360 - Math.abs(nonZeroCrossingDiff);
    if (Math.abs(nonZeroCrossingDiff) < zeroCrossingDiff) {
      return nonZeroCrossingDiff;
    } else {
      return (nonZeroCrossingDiff < 0) ? zeroCrossingDiff : -zeroCrossingDiff;
    }
  }

  public static double getReferenceAngle(double angle) {
    if (angle <= 90) {
      return angle;
    } else if (angle <= 180) {
      return 180 - angle;
    } else if (angle <= 270) {
      return angle - 180;
    } else {
      return 360 - angle;
    }
  }
}