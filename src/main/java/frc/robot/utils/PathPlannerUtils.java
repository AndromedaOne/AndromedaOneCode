package frc.robot.utils;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class PathPlannerUtils {
  private static final double kEpsilon = 1E-8;

  public static Twist3d multiplyTwist(Twist3d twist, double factor) {
    return new Twist3d(twist.dx * factor, twist.dy * factor, twist.dz * factor, twist.rx * factor,
        twist.ry * factor, twist.rz * factor);
  }

  public static boolean epsilonEquals(double a, double b, double epsilon) {
    return (a - epsilon <= b) && (a + epsilon >= b);
  }

  public static boolean epsilonEquals(double a, double b) {
    return epsilonEquals(a, b, kEpsilon);
  }

  public static boolean epsilonEquals(ChassisSpeeds s1, ChassisSpeeds s2) {
    return epsilonEquals(s1.vxMetersPerSecond, s2.vxMetersPerSecond)
        && PathPlannerUtils.epsilonEquals(s1.vyMetersPerSecond, s2.vyMetersPerSecond)
        && PathPlannerUtils.epsilonEquals(s1.omegaRadiansPerSecond, s2.omegaRadiansPerSecond);
  }
}