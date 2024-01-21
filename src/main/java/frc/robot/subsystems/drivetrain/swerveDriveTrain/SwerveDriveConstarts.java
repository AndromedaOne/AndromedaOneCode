package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.robot.actuators.SwerveModuleConstants;

/**
 * to facilitate a faster implementation, we're going to use a constants class
 * for SwerveDrive eventually this will be refactored to use our Config4905
 * framework
 * 
 */
public class SwerveDriveConstarts {

  public static final class Swerve {

    public static final boolean invertGyro = true; // Always ensure Gyro is CCW+ CW-

    // trackWidth: Center to Center distance of left and right modules in meters.
    public static final double trackWidth = Units.inchesToMeters(24);
    // wheelBase: Center to Center distance of front and rear module wheels in
    // meters.
    public static final double wheelBase = Units.inchesToMeters(24);
    // wheelDiameter: Diameter of the wheel (including tread) in meters.
    public static final double wheelDiameter = Units.inchesToMeters(4.0);
    // wheelCircumference: Cirumference of the wheel (including tread) in meters.
    public static final double wheelCircumference = wheelDiameter * Math.PI;

    public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(
        new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

    public static final double openLoopRamp = 0.25;

    // driveGearRatio: Total gear ratio for the drive motor.
    public static final double driveGearRatio = (6.75 / 1.0);
    // angleGearRatio: Total gear ratio for the angle motor.
    // The encoder is being driven directly by the wheel, so the ratio is 1.
    public static final double angleGearRatio = (1.0);

    /* Swerve Voltage Compensation */
    public static final double voltageComp = 12.0;

    /* Swerve Current Limiting */
    public static final int angleContinuousCurrentLimit = 20;
    public static final int driveContinuousCurrentLimit = 80;

    /* Angle Motor PID Values */
    public static final double angleKP = 0.05;
    public static final double angleKI = 0.0;
    public static final double angleKD = 0.0;
    public static final double angleKFF = 0.0;

    /* Drive Motor PID Values */
    public static final double driveKP = 0.1;
    public static final double driveKI = 0.0;
    public static final double driveKD = 0.0;
    public static final double driveKFF = 0.0;

    /* Drive Motor Characterization Values */
    public static final double driveKS = 0.667;
    public static final double driveKV = 2.44;
    public static final double driveKA = 0.27;

    /* Drive Motor Conversion Factors */
    public static final double driveConversionPositionFactor = (wheelDiameter * Math.PI)
        / driveGearRatio;
    public static final double driveConversionVelocityFactor = driveConversionPositionFactor / 60.0;
    public static final double angleConversionFactor = 360.0 / angleGearRatio;

    /* Swerve Profiling Values */
    public static final double maxSpeed = 4.5; // meters per second
    public static final double maxAngularVelocity = 11.5;

    /* Neutral Modes */
    public static final IdleMode angleNeutralMode = IdleMode.kBrake;
    public static final IdleMode driveNeutralMode = IdleMode.kBrake;

    /* Motor Inverts */
    public static final boolean driveInvert = false;
    public static final boolean angleInvert = true;

    /* Module Specific Constants */
    /* Front Left Module - Module 0 */
    public static final class Mod0 {
      public static final int driveMotorID = 8;
      public static final int angleMotorID = 7;
      public static final SwerveModuleConstants constants = new SwerveModuleConstants(driveMotorID,
          angleMotorID);
    }

    /* Front Right Module - Module 1 */
    public static final class Mod1 {
      public static final int driveMotorID = 4;
      public static final int angleMotorID = 3;
      public static final SwerveModuleConstants constants = new SwerveModuleConstants(driveMotorID,
          angleMotorID);
    }

    /* Back Left Module - Module 2 */
    public static final class Mod2 {
      public static final int driveMotorID = 5;
      public static final int angleMotorID = 6;
      public static final SwerveModuleConstants constants = new SwerveModuleConstants(driveMotorID,
          angleMotorID);
    }

    /* Back Right Module - Module 3 */
    public static final class Mod3 {
      public static final int driveMotorID = 1;
      public static final int angleMotorID = 2;
      public static final SwerveModuleConstants constants = new SwerveModuleConstants(driveMotorID,
          angleMotorID);
    }
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
  }

}