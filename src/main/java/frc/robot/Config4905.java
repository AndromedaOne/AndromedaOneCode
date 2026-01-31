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
  private Config m_ws2812LEDsConfig;
  private Config m_compressorConfig;
  private Config m_ejectBeltConfig;
  private Config m_hopperBeltsConfig;
  private Config m_AHIConfig;
  private Config m_intakerollersConfig;
  private Config m_climberConfig;
  private static Config4905 m_config4905 = null;

  // current linux home dir on a roborio
  private final String m_linuxPathToHomeStr = "/home/lvuser/";

  private String m_baseDir;
  private String m_robotName;
  private boolean m_isSwerveBot = false;

  private Config4905() {
    // first look to see if this is a roborio
    if (Files.exists(Paths.get(m_linuxPathToHomeStr))) {
      m_baseDir = m_linuxPathToHomeStr;
      m_nameConfig = ConfigFactory.parseFile(new File(m_linuxPathToHomeStr + "name.conf"));
      m_robotName = m_nameConfig.getString("robot.name");
      if (m_robotName.equals("SwerveBot")) {
        m_isSwerveBot = true;
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
    m_ws2812LEDsConfig = load("ws2812LEDs.conf");
    m_compressorConfig = load("compressor.conf");
    m_ejectBeltConfig = load("ejectbelt.conf");
    m_hopperBeltsConfig = load("hopperbelts.conf");
    m_AHIConfig = load("ahi.conf");
    m_intakerollersConfig = load("intakerollers.conf");
    m_climberConfig = load("climber.conf");
  }

  public Config getControllersConfig() {
    return m_controllers;
  }

  public Config getDrivetrainConfig() {
    return m_drivetrainConfig;
  }

  public boolean doesTankDrivetrainExist() {
    return m_config.hasPath("subsystems.driveTrain");
  }

  public boolean doesDrivetrainExist() {
    return doesTankDrivetrainExist() || doesSwerveDrivetrainExist();
  }

  public Config getSwerveDrivetrainConfig() {
    return m_swervedrivetrainConfig;

  }

  public boolean doesSwerveDrivetrainExist() {
    return m_config.hasPath("subsystems.swervedrivetrain");
  }

  public Config getWS2812LEDsConfig() {
    return m_ws2812LEDsConfig;
  }

  public boolean doesWS2812LEDsExist() {
    return m_config.hasPath("subsystems.WS2812LEDs");
  }

  public boolean doesCompressorExist() {
    return m_config.hasPath("subsystems.compressor");
  }

  public Config getCompressorConfig() {
    return m_compressorConfig;
  }

  public boolean doesHopperBeltsExist() {
    return m_config.hasPath("subsystems.hopperbelts");
  }

  public Config getHopperBeltsConfig() {
    return m_hopperBeltsConfig;
  }

  public boolean doesAHIExist() {
    return m_config.hasPath("subsystems.ahi");
  }

  public Config getAHIConfig() {
    return m_AHIConfig;
  }

  public boolean doesIntakeRollersExist() {
    return m_config.hasPath("subsystems.intakerollers");
  }

  public Config getIntakeRollersConfig() {
    return m_intakerollersConfig;
  }

  public boolean doesClimberExist() {
    return m_config.hasPath("subsystems.climber");
  }

  public Config getClimberConfig() {
    return m_climberConfig;
  }

  public Config getSensorConfig() {
    return m_sensorConfig;
  }

  public Config getCommandConstantsConfig() {
    return m_commandConstantsConfig;
  }

  public boolean doesEjectBeltExist() {
    return m_config.hasPath("subsystems.ejectbelt");
  }

  public Config getEjectBeltConfig() {
    return m_ejectBeltConfig;
  }

  public boolean isSwerveBot() {
    return m_isSwerveBot;
  }

  public String getRobotName() {
    return m_robotName;
  }
}