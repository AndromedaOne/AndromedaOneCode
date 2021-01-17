package frc.robot.utils;

public class AngleConversionUtils {
  public static double ConvertAngleToCompassHeading(double angle) {
    double correctedAngle = angle % 360;
    if (correctedAngle < 0) {
      correctedAngle += 360;
    }
    return correctedAngle;
  }
}
