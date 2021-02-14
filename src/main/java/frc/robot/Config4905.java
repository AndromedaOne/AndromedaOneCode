package frc.robot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

// implemented as a singleton
public class Config4905 {

  private Config m_nameConfig;

  /**
   * This config should live on the robot and have hardware- specific configs.
   */
  private Config m_environmentalConfig;

  /**
   * This config lives in the jar and has hardware-independent configs.
   */
  private Config m_defaultConfig = ConfigFactory.parseResources("application.conf");

  /**
   * Combined config
   */
  private Config m_config;

  private Config climberConfig;

  private Config drivetrainConfig;

  private Config feederConfig;

  private Config intakeConfig;

  private Config shooterConfig;

  private Config sensorConfig;

  private Config commandConstantsConfig;

  private static Config4905 m_config4905 = null;

  // current linux home dir on a roborio
  private final String m_linuxPathToHomeStr = "/home/lvuser/";

  private String m_baseDir;

  String m_robotName;

  private Config4905() {
    // first look to see if this is a roborio
    if (Files.exists(Paths.get(m_linuxPathToHomeStr))) {
      m_baseDir = m_linuxPathToHomeStr;
      m_nameConfig = ConfigFactory.parseFile(new File(m_linuxPathToHomeStr + "name.conf"));
      m_robotName = m_nameConfig.getString("robot.name");
    } else {
      // for now assume we're running a Romi robot
      // the next line retrieves the path to the jar file that is being
      // executed. this should be in a standard place in the repo. from there
      // we can find the deploy directory and the configs
      String relativePathToDeploy = "/src/main/deploy";
      String jarDir = System.getProperty("user.dir");
      if (!Files.exists(Paths.get(jarDir + relativePathToDeploy))) {
        System.out.println("ERROR: could not find robot config directory: " + jarDir + relativePathToDeploy);
      }
      // don't look for name.conf file, just use Romi
      m_baseDir = jarDir + "/src/main/";
      m_robotName = "RomiRobot";
    }
    m_environmentalConfig = ConfigFactory
        .parseFile(new File(m_baseDir + "deploy/robotConfigs/" + m_robotName + "/robot.conf"));
    m_config = m_environmentalConfig.withFallback(m_defaultConfig).resolve();
    reload();
    System.out.println("Robot name = " + m_robotName);
  }

  public static Config4905 getConfig4905() {
    if (m_config4905 == null) {
      m_config4905 = new Config4905();
    }
    return m_config4905;
  }

  private Config load(String fileName) {
    String filePath = m_baseDir + "deploy/robotConfigs/" + m_robotName + "/" + fileName;
    Config config = ConfigFactory.parseFile(new File(filePath)).withFallback(m_defaultConfig).resolve();
    System.out.println("loaded config " + fileName + " from " + filePath);
    System.out.println(config);
    return config;
  }

  public void reload() {
    commandConstantsConfig = load("commandconstants.conf");
    sensorConfig = load("sensors.conf");
    shooterConfig = load("Shooter.conf");
    intakeConfig = load("Intake.conf");
    feederConfig = load("Feeder.conf");
    drivetrainConfig = load("drivetrain.conf");
    climberConfig = load("Climber.conf");
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
}