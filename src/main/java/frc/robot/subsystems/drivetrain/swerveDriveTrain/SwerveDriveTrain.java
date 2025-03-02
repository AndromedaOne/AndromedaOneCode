package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import static edu.wpi.first.math.util.Units.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.DriveFeedforwards;
import com.typesafe.config.Config;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.SwerveModule.KrakenAndSparkMaxSwerveModule;
import frc.robot.actuators.SwerveModule.SparkMaxSwerveModule;
import frc.robot.actuators.SwerveModule.SwerveModuleBase;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.DriveTrainMode;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;
import frc.robot.utils.AngleConversionUtils;
import frc.robot.utils.PoseEstimation4905;
import frc.robot.utils.PoseEstimation4905.RegionsForPose;

/**
 * The swervedrive code is based on FRC3512 implementation. the repo for this is
 * located here: https://github.com/frc3512/SwerveBot-2022 there is also an
 * instructions guide for setting the constants, this guide is actually from
 * FRC364 and can be found here:
 * https://github.com/Team364/BaseFalconSwerve#setting-constants
 * 
 */
public class SwerveDriveTrain extends SubsystemBase implements DriveTrainBase {
  private Pose2d m_currentPose;
  private Boolean needToReset = true;
  private Gyro4905 m_gyro;
  private PoseEstimation4905 m_poseEstimation;
  private SwerveModuleBase[] m_SwerveMods;
  private Field2d m_field;
  private Config m_config;
  private ParkingBrakeStates m_ParkingBrakeState = ParkingBrakeStates.BRAKESOFF;
  public static SwerveDriveKinematics m_swerveKinematics;
  private ChassisSpeeds m_currentChassisSpeeds;
  private SwerveSetpoint m_prevSetpoint;
  private DriveTrainMode m_driveTrainMode = new DriveTrainMode();
  private static SwerveSetpointGenerator m_generator;
  private double m_modSpeed = 0;
  private double m_modDistance = 0;
  private double m_robotAngle = 0;
  private boolean m_isInsideUnsafeZone = false;
  private int m_count = 0;
  private double m_highestAccel = 0;
  private PoseEstimation4905.RegionsForPose m_region = RegionsForPose.UNKNOWN;
  private boolean m_isLeftSide = false;

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

    if (m_config.getBoolean("useKraken")) {
      m_SwerveMods = new KrakenAndSparkMaxSwerveModule[] { new KrakenAndSparkMaxSwerveModule(0),
          new KrakenAndSparkMaxSwerveModule(1), new KrakenAndSparkMaxSwerveModule(2),
          new KrakenAndSparkMaxSwerveModule(3) };
    } else {
      m_SwerveMods = new SparkMaxSwerveModule[] { new SparkMaxSwerveModule(0),
          new SparkMaxSwerveModule(1), new SparkMaxSwerveModule(2), new SparkMaxSwerveModule(3) };
    }

    SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[4];
    for (int i = 0; i < 4; ++i) {
      swerveModulePositions[i] = m_SwerveMods[i].getPosition();
    }
    m_poseEstimation = new PoseEstimation4905(m_swerveKinematics, swerveModulePositions);
    m_currentChassisSpeeds = m_swerveKinematics.toChassisSpeeds(getStates());
  }

  @Override
  public void configurePathPlanner() {
    if (m_config.getBoolean("usePathPlanning")) {
      PPHolonomicDriveController m_pathFollowingConfig = new PPHolonomicDriveController(
          new PIDConstants(m_config.getDouble("pathplanning.translationConstants.p"),
              m_config.getDouble("pathplanning.translationConstants.i"),
              m_config.getDouble("pathplanning.translationConstants.d")),
          new PIDConstants(m_config.getDouble("pathplanning.rotationConstants.p"),
              m_config.getDouble("pathplanning.rotationConstants.i"),
              m_config.getDouble("pathplanning.rotationConstants.d")));
      // Load the RobotConfig from the GUI settings. You should probably
      // store this in your Constants file
      DCMotor dcMotor = new DCMotor(0, 0, 0, 0, 0, 0);
      ModuleConfig modConfig = new ModuleConfig(0.0, 0.0, 0.0, dcMotor, 0.0, 0.0, 0);
      RobotConfig robotConfig = new RobotConfig(0.0, 0.0, modConfig, 0.0);
      try {
        robotConfig = RobotConfig.fromGUISettings();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);

      }
      m_generator = new SwerveSetpointGenerator(robotConfig,
          m_config.getDouble("maxAngularVelocity"));
      m_prevSetpoint = new SwerveSetpoint(m_currentChassisSpeeds, getStates(),
          DriveFeedforwards.zeros(4));
      // the numbers for the holonomic path are extremely inaccurate.
      AutoBuilder.configure(this::getPoseForPathPlanner, this::resetOdometry,
          this::getCurrentSpeeds, (speeds) -> driveRobotRelativeBetter(speeds),
          m_pathFollowingConfig, robotConfig, () -> false, getSubsystemBase());
    }
  }

  @Override
  public void move(double forwardBackward, double strafe, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
    if (m_ParkingBrakeState == ParkingBrakeStates.BRAKESON) {
      forwardBackward = 0;
      strafe = 0;
      rotation = 0;
    }
    Translation2d translation2d = new Translation2d(forwardBackward, strafe)
        .times(m_config.getDouble("maxSpeed"));
    ChassisSpeeds chassisSpeeds = fieldRelative

        ? ChassisSpeeds.fromFieldRelativeSpeeds(translation2d.getX(), translation2d.getY(),
            rotation, getYaw())
        : new ChassisSpeeds(translation2d.getX(), translation2d.getY(), rotation);

    SwerveModuleState[] swerveModuleStates = m_swerveKinematics.toSwerveModuleStates(chassisSpeeds);

    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, m_config.getDouble("maxSpeed"));
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.setDesiredState(swerveModuleStates[mod.getModuleNumber()], isOpenLoop, false);
    }
  }

  public void driveRobotRelativeBetter(ChassisSpeeds speeds) {
    // Note: it is important to not discretize speeds before or after
    // using the setpoint generator, as it will discretize them for you
    m_prevSetpoint = m_generator.generateSetpoint(m_prevSetpoint, // The previous setpoint
        speeds, // The desired target speeds
        0.02 // The loop time of the robot code, in seconds
    );
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.setDesiredState(m_prevSetpoint.moduleStates()[mod.getModuleNumber()], true, false);
    }
  }

  public Pose2d getPose() {
    return m_poseEstimation.getPose();
  }

  public Pose2d getPoseForPathPlanner() {
    Pose2d pathPlannerPose = m_poseEstimation.getPose();
    // commented out because we thought it'd fix the rotation error of pathplanner
    // but it didn't
    /*
     * double correctedAngle = pathPlannerPose.getRotation().getDegrees(); if
     * (correctedAngle < 0) { correctedAngle += 360; } Pose2d correctedPose = new
     * Pose2d(m_poseEstimation.getPose().getTranslation(),
     * Rotation2d.fromDegrees(correctedAngle));
     */
    return pathPlannerPose;
  }

  public ChassisSpeeds getCurrentSpeeds() {
    ChassisSpeeds speeds = new ChassisSpeeds(m_currentChassisSpeeds.vxMetersPerSecond,
        m_currentChassisSpeeds.vyMetersPerSecond, m_currentChassisSpeeds.omegaRadiansPerSecond);
    return speeds;
  }

  public boolean resetOdometry(Pose2d pose) {
    System.out.println("Resetting Odometry, compass heading " + m_gyro.getCompassHeading());
    return m_poseEstimation.resetPosition(this.getPositions(), pose);
  }

  public SwerveModuleState[] getStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (SwerveModuleBase mod : m_SwerveMods) {
      states[mod.getModuleNumber()] = mod.getState();
    }
    return states;
  }

  @Override
  public boolean isAtAngle(double angle) {
    boolean returnValue = true;
    for (SwerveModuleBase mod : m_SwerveMods) {
      double modAngle = mod.getAngle().getDegrees();
      double reverseModAngle = 180 + modAngle;
      if (reverseModAngle > 360) {
        reverseModAngle -= 360;
      }
      if (!((Math.abs(modAngle - angle) < 1) || (Math.abs(modAngle - angle) > 359)
          || (Math.abs(reverseModAngle - angle) < 1)
          || (Math.abs(reverseModAngle - angle) > 359))) {
        returnValue = false;
      }
    }
    return returnValue;
  }

  public SwerveModulePosition[] getPositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    for (SwerveModuleBase mod : m_SwerveMods) {
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
    SmartDashboard.putNumber("robotDistance", getRobotPositionInches());
    m_currentChassisSpeeds = m_swerveKinematics.toChassisSpeeds(getStates());
    SmartDashboard.putNumber("Odometry Input Heading", -1 * m_gyro.getCompassHeading());
    if (m_count == 25) {
      double currentPosition = m_SwerveMods[0].getPosition().distanceMeters;
      double currentVelocity = (currentPosition - m_modDistance) * 2;
      SmartDashboard.putNumber("Mod Distance ", m_modDistance);
      SmartDashboard.putNumber("Current Position ", currentPosition);
      SmartDashboard.putNumber("Robot Velocity   ", currentVelocity);
      if (m_highestAccel < Math.abs(currentVelocity - m_modSpeed) * 2) {
        m_highestAccel = Math.abs(currentVelocity - m_modSpeed) * 2;
      }
      SmartDashboard.putNumber("Robot Acceleration ", (currentVelocity - m_modSpeed) * 2);
      m_modSpeed = currentVelocity;
      m_modDistance = currentPosition;
      m_count = 0;
      SmartDashboard.putNumber("Max Acceleration ", m_highestAccel);
    }
    m_count++;
    if (needToReset) {
      if (resetOdometry(getPose())) {
        needToReset = false;
      }
    } else {
      m_currentPose = m_poseEstimation.update(getPositions());
      m_region = m_poseEstimation.getRegion();
      m_isLeftSide = m_poseEstimation.isLeftSide();
      m_isInsideUnsafeZone = m_poseEstimation.getInUnsafeZone();
      SmartDashboard.putBoolean("Is inside of unsafe zone ", m_isInsideUnsafeZone);
      SmartDashboard.putNumber("Pose X ", metersToInches(m_currentPose.getX()));
      SmartDashboard.putNumber("Pose Y ", metersToInches(m_currentPose.getY()));
      SmartDashboard.putNumber("Pose angle ", m_currentPose.getRotation().getDegrees());
      SmartDashboard.putString("PoseRegion", m_region.toString());
      SmartDashboard.putBoolean("Is Left Side ", m_isLeftSide);
      double currentAngle = m_currentPose.getRotation().getDegrees();
      double currentAngularVelocity = (currentAngle - m_robotAngle) * 2;
      SmartDashboard.putNumber("Robot Angular Velocity", currentAngularVelocity);
      m_robotAngle = currentAngle;
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

  public PoseEstimation4905 getSwerveOdometry() {
    return m_poseEstimation;
  }

  public void setSwerveOdometry(PoseEstimation4905 swerveOdometry) {
    this.m_poseEstimation = swerveOdometry;
  }

  public SwerveModuleBase[] getSwerveMods() {
    return m_SwerveMods;
  }

  public void setSwerveMods(SwerveModuleBase[] swerveMods) {
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
    move(fowardBackSpeed, 0, -rotateAmount, false, true);
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double compassHeading) {
    if (rotation == 0.0) {
      double robotDeltaAngle = AngleConversionUtils
          .calculateMinimalCompassHeadingDifference(m_gyro.getCompassHeading(), compassHeading);
      rotation = robotDeltaAngle
          * Config4905.getConfig4905().getCommandConstantsConfig().getDouble("moveUsingGyroP");
      Trace.getInstance().addTrace(true, "MoveUsingGyro",
          new TracePair("CompassHeading", compassHeading),
          new TracePair("GyroCompassHeading", m_gyro.getCompassHeading()),
          new TracePair("robotDeltaAngle", robotDeltaAngle), new TracePair("rotation", rotation),
          new TracePair("ForwardBackward", forwardBackward));
    }
    move(forwardBackward, 0, rotation, false, true);
  }

  /**
   * The angle passed in is counter clockwise positive
   */
  public void moveUsingGyroStrafe(double forwardBackward, double angle, boolean useSquaredInputs,
      double compassHeading) {
    double angleInRadians = Math.toRadians(angle);
    double forwardBackwardValue = forwardBackward * Math.cos(angleInRadians);
    double strafeValue = forwardBackward * Math.sin(angleInRadians);
    // this is where you want to put debugging for forwardBackward, angleInRadians,
    // forwardBackwardValue, strafeValue, m_SwerveMods[0].getAngle().getDegrees()
    move(forwardBackwardValue, strafeValue, 0.0, false, true);
  }

  @Override
  public void stop() {
    move(0, 0, 0, true, true);
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
    double modDistance = m_SwerveMods[0].getPosition().distanceMeters;
    if ((m_SwerveMods[0].getAngle().getDegrees() < 270)
        && (m_SwerveMods[0].getAngle().getDegrees() > 90)) {
      modDistance = -modDistance;
    }

    return modDistance * 39.3701;
  }

  @Override
  public double getRobotPositionInchesBasedOnAngle(double angle) {
    double modDistance = m_SwerveMods[0].getPosition().distanceMeters;
    double calcAngle = m_SwerveMods[0].getAngle().getDegrees();
    if ((Math.abs(angle - calcAngle) > 170) && (Math.abs(angle - calcAngle) < 350)) {
      modDistance = -modDistance;
    }
    return modDistance * 39.3701;
  }

  @Override
  public double getRobotVelocityInches() {
    throw new RuntimeException(
        "ERROR: " + getClass().getSimpleName() + " does not implement getRobotVelocityInches");
  }

  @Override
  public void setCoast(boolean value) {
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.setCoast(value);
    }
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
    m_driveTrainMode.setDriveTrainMode(mode);
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    return m_driveTrainMode.getDriveTrainMode();
  }

  private void setX() {
    for (SwerveModuleBase mod : m_SwerveMods) {
      int angle = -45;
      if ((mod.getModuleNumber() == 0) || (mod.getModuleNumber() == 3)) {
        angle = 45;
      }
      mod.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(angle)), true, true);
    }
  }

  public void setToAngle(double angle) {
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(angle)), true, true);
    }
  }

  public void setToZero() {
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)), true, true);
    }
  }

  @Override
  public void setVelocityToZero() {
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.setDesiredState(new SwerveModuleState(0, mod.getAngle()), true, true);
    }
  }

  public void disableAccelerationLimiting() {
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.disableAccelerationLimiting();
    }
  }

  public void enableAccelerationLimiting() {
    for (SwerveModuleBase mod : m_SwerveMods) {
      mod.enableAccelerationLimiting();
    }
  }

  public PoseEstimation4905.RegionsForPose getRegion() {
    return m_region;
  }

  @Override
  public boolean isLeftSide() {
    return m_isLeftSide;
  }

  @Override
  public boolean isUnsafeZone() {
    return m_isInsideUnsafeZone;
  }

  @Override
  public Pose2d currentPose2d() {
    return m_currentPose;
  }

}
