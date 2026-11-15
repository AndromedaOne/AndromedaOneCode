package frc.robot.utils;

import org.wpilib.math.geometry.*;
import org.wpilib.math.kinematics.ChassisVelocities;

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

  public static boolean epsilonEquals(ChassisVelocities s1, ChassisVelocities s2) {
    return epsilonEquals(s1.vx, s2.vx)
        && PathPlannerUtils.epsilonEquals(s1.vy, s2.vy)
        && PathPlannerUtils.epsilonEquals(s1.omega, s2.omega);
  }
}