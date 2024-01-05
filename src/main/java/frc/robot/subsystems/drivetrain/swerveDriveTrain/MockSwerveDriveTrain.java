package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SwerveModule;
import frc.robot.sensors.gyro.Gyro4905;

public class MockSwerveDriveTrain implements SwerveDriveTrainBase {

  @Override
  public void drive(Translation2d translation, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
  }

  @Override
  public void setModuleStates(SwerveModuleState[] desiredStates) {
  }

  @Override
  public Pose2d getPose() {
    return null;
  }

  @Override
  public void resetOdometry(Pose2d pose) {
  }

  @Override
  public SwerveModuleState[] getStates() {
    return null;
  }

  @Override
  public SwerveModulePosition[] getPositions() {
    return null;
  }

  @Override
  public Rotation2d getYaw() {
    return null;
  }

  @Override
  public void periodic() {
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return null;
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
  }

  @Override
  public void init() {
  }

  @Override
  public Gyro4905 getGyro() {
    return null;
  }

  @Override
  public SwerveDriveOdometry getSwerveOdometry() {
    return null;
  }

  @Override
  public void setSwerveOdometry(SwerveDriveOdometry swerveOdometry) {
  }

  @Override
  public SwerveModule[] getmSwerveMods() {
    return null;
  }

  @Override
  public void setmSwerveMods(SwerveModule[] mSwerveMods) {
  };

  @Override
  public Field2d getField() {
    return null;
  };

  @Override
  public void setField(Field2d field) {
  };

}