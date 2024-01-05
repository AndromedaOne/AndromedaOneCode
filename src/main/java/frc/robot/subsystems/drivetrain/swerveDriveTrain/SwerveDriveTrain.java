package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SwerveModule;
import frc.robot.lib.config.Constants;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public class SwerveDriveTrain extends SubsystemBase implements SwerveDriveTrainBase {

  private final Pigeon2 gyro;
  private SwerveDriveOdometry swerveOdometry;
  private SwerveModule[] mSwerveMods;
  private Field2d field;

  public SwerveDriveTrain() {
    gyro = new Pigeon2(Constants.Swerve.pigeonID);
    gyro.configFactoryDefault();
    zeroGyro();

    mSwerveMods = new SwerveModule[] { new SwerveModule(0, Constants.Swerve.Mod0.constants),
        new SwerveModule(1, Constants.Swerve.Mod1.constants),
        new SwerveModule(2, Constants.Swerve.Mod2.constants),
        new SwerveModule(3, Constants.Swerve.Mod3.constants) };

    SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[4];
    for(int i = 0; i < 4; ++i) {
      swerveModulePositions[i] = mSwerveMods[i].getPosition();
    }
    swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), swerveModulePositions);

    field = new Field2d();
    SmartDashboard.putData("Field", field);
  }

  public void drive(Translation2d translation, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
    SwerveModuleState[] swerveModuleStates = Constants.Swerve.swerveKinematics
        .toSwerveModuleStates(fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(),
                rotation, getYaw())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation));
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
    }
  }

// Used by SwerveControllerCommand in Auto
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);

    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(desiredStates[mod.moduleNumber], false);
    }

  }

  public Pose2d getPose() {
    return swerveOdometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    swerveOdometry.resetPosition(getYaw(), this.getPositions(), pose);
  }

  public SwerveModuleState[] getStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (SwerveModule mod : mSwerveMods) {
      states[mod.moduleNumber] = mod.getState();
    }
    return states;
  }

  public SwerveModulePosition[] getPositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for (SwerveModule mod : mSwerveMods) {
      positions[mod.moduleNumber] = mod.getPosition();
    }
    return positions;
  }

  public void zeroGyro() {
    gyro.setYaw(0);
  }

  public Rotation2d getYaw() {
    return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw())
        : Rotation2d.fromDegrees(gyro.getYaw());
  }

  @Override
  public void periodic() {

    // swerveOdometry.update(getYaw(), getStates)));

    swerveOdometry.update(getYaw(), this.getPositions());
    field.setRobotPose(getPose());

    for (SwerveModule mod : mSwerveMods) {
      // SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder",
      //     mod.getCanCoder().getDegrees());
      SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated",
          mod.getState().angle.getDegrees());
      SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity",
          mod.getState().speedMetersPerSecond);
    }
  }

  // @Override
  public SubsystemBase getSubsystemBase() {
    return (this);
  }

  // @Override
  public void setDefaultCommand(CommandBase command) {
    super.setDefaultCommand(command);
  }

  // @Override
  public void init() {
  }

  public Pigeon2 getGyro() {
    return gyro;
  }

  public SwerveDriveOdometry getSwerveOdometry() {
    return swerveOdometry;
  }

  public void setSwerveOdometry(SwerveDriveOdometry swerveOdometry) {
    this.swerveOdometry = swerveOdometry;
  }

  public SwerveModule[] getmSwerveMods() {
    return mSwerveMods;
  }

  public void setmSwerveMods(SwerveModule[] mSwerveMods) {
    this.mSwerveMods = mSwerveMods;
  }

  public Field2d getField() {
    return field;
  }

  public void setField(Field2d field) {
    this.field = field;
  }

  @Override
  public void move(double fowardBackSpeed, double rotateAmount, boolean squaredInput) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void move(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs, double heading) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void moveUsingGyro(Translation2d translations, double rotation, boolean fieldRelative, boolean isOpenLoop,
      double heading) {
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
  }

}
