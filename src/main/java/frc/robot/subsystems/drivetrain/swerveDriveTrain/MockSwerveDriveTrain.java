package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SwerveModule;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public class MockSwerveDriveTrain implements SwerveDriveTrainBase {

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
  public SubsystemBase getSubsystemBase() {
    return null;
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void init() {
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
  }

  @Override
  public void move(double fowardBackSpeed, double rotateAmount, boolean squaredInput) {
  }

  @Override
  public void move(Translation2d translation, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double heading) {
  }

  @Override
  public void moveUsingGyro(Translation2d translations, double rotation, boolean fieldRelative,
      boolean isOpenLoop, double heading) {

  }

  @Override
  public void stop() {
  }

  @Override
  public void enableParkingBrakes() {
  }

  @Override
  public void disableParkingBrakes() {
  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    return null;
  }

  @Override
  public boolean hasParkingBrake() {
    return false;
  }

  @Override
  public double getRobotPositionInches() {
    return 0;
  }

  @Override
  public double getRobotVelocityInches() {
    return 0;
  }

  @Override
  public void setCoast(boolean value) {
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    return null;
  };

}