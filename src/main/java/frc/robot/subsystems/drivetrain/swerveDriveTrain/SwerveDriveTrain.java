package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.function.DoubleSupplier;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.DriveFeedforwards;
import com.typesafe.config.Config;

import edu.wpi.first.math.filter.LinearFilter;
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
import frc.robot.actuators.SwerveModule.KrakenSwerveModule;
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
import frc.robot.utils.FieldConstants;
import frc.robot.utils.PoseEstimation4905;

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
  public Pose2d m_oldPose;
  public Instant m_oldPoseTime;
  public double m_velocityToHub;
  private boolean m_needToReset = true;
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
  private boolean m_isInsideUnsafeZone = false;
  private int m_count = 0;
  private double m_highestAccel = 0;
  private boolean m_isLeftSide = false;
  private boolean m_changingMidMode = false;
  private FieldConstants m_fieldConstants;
  private double m_robotFieldElementPoseOffset;
  private LinearFilter m_linearFilter;

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

    if (m_config.getBoolean("useKrakenAndSpark") && m_config.getBoolean("useKrakenOnly")) {
      throw new IllegalArgumentException(
          "Cannot use both KrakenAndSpark and KrakenOnly swerve modules at the same time");
    }

    if (m_config.getBoolean("useKrakenAndSpark")) {
      m_SwerveMods = new KrakenAndSparkMaxSwerveModule[] { new KrakenAndSparkMaxSwerveModule(0),
          new KrakenAndSparkMaxSwerveModule(1), new KrakenAndSparkMaxSwerveModule(2),
          new KrakenAndSparkMaxSwerveModule(3) };
    } else if (m_config.getBoolean("useKrakenOnly")) {
      // use kraken swerve modules
      m_SwerveMods = new KrakenSwerveModule[] { new KrakenSwerveModule(0),
          new KrakenSwerveModule(1), new KrakenSwerveModule(2), new KrakenSwerveModule(3) };

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
    m_fieldConstants = new FieldConstants();
    m_robotFieldElementPoseOffset = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getDouble("robotToFieldElementAngleOffset");
    m_oldPose = new Pose2d();
    m_oldPoseTime = Instant.now();
    m_linearFilter = LinearFilter.movingAverage(10);
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
      if (m_config.getBoolean("pathplanning.usePathGUI")) {
        try {
          robotConfig = RobotConfig.fromGUISettings();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      } else {
        try {
          robotConfig = getFromConfig();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
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

  public double getVelocityToHub(Pose2d pose, Instant time) {
    double velocity = 0;
    double oldDistance = getHubDistanceToRobotInInches(m_oldPose);
    double currentDistance = getHubDistanceToRobotInInches(pose);
    velocity = ((currentDistance - oldDistance) * 1000.0
        / (Duration.between(m_oldPoseTime, time).toMillis()));
    String name = "DriveTrain/";
    SmartDashboard.putNumber(name + "velocity to hub", velocity);
    SmartDashboard.putNumber(name + "delta distance from hub", currentDistance - oldDistance);
    SmartDashboard.putNumber(name + "delta time in seconds",
        (Duration.between(m_oldPoseTime, time).toMillis()) / 1000.0);
    return velocity;
  }

  public DoubleSupplier getVelocityToHubDoubleSupplier() {
    return () -> m_velocityToHub;
  }

  @Override
  public void periodic() {
    // publish the states to NetworkTables for AdvantageScope
    m_publisher.set(getStates());
    m_currentChassisSpeeds = m_swerveKinematics.toChassisSpeeds(getStates());
    if (m_count == 25) {
      double currentPosition = m_SwerveMods[0].getPosition().distanceMeters;
      double currentVelocity = (currentPosition - m_modDistance) * 2;
      if (m_highestAccel < Math.abs(currentVelocity - m_modSpeed) * 2) {
        m_highestAccel = Math.abs(currentVelocity - m_modSpeed) * 2;
      }
      m_modSpeed = currentVelocity;
      m_modDistance = currentPosition;
      m_count = 0;
    }
    m_count++;
    if (m_needToReset) {
      if (resetOdometry(getPose())) {
        m_needToReset = false;
      }
    } else {
      m_currentPose = m_poseEstimation.update(getPositions());
      String name = "DriveTrain/";
      SmartDashboard.putNumber(name + "Distance To Hub", getHubDistanceToRobotInInches());
      SmartDashboard.putNumber(name + "Distance to shuttle spot",
          getShuttleDistanceToRobotInInches());
      Instant newPoseTime = Instant.now();
      m_velocityToHub = m_linearFilter.calculate(getVelocityToHub(m_currentPose, newPoseTime));
      SmartDashboard.putNumber(name + "filtered velocity to hub", m_velocityToHub);
      m_oldPose = m_currentPose;
      m_oldPoseTime = newPoseTime;
    }
  }

  // @Override
  public SubsystemBase getSubsystemBase() {
    return this;
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
  public void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput) {
    move(forwardBackSpeed, 0, -rotateAmount, false, true);
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
   * The angle passed in is counter clockwise positive compassheading does
   * absolulely nothing why is it here
   */
  public void moveUsingGyroStrafe(double forwardBackward, double angle, boolean useSquaredInputs) {
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

  public void setMidModeValueBoolean() {
    m_changingMidMode = true;
  }

  public boolean getMidModeValueBoolean() {
    if (m_changingMidMode) {
      m_changingMidMode = false;
      return true;
    }
    return false;
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

  @Override
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

  /*
   * This method sets the swerve modules in the same heading (90 degress) but
   * different angles to mitigate robot shift when the wheels turn to face the
   * direction that it is going to drive to approach the tower (2026 game
   * Rebuilt). Swerves modules 0 and 3 turn clockwise while swerve modules 1 and 2
   * turn counterclockwise. From our tests it did reduce our shifting.
   */
  public void setToNinety() {
    double[] angles = { 90.0, 270.0, 270.0, 90.0 };
    for (int i = 0; i < m_SwerveMods.length; i++) {
      m_SwerveMods[i].setDesiredStateNoOptimize(
          new SwerveModuleState(0, Rotation2d.fromDegrees(angles[i])), true, true);
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

  @Override
  public double getModZeroAngle() {
    return m_SwerveMods[0].getAngle().getDegrees();
  }

  public RobotConfig getFromConfig() throws IOException, ParseException {
    boolean isHolonomic = m_config.getBoolean("pathplanning.holonomic");
    double massKG = m_config.getDouble("pathplanning.mass");
    double MOI = m_config.getDouble("pathplanning.MOI");
    // converting from inches to meters
    double wheelRadius = (m_config.getDouble("wheelDiameter") / 2) / 39.37;
    double gearing = m_config.getDouble("driveGearRatio");
    double maxDriveSpeed = m_config.getDouble("maxSpeed");
    double wheelCOF = m_config.getDouble("pathplanning.wheelCOF");
    String driveMotor = m_config.getString("pathplanning.driveMotorType");
    double driveCurrentLimit = m_config.getDouble("driveContinuousCurrentLimit");

    int numMotors = isHolonomic ? 1 : 2;
    DCMotor gearbox = switch (driveMotor) {
    case "krakenX60" -> DCMotor.getKrakenX60(numMotors);
    case "krakenX60FOC" -> DCMotor.getKrakenX60Foc(numMotors);
    case "falcon500" -> DCMotor.getFalcon500(numMotors);
    case "falcon500FOC" -> DCMotor.getFalcon500Foc(numMotors);
    case "vortex" -> DCMotor.getNeoVortex(numMotors);
    case "NEO" -> DCMotor.getNEO(numMotors);
    case "CIM" -> DCMotor.getCIM(numMotors);
    case "miniCIM" -> DCMotor.getMiniCIM(numMotors);
    default -> throw new IllegalArgumentException("Invalid motor type: " + driveMotor);
    };
    gearbox = gearbox.withReduction(gearing);

    ModuleConfig moduleConfig = new ModuleConfig(wheelRadius, maxDriveSpeed, wheelCOF, gearbox,
        driveCurrentLimit, numMotors);

    if (isHolonomic) {
      Translation2d[] moduleOffsets = new Translation2d[] {
          new Translation2d(m_config.getDouble("pathplanning.FLX"),
              m_config.getDouble("pathplanning.FLY")),
          new Translation2d(m_config.getDouble("pathplanning.FRX"),
              m_config.getDouble("pathplanning.FRY")),
          new Translation2d(m_config.getDouble("pathplanning.BLX"),
              m_config.getDouble("pathplanning.BLY")),
          new Translation2d(m_config.getDouble("pathplanning.BRX"),
              m_config.getDouble("pathplanning.BRY")) };

      return new RobotConfig(massKG, MOI, moduleConfig, moduleOffsets);
    } else {
      double trackwidth = m_config.getDouble("pathplanning.driveBaseRadiusInMeters");

      return new RobotConfig(massKG, MOI, moduleConfig, trackwidth);
    }
  }

  @Override
  public double getHubDistanceToRobotInInches() {
    return getHubDistanceToRobotInInches(currentPose2d());
  }

  private double getHubDistanceToRobotInInches(Pose2d pose) {
    Pose2d hubPose2d = m_fieldConstants.getHubPose();
    double xDistance = hubPose2d.getX() - pose.getX();
    double yDistance = hubPose2d.getY() - pose.getY();
    double distance = Math.sqrt(Math.pow(yDistance, 2) + Math.pow(xDistance, 2));
    // convert from meters to inches.
    return (distance * 39.3701);
  }

  @Override
  public DoubleSupplier getHubDistanceToRobotInInchesSupplier() {
    return () -> getHubDistanceToRobotInInches();
  }

  @Override
  public double getShuttleDistanceToRobotInInches() {
    Pose2d shuttlePose2d;
    if (m_fieldConstants.isRightSide(currentPose2d())) {
      shuttlePose2d = m_fieldConstants.getTargetPoseShuttleRight();
    } else {
      shuttlePose2d = m_fieldConstants.getTargetPoseShuttleLeft();
    }
    double xDistance = shuttlePose2d.getX() - currentPose2d().getX();
    double yDistance = shuttlePose2d.getY() - currentPose2d().getY();
    double distance = Math.sqrt(Math.pow(yDistance, 2) + Math.pow(xDistance, 2));
    // convert from meters to inches.
    return (distance * 39.3701);
  }

  @Override
  public DoubleSupplier getShuttleDistanceToRobotInInchesSupplier() {
    return () -> getShuttleDistanceToRobotInInches();
  }

  @Override
  public double getRobotToFieldElementAngle(Pose2d objPose) {
    if (objPose == null) {
      // failsafe
      return 0;
    }
    Pose2d robotPose = currentPose2d();
    double a = objPose.getX() - robotPose.getX();
    double b = objPose.getY() - robotPose.getY();
    double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    double angle = (Math.toDegrees(Math.asin(a / c))) - 90; // 90 normalizes the angle;
    // adjusting the angle based on being on the left side of the field
    if (b < 0) {
      angle = angle + m_robotFieldElementPoseOffset;
      angle = (-1) * angle;
    } else {
      angle = angle - m_robotFieldElementPoseOffset;
    }
    // negative angles don't work with auto
    // either the robot goes to the wrong angle or it never finishes
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  @Override
  public DoubleSupplier getRobotToFieldElementAngleSupplier(Pose2d objPose) {
    return () -> getRobotToFieldElementAngle(objPose);
  }
}
