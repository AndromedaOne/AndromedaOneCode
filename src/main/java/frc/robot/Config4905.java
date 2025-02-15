package frc.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
  private Config m_harvesterConfig;
  private Config m_conveyorConfig;
  private Config m_compressorConfig;
  private Config m_showBotCannonConfig;
  private Config m_showBotCannonElevatorConfig;
  private Config m_showBotAudioConfig;
  private Config m_romiBallMopperConfig;
  private Config m_wingsConfig;
  private Config m_shooterConfig;
  private Config m_climberConfig;
  private Config m_intakeConfig;
  private Config m_feederConfig;
  private Config m_sbsdArmConfig;
  private Config m_sbsdCoralEndEffectorConfig;
  private Config m_RedAutonomousConfig;
  private Config m_BlueAutonomousConfig;
  private static Config4905 m_config4905 = null;

  // current linux home dir on a roborio
  private final String m_linuxPathToHomeStr = "/home/lvuser/";

  private String m_baseDir;
  private String m_robotName;
  private boolean m_isRomi = false;
  private boolean m_isShowBot = false;
  private boolean m_isTopGun = false;
  private boolean m_isSwerveBot = false;
  private boolean m_isSBSD = false;

  private Config4905() {
    // first look to see if this is a roborio
    if (Files.exists(Paths.get(m_linuxPathToHomeStr))) {
      m_baseDir = m_linuxPathToHomeStr;
      m_nameConfig = ConfigFactory.parseFile(new File(m_linuxPathToHomeStr + "name.conf"));
      m_robotName = m_nameConfig.getString("robot.name");
      if (m_robotName.equals("ShowBot")) {
        m_isShowBot = true;
      } else if (m_robotName.equals("TopGun")) {
        m_isTopGun = true;
      } else if (m_robotName.equals("SwerveBot")) { // Name pending
        m_isSwerveBot = true;
      } else if (m_robotName.equals("SBSD")) {
        m_isSBSD = true;
      }
    } else {
      // try to figure out which Romi we're on by looking at the SSID's we're
      // connected to
      // all of our Romi's will start with "4905_Romi" and potentially have some
      // identifier
      // after Romi. so search for 4905_Romi and extract the romi name and use this
      // as the robot name
      try {
        Process proc = Runtime.getRuntime().exec("netsh wlan show interfaces");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = bReader.readLine()) != null) {
          if (line.matches("(.*)SSID(.*)4905_Romi(.*)")) {
            System.out.println("SSID line: " + line);
            String[] tokens = line.trim().split("\\s+");
            System.out.println("Romi Robot name = " + tokens[2]);
            m_robotName = tokens[2];
          }
        }
      } catch (IOException e) {
        System.out.println("ERROR: cannot find name of Romi via SSID");
        e.printStackTrace();
      }
      // the next line retrieves the path to the jar file that is being
      // executed. this should be in a standard place in the repo. from there
      // we can find the deploy directory and the configs
      String relativePathToDeploy = "/src/main/deploy";
      String jarDir = System.getProperty("user.dir");
      if (!Files.exists(Paths.get(jarDir + relativePathToDeploy))) {
        System.out.println(
            "ERROR: could not find robot config directory: " + jarDir + relativePathToDeploy);
      }
      // don't look for name.conf file, just use Romi
      m_baseDir = jarDir + "/src/main/";
      m_isRomi = true;
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
    m_climberConfig = load("climber.conf");
    m_ledConfig = load("LED.conf");
    m_leftLedConfig = load("leftLED.conf");
    m_rightLedConfig = load("rightLED.conf");
    m_ws2812LEDsConfig = load("ws2812LEDs.conf");
    m_harvesterConfig = load("harvester.conf");
    m_conveyorConfig = load("conveyor.conf");
    m_compressorConfig = load("compressor.conf");
    m_showBotCannonConfig = load("showBotCannon.conf");
    m_showBotCannonElevatorConfig = load("showBotCannonElevator.conf");
    m_showBotAudioConfig = load("showBotAudio.conf");
    m_romiBallMopperConfig = load("romiBallMopper.conf");
    m_wingsConfig = load("wings.conf");
    m_shooterConfig = load("shooter.conf");
    m_intakeConfig = load("intake.conf");
    m_feederConfig = load("feeder.conf");
    m_sbsdArmConfig = load("sbsdarm.conf");
    m_sbsdCoralEndEffectorConfig = load("coralendeffector.conf");
    m_RedAutonomousConfig = load("RedAutonomous.conf");
    m_BlueAutonomousConfig = load("BlueAutonomous.conf");
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

  public boolean doesHarvesterExist() {
    if (m_config.hasPath("subsystems.harvester")) {
      return true;
    } else {
      return false;
    }
  }

  public boolean doesRomiWingsExist() {
    return m_config.hasPath("subsystems.romiWings");
  }

  public Config getHarvesterConfig() {
    return m_harvesterConfig;
  }

  public boolean doesConveyorExist() {
    if (m_config.hasPath("subsystems.conveyor")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getClimberConfig() {
    return m_climberConfig;
  }

  public boolean doesClimberExist() {
    if (m_config.hasPath("subsystems.climber")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getConveyorConfig() {
    return m_conveyorConfig;
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

  public boolean doesShowBotCannonExist() {
    if (m_config.hasPath("subsystems.showBotCannon")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getShowBotCannonConfig() {
    return m_showBotCannonConfig;
  }

  public boolean doesShowBotCannonElevatorExist() {
    if (m_config.hasPath("subsystems.showbotCannonElevator")) {
      return true;
    } else {
      return false;
    }
  }

  public Config getShowBotCannonElevatorConfig() {
    return m_showBotCannonElevatorConfig;
  }

  public boolean doesShowBotAudioExist() {
    if (m_config.hasPath("subsystems.showBotAudio")) {
      return true;
    }
    return false;
  }

  public Config getShowBotAudioConfig() {
    return m_showBotAudioConfig;
  }

  public Config getRomiBallMopperConfig() {
    return m_romiBallMopperConfig;
  }

  public boolean doesRomiBallMopperExist() {
    if (m_config.hasPath("subsystems.romiBallMopper")) {
      return true;
    }
    return false;
  }

  public boolean doesShooterExist() {
    if (m_config.hasPath("subsystems.shooter")) {
      return true;
    }
    return false;
  }

  public Config getShooterConfig() {
    return m_shooterConfig;
  }

  public boolean doesFeederExist() {
    if (m_config.hasPath("subsystems.feeder")) {
      return true;
    }
    return false;
  }

  public Config getFeederConfig() {
    return m_feederConfig;
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

  public Config getSensorConfig() {
    return m_sensorConfig;
  }

  public Config getCommandConstantsConfig() {
    return m_commandConstantsConfig;
  }

  public Config getWingsConfig() {
    return m_wingsConfig;
  }

  public Config getIntakeConfig() {
    return m_intakeConfig;
  }

  public Config getRedAutonomousConfig() {
    return m_RedAutonomousConfig;
  }

  public Config getBlueAutonomousConfig() {
    return m_BlueAutonomousConfig;
  }

  public boolean doesIntakeExist() {
    if (m_config.hasPath("subsystems.intake")) {
      return true;
    }
    return false;
  }

  public boolean isRomi() {
    return m_isRomi;
  }

  public boolean isShowBot() {
    return m_isShowBot;
  }

  public boolean isTopGun() {
    return m_isTopGun;
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