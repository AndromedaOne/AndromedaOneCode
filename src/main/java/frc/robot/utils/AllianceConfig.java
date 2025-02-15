package frc.robot.utils;

import java.util.Optional;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Config4905;

public class AllianceConfig {
  public static Config getCurrentAllianceConfig() {
    Config autonomousConfig;
    if (DriverStation.isFMSAttached()) {
      Optional<Alliance> currentAlliance = DriverStation.getAlliance();
      if (currentAlliance.isPresent()) {
        if (currentAlliance.get() == Alliance.Red) {
          autonomousConfig = Config4905.getConfig4905().getRedAutonomousConfig();
        } else {
          autonomousConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
        }
      } else {
        autonomousConfig = Config4905.getConfig4905().getRedAutonomousConfig();
      }
    } else {
      autonomousConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    }
    return autonomousConfig;
  }

  public static Alliance getCurrentAlliance() {
    Alliance alliance = Alliance.Red;
    if (DriverStation.isFMSAttached()) {
      Optional<Alliance> currentAlliance = DriverStation.getAlliance();
      if (currentAlliance.isPresent()) {
        if (currentAlliance.get() == Alliance.Blue) {
          alliance = Alliance.Blue;
        }
      }
    } else {

      Optional<Alliance> currentAlliance = DriverStation.getAlliance();
      if (currentAlliance.isPresent()) {
        if (currentAlliance.get() == Alliance.Blue) {
          alliance = Alliance.Blue;
        }
      }

    }
    return alliance;
  }
}
