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
import frc.robot.subsystems.drivetrain.DriveTrainBase;
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
public class SwerveDriveTrain extends SubsystemBase implements DriveTrainBase {

  private Gyro4905 m_gyro;
  private SwerveDriveOdometry m_swerveOdometry;
  private SwerveModule[] m_SwerveMods;
  private Field2d m_field;
  private Config m_config;
  private ParkingBrakeStates m_ParkingBrakeState = ParkingBrakeStates.BRAKESOFF;
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

    m_SwerveMods = new SwerveModule[] { new SwerveModule(0), new SwerveModule(1),
        new SwerveModule(2), new SwerveModule(3) };

    SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[4];
    for (int i = 0; i < 4; ++i) {
      swerveModulePositions[i] = m_SwerveMods[i].getPosition();
    }
    m_swerveOdometry = new SwerveDriveOdometry(m_swerveKinematics, getYaw(), swerveModulePositions);
  }

  @Override
  public void move(double forwardBackward, double strafe, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {

    ChassisSpeeds chassisSpeeds = fieldRelative
        ? ChassisSpeeds.fromFieldRelativeSpeeds(forwardBackward, strafe, rotation, getYaw())
        : new ChassisSpeeds(forwardBackward, strafe, rotation);

    SwerveModuleState[] swerveModuleStates = m_swerveKinematics.toSwerveModuleStates(chassisSpeeds);

    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, m_config.getDouble("maxSpeed"));
    for (SwerveModule mod : m_SwerveMods) {
      mod.setDesiredState(swerveModuleStates[mod.getModuleNumber()], isOpenLoop, false);
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
    throw new RuntimeException("ERROR: " + getClass().getSimpleName() + " does not implement move");
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double heading) {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement moveUsingGyro");
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double strafe, double rotation,
      boolean fieldRelative, boolean isOpenLoop, double heading) {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement moveUsingGyro");
  }

  @Override
  public void stop() {
    throw new RuntimeException("ERROR: " + getClass().getSimpleName() + " does not implement stop");
  }

  @Override
  public void enableParkingBrakes() {
    m_ParkingBrakeState = ParkingBrakeStates.BRAKESON;
    setX();
    Trace.getInstance().logInfo("Parking Brakes Enabled");
  }

  @Override
  public void disableParkingBrakes() {
    m_ParkingBrakeState = ParkingBrakeStates.BRAKESOFF;
    setToZero();
    Trace.getInstance().logInfo("Parking Brakes Disabled");
  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    return m_ParkingBrakeState;
  }

  @Override
  public boolean hasParkingBrake() {
    return true;
  }

  @Override
  public double getRobotPositionInches() {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement getRobotPositionInches");
  }

  @Override
  public double getRobotVelocityInches() {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement getRobotVelocityInches");
  }

  @Override
  public void setCoast(boolean value) {
    for (SwerveModule mod : m_SwerveMods) {
      mod.setCoast(value);
    }
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement setDriveTrainMode");
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement getDriveTrainMode");
  }

  private void setX() {
    for (SwerveModule mod : m_SwerveMods) {
      int angle = -45;
      if ((mod.getModuleNumber() == 0) || (mod.getModuleNumber() == 3)) {
        angle = 45;
      }
      mod.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(angle)), false, true);
    }
  }

  private void setToZero() {
    for (SwerveModule mod : m_SwerveMods) {
      mod.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), false, true);
    }
  }

}
