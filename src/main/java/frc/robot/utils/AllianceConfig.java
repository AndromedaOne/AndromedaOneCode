package frc.robot.utils;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AllianceConfig {
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
