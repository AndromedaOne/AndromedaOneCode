package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
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
  public void setDefaultCommand(CommandBase command) {
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
    // TODO Auto-generated method stub

  }

  @Override
  public void move(Translation2d translation, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
    // TODO Auto-generated method stub

  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double heading) {
    // TODO Auto-generated method stub

  }

  @Override
  public void moveUsingGyro(Translation2d translations, double rotation, boolean fieldRelative,
      boolean isOpenLoop, double heading) {
    // TODO Auto-generated method stub

  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

  @Override
  public void enableParkingBrakes() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disableParkingBrakes() {
    // TODO Auto-generated method stub

  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasParkingBrake() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public double getRobotPositionInches() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getRobotVelocityInches() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setCoast(boolean value) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
    // TODO Auto-generated method stub

  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    // TODO Auto-generated method stub
    return null;
  };

}