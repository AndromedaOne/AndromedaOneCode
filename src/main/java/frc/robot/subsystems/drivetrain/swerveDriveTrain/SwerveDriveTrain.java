package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.actuators.SwerveModule;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.telemetries.Trace;

/**
 * The swervedrive code is based on FRC3512 implementation. the repo for this is
 * located here: https://github.com/frc3512/SwerveBot-2022 there is also an
 * instructions guide for setting the constants, this guide is actually from
 * FRC364 and can be found here:
 * https://github.com/Team364/BaseFalconSwerve#setting-constants
 * 
 */
public class SwerveDriveTrain extends SubsystemBase implements SwerveDriveTrainBase {

  private Gyro4905 gyro;
  private SwerveDriveOdometry swerveOdometry;
  private SwerveModule[] mSwerveMods;
  private Field2d field;

  // this is used to publish the swervestates to NetworkTables so that they can be
  // used
  // in AdvantageScope to show the state of the swerve drive
  StructArrayPublisher<SwerveModuleState> m_publisher = NetworkTableInstance.getDefault()
      .getStructArrayTopic("MyStates", SwerveModuleState.struct).publish();

  public SwerveDriveTrain() {
    Trace.getInstance().logInfo("Construct SwerveDrive");
    gyro = Robot.getInstance().getSensorsContainer().getGyro();

    mSwerveMods = new SwerveModule[] {
        new SwerveModule(0, SwerveDriveConstarts.Swerve.Mod0.constants),
        new SwerveModule(1, SwerveDriveConstarts.Swerve.Mod1.constants),
        new SwerveModule(2, SwerveDriveConstarts.Swerve.Mod2.constants),
        new SwerveModule(3, SwerveDriveConstarts.Swerve.Mod3.constants) };

    SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[4];
    for (int i = 0; i < 4; ++i) {
      swerveModulePositions[i] = mSwerveMods[i].getPosition();
    }
    swerveOdometry = new SwerveDriveOdometry(SwerveDriveConstarts.Swerve.swerveKinematics, getYaw(),
        swerveModulePositions);
  }

  @Override
  public void move(Translation2d translation, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {

    ChassisSpeeds chassisSpeeds = fieldRelative
        ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
            getYaw())
        : new ChassisSpeeds(translation.getX(), translation.getY(), rotation);

    SwerveModuleState[] swerveModuleStates = SwerveDriveConstarts.Swerve.swerveKinematics
        .toSwerveModuleStates(chassisSpeeds);

    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates,
        SwerveDriveConstarts.Swerve.maxSpeed);
    for (SwerveModule mod : mSwerveMods) {
      if (true) {
        mod.setDesiredState(swerveModuleStates[mod.getModuleNumber()], isOpenLoop);
      }
    }
    SmartDashboard.putNumber("ChassisSpeeds X", chassisSpeeds.vxMetersPerSecond);
    SmartDashboard.putNumber("ChassisSpeeds Y", chassisSpeeds.vyMetersPerSecond);
    SmartDashboard.putNumber("ChassisSpeeds O", chassisSpeeds.omegaRadiansPerSecond);
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
      states[mod.getModuleNumber()] = mod.getState();
    }
    return states;
  }

  public SwerveModulePosition[] getPositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for (SwerveModule mod : mSwerveMods) {
      positions[mod.getModuleNumber()] = mod.getPosition();
    }
    return positions;
  }

  public Rotation2d getYaw() {
    return (SwerveDriveConstarts.Swerve.invertGyro)
        ? Rotation2d.fromDegrees(360 - gyro.getCompassHeading())
        : Rotation2d.fromDegrees(gyro.getCompassHeading());
  }

  @Override
  public void periodic() {

    // swerveOdometry.update(getYaw(), this.getPositions());
    // field.setRobotPose(getPose());

    // publish the states to NetworkTables for AdvantageScope
    m_publisher.set(getStates());

    for (SwerveModule mod : mSwerveMods) {
      SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " AngleMotor actual degrees",
          mod.getAngle().getDegrees());
      SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " AngleMotor raw degrees",
          mod.getRawAngle());
      SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " AngleMotor desired degrees",
          mod.getState().angle.getDegrees());
      SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " DriveMotor desired speed",
          mod.getState().speedMetersPerSecond);
      SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " Drive Motor input speed",
          mod.getDriveMotorCurrentSpeed());
    }
  }

  // @Override
  public SubsystemBase getSubsystemBase() {
    return (this);
  }

  // @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  // @Override
  public void init() {
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
    for (SwerveModule mod : mSwerveMods) {
      mod.setCoast(value);
    }
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    return null;
  }

}
