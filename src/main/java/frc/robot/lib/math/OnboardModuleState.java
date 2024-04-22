package frc.robot.lib.math;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class OnboardModuleState {
  public static SwerveModuleState optimize(SwerveModuleState desiredState,
      Rotation2d currentAngle) {
    double targetAngle = placeInAppropriate0To360Scope(currentAngle.getDegrees(),
        desiredState.angle.getDegrees());
    double targetSpeed = desiredState.speedMetersPerSecond;
    double delta = targetAngle - currentAngle.getDegrees();
    if (Math.abs(delta) > 90) {
      targetSpeed = -targetSpeed;
      targetAngle = delta > 90 ? (targetAngle -= 180) : (targetAngle += 180);
    }
    return new SwerveModuleState(targetSpeed, Rotation2d.fromDegrees(targetAngle));
  }

  private static double placeInAppropriate0To360Scope(double scopeRefrence, double newAngle) {
    double lowerBound;
    double upperBound;
    double lowerOffset = scopeRefrence % 360;
    if (lowerOffset >= 0) {
      lowerBound = scopeRefrence - lowerOffset;
      upperBound = scopeRefrence + (360 - lowerOffset);
    } else {
      upperBound = scopeRefrence - lowerOffset;
      lowerBound = scopeRefrence - (360 + lowerOffset);
    }
    while (newAngle < lowerBound) {
      newAngle += 360;
    }
    while (newAngle > upperBound) {
      newAngle -= 360;
    }
    if (newAngle - scopeRefrence > 180) {
      newAngle -= 360;
    } else if (newAngle - scopeRefrence < -180) {
      newAngle += 360;
    }
    return newAngle;
  }

}
