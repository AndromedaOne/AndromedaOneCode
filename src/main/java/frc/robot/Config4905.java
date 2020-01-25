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

    if (m_config.hasPath("subsystems.driveTrain")) {
      System.out.println("Using real drivetrain");
    } else {
      System.out.println("Using fake drivetrain");
    }
  }

  public static Config4905 getConfig4905() {
    return m_config4905;
  }

  private static Config drivetrainFactory = ConfigFactory
    .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/drivetrain.conf"));
  private static Config drivetrainConfig = drivetrainFactory.withFallback(defaultConfig).resolve();
  
  public Config getDrivetrainConfig() {
       return drivetrainConfig;
  }
}