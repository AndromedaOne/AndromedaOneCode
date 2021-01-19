package frc.robot;

import java.io.File;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Config4905 {
  private static Config nameConfig = ConfigFactory.parseFile(new File("/home/lvuser/name.conf"));

  /**
   * This config should live on the robot and have hardware- specific configs.
   */
  private static Config environmentalConfig = ConfigFactory
      .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/robot.conf"));

  /**
   * This config lives in the jar and has hardware-independent configs.
   */
  private static Config defaultConfig = ConfigFactory.parseResources("application.conf");

  /**
   * Combined config
   */
  private static Config m_config = environmentalConfig.withFallback(defaultConfig).resolve();

  private static Config climberConfig;

  private static Config drivetrainConfig;

  private static Config feederConfig;

  private static Config intakeConfig;

  private static Config shooterConfig;

  private static Config sensorConfig;

  private static Config commandConstantsConfig;

  private static Config robotDimensionsConfig;

  private static final Config4905 m_config4905 = new Config4905();

  private static final String BASEDIRECTORY = "/home/lvuser/deploy/robotConfigs/";

  private Config4905() {
    System.out.println("Robot name = " + nameConfig.getString("robot.name"));
  }

  public static Config4905 getConfig4905() {
    return m_config4905;
  }

  private static Config load(String fileName) {
    String filePath = BASEDIRECTORY + nameConfig.getString("robot.name") + "/" + fileName;
    Config config = ConfigFactory.parseFile(new File(filePath)).withFallback(defaultConfig).resolve();

    System.out.println("loaded config " + fileName + " from " + filePath);

    System.out.println(config);
    return config;
  }

  public static void reload() {
    commandConstantsConfig = load("commandconstants.conf");
    sensorConfig = load("sensors.conf");
    shooterConfig = load("Shooter.conf");
    intakeConfig = load("Intake.conf");
    feederConfig = load("Feeder.conf");
    drivetrainConfig = load("drivetrain.conf");
    climberConfig = load("Climber.conf");
    robotDimensionsConfig = load("Dimensions.conf");
  }

  static {
    reload();
  }

  public Config getClimberConfig() {
    return climberConfig;
  }

  public boolean doesClimberExist() {
    if (m_config.hasPath("subsystems.climber")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getDrivetrainConfig() {
    return drivetrainConfig;
  }

  public boolean doesDrivetrainExist() {
    if (m_config.hasPath("subsystems.driveTrain")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getFeederConfig() {
    return feederConfig;
  }

  public boolean doesFeederExist() {
    if (m_config.hasPath("subsystems.feeder")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getIntakeConfig() {
    return intakeConfig;
  }

  public boolean doesIntakeExist() {
    if (m_config.hasPath("subsystems.intake")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getShooterConfig() {
    return shooterConfig;
  }

  public boolean doesShooterExist() {
    if (m_config.hasPath("subsystems.shooter")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getSensorConfig() {
    return sensorConfig;
  }

  public Config getCommandConstantsConfig() {
    return commandConstantsConfig;
  }

  public Config getRobotDimensionsConfig() {
    return robotDimensionsConfig;
  }
}