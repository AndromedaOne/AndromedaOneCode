package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import com.typesafe.config.Config;

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
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.SwerveModule;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

/**
 * The swervedrive code is based on FRC3512 implementation. the repo for this is
 * located here: https://github.com/frc3512/SwerveBot-2022 there is also an
 * instructions guide for setting the constants, this guide is actually from
 * FRC364 and can be found here:
 * https://github.com/Team364/BaseFalconSwerve#setting-constants
 * 
 */
public class SwerveDriveTrain extends SubsystemBase implements SwerveDriveTrainBase {

  private Gyro4905 m_gyro;
  private SwerveDriveOdometry m_swerveOdometry;
  private SwerveModule[] m_SwerveMods;
  private Field2d m_field;
  private Config m_config; 
  public static SwerveDriveKinematics m_swerveKinematics;
  // this is used to publish the swervestates to NetworkTables so that they can be
  // used
  // in AdvantageScope to show the state of the swerve drive
  StructArrayPublisher<SwerveModuleState> m_publisher = NetworkTableInstance.getDefault()
      .getStructArrayTopic("/MyStates", SwerveModuleState.struct).publish();

  public SwerveDriveTrain() {
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
    .getConfig("SwerveDriveConstants");
    int wheelBase = m_config.getInt("wheelBase");
    int trackWidth = m_config.getInt("trackWidth");
    m_swerveKinematics = new SwerveDriveKinematics(
        new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));
   
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();

    m_SwerveMods = new SwerveModule[] {
        new SwerveModule(0, SwerveDriveConstarts.Swerve.Mod0.constants),
        new SwerveModule(1, SwerveDriveConstarts.Swerve.Mod1.constants),
        new SwerveModule(2, SwerveDriveConstarts.Swerve.Mod2.constants),
        new SwerveModule(3, SwerveDriveConstarts.Swerve.Mod3.constants) };

    SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[4];
    for (int i = 0; i < 4; ++i) {
      swerveModulePositions[i] = m_SwerveMods[i].getPosition();
    }
    m_swerveOdometry = new SwerveDriveOdometry(SwerveDriveConstarts.Swerve.swerveKinematics,
        getYaw(), swerveModulePositions);
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

    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, m_config.getDouble("maxSpeed"));
    for (SwerveModule mod : m_SwerveMods) {
      mod.setDesiredState(swerveModuleStates[mod.getModuleNumber()], isOpenLoop);
    }
    SmartDashboard.putNumber("ChassisSpeeds X", chassisSpeeds.vxMetersPerSecond);
    SmartDashboard.putNumber("ChassisSpeeds Y", chassisSpeeds.vyMetersPerSecond);
    SmartDashboard.putNumber("ChassisSpeeds O", chassisSpeeds.omegaRadiansPerSecond);
  }

  public Pose2d getPose() {
    return m_swerveOdometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    m_swerveOdometry.resetPosition(getYaw(), this.getPositions(), pose);
  }

  public SwerveModuleState[] getStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (SwerveModule mod : m_SwerveMods) {
      states[mod.getModuleNumber()] = mod.getState();
    }
    return states;
  }

  public SwerveModulePosition[] getPositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for (SwerveModule mod : m_SwerveMods) {
      positions[mod.getModuleNumber()] = mod.getPosition();
    }
    return positions;
  }

  public Rotation2d getYaw() {
    return (m_config.getBoolean("invertGyro")
        ? Rotation2d.fromDegrees(360 - m_gyro.getCompassHeading())
        : Rotation2d.fromDegrees(m_gyro.getCompassHeading()));
  }

  @Override
  public void periodic() {

    // swerveOdometry.update(getYaw(), this.getPositions());
    // field.setRobotPose(getPose());

    // publish the states to NetworkTables for AdvantageScope
    m_publisher.set(getStates());
    for (SwerveModule mod : m_SwerveMods) {
      SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " Actual angle", mod.getRawAngle());
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
    return m_swerveOdometry;
  }

  public void setSwerveOdometry(SwerveDriveOdometry swerveOdometry) {
    this.m_swerveOdometry = swerveOdometry;
  }

  public SwerveModule[] getSwerveMods() {
    return m_SwerveMods;
  }

  public void setSwerveMods(SwerveModule[] swerveMods) {
    this.m_SwerveMods = swerveMods;
  }

  public Field2d getField() {
    return m_field;
  }

  public void setField(Field2d field) {
    this.m_field = field;
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
    for (SwerveModule mod : m_SwerveMods) {
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
