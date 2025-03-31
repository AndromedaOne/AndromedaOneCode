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
  private Config m_controllers;
  private Config m_drivetrainConfig;
  private Config m_swervedrivetrainConfig;
  private Config m_sensorConfig;
  private Config m_commandConstantsConfig;
  private Config m_ledConfig;
  private Config m_leftLedConfig;
  private Config m_rightLedConfig;
  private Config m_ws2812LEDsConfig;
  private Config m_compressorConfig;
  private Config m_sbsdClimberConfig;
  private Config m_sbsdArmConfig;
  private Config m_sbsdCoralEndEffectorConfig;
  private Config m_sbsdAlgaeManipulatorConfig;
  private static Config4905 m_config4905 = null;

  // current linux home dir on a roborio
  private final String m_linuxPathToHomeStr = "/home/lvuser/";

  private String m_baseDir;
  private String m_robotName;
  private boolean m_isSwerveBot = false;
  private boolean m_isSBSD = false;

  private Config4905() {
    // first look to see if this is a roborio
    if (Files.exists(Paths.get(m_linuxPathToHomeStr))) {
      m_baseDir = m_linuxPathToHomeStr;
      m_nameConfig = ConfigFactory.parseFile(new File(m_linuxPathToHomeStr + "name.conf"));
      m_robotName = m_nameConfig.getString("robot.name");
      if (m_robotName.equals("SwerveBot")) { // Name pending
        m_isSwerveBot = true;
      } else if (m_robotName.equals("SBSD")) {
        m_isSBSD = true;
      }
    }
    if ((m_robotName == null) || m_robotName.isEmpty()) {
      throw new RuntimeException("ERROR: cannot determine robot name, maybe you're not connected?");
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
    Config config = ConfigFactory.parseFile(new File(filePath)).withFallback(m_defaultConfig)
        .resolve();
    System.out.println("loaded config " + fileName + " from " + filePath);
    System.out.println(config);
    return config;
  }

  public void reload() {
    m_commandConstantsConfig = load("commandconstants.conf");
    m_controllers = load("controllers.conf");
    m_sensorConfig = load("sensors.conf");
    m_drivetrainConfig = load("drivetrain.conf");
    m_swervedrivetrainConfig = load("swervedrivetrain.conf");
    m_ledConfig = load("LED.conf");
    m_leftLedConfig = load("leftLED.conf");
    m_rightLedConfig = load("rightLED.conf");
    m_ws2812LEDsConfig = load("ws2812LEDs.conf");
    m_compressorConfig = load("compressor.conf");
    m_sbsdArmConfig = load("sbsdarm.conf");
    m_sbsdCoralEndEffectorConfig = load("coralendeffector.conf");
    m_sbsdAlgaeManipulatorConfig = load("algaemanipulator.conf");
    m_sbsdClimberConfig = load("sbsdclimber.conf");
  }

  public Config getControllersConfig() {
    return (m_controllers);
  }

  public Config getDrivetrainConfig() {
    return m_drivetrainConfig;
  }

  public boolean doesTankDrivetrainExist() {
    if (m_config.hasPath("subsystems.driveTrain")) {
      return true;
    } else {
      return false;
    }
  }

  public boolean doesDrivetrainExist() {
    return doesTankDrivetrainExist() || doesSwerveDrivetrainExist();
  }

  public Config getSwerveDrivetrainConfig() {
    return m_swervedrivetrainConfig;

  }

  public boolean doesSwerveDrivetrainExist() {
    if (m_config.hasPath("subsystems.swervedrivetrain")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getLEDConfig() {
    return m_ledConfig;
  }

  public Config getLeftLEDConfig() {
    return m_leftLedConfig;
  }

  public Config getRightLEDConfig() {
    return m_rightLedConfig;
  }

  public Config getWS2812LEDsConfig() {
    return m_ws2812LEDsConfig;
  }

  public boolean doesLEDExist() {
    if (m_config.hasPath("subsystems.LED")) {
      return true;
    } else {
      return false;
    }
  }

  public boolean doesLeftLEDExist() {
    if (m_config.hasPath("subsystems.leftLED")) {
      return true;
    } else {
      return false;
    }
  }

  public boolean doesRightLEDExist() {
    if (m_config.hasPath("subsystems.rightLED")) {
      return true;
    } else {
      return false;
    }
  }

  public boolean doesWS2812LEDsExist() {
    if (m_config.hasPath("subsystems.WS2812LEDs")) {
      return true;
    }
    return false;
  }

  public boolean doesCompressorExist() {
    if (m_config.hasPath("subsystems.compressor")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getCompressorConfig() {
    return m_compressorConfig;
  }

  public boolean doesSBSDArmExist() {
    return (m_config.hasPath("subsystems.sbsdarm"));
  }

  public Config getSBSDArmConfig() {
    return m_sbsdArmConfig;
  }

  public boolean doesSBSDCoralEndEffectorExist() {
    return (m_config.hasPath("subsystems.sbsdcoralendeffector"));
  }

  public Config getSBSDCoralEndEffectorConfig() {
    return m_sbsdCoralEndEffectorConfig;
  }

  public boolean doesSBSDCoralIntakeEjectExist() {
    return (m_config.hasPath("subsystems.sbsdcoralintakeeject"));
  }

  public boolean doesSBSDAlgaeManipulatorExist() {
    return (m_config.hasPath("subsystems.algaemanipulator"));
  }

  public Config getSBSDAlgaeManipulatorConfig() {
    return m_sbsdAlgaeManipulatorConfig;
  }

  public boolean doesSBSDClimberExist() {
    if (m_config.hasPath("subsystems.sbsdclimber")) {
      return true;
    }
    return false;
  }

  public Config getSBSDClimberConfig() {
    return m_sbsdClimberConfig;
  }

  public Config getSensorConfig() {
    return m_sensorConfig;
  }

  public Config getCommandConstantsConfig() {
    return m_commandConstantsConfig;
  }

  public boolean doesIntakeExist() {
    if (m_config.hasPath("subsystems.intake")) {
      return true;
    }
    return false;
  }

  public boolean isSwerveBot() {
    return m_isSwerveBot;
  }

  public boolean isSBSD() {
    return m_isSBSD;
  }

  public String getRobotName() {
    return m_robotName;
  }
}