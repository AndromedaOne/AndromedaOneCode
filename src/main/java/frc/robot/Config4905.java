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

  private static final Config4905 m_config4905 = new Config4905();

  private Config4905() {
    System.out.println("Robot name = " + nameConfig.getString("robot.name"));
  }

  public static Config4905 getConfig4905() {
    return m_config4905;
  }

  private static Config climberFactory = ConfigFactory
      .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/Climber.conf"));
  private static Config climberConfig = climberFactory.withFallback(defaultConfig).resolve();

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

  private static Config drivetrainFactory = ConfigFactory.parseFile(
      new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/drivetrain.conf"));
  private static Config drivetrainConfig = drivetrainFactory.withFallback(defaultConfig).resolve();

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

  private static Config feederFactory = ConfigFactory
      .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/Feeder.conf"));
  private static Config feederConfig = feederFactory.withFallback(defaultConfig).resolve();

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

  private static Config intakeFactory = ConfigFactory
      .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/Intake.conf"));
  private static Config intakeConfig = intakeFactory.withFallback(defaultConfig).resolve();

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

  private static Config shooterFactory = ConfigFactory
      .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/Shooter.conf"));
  private static Config shooterConfig = shooterFactory.withFallback(defaultConfig).resolve();

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

  private static Config sensorFactory = ConfigFactory
      .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/sensors.conf"));

  public Config getSensorConfig() {
    return sensorFactory.withFallback(defaultConfig).resolve();
  }

  private static Config pidConstantsFactory = ConfigFactory.parseFile(
      new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/pidconstants.conf"));
  private static Config pidConstantsConfig = pidConstantsFactory.withFallback(defaultConfig).resolve();

  public Config getPidConstantsConfig() {
    return pidConstantsConfig;
  }
}