package frc.robot.utils;

import java.util.Optional;

import org.wpilib.driverstation.MatchState;
import org.wpilib.driverstation.RobotState;
import org.wpilib.driverstation.Alliance;
import org.wpilib.driverstation.MatchType;
import org.wpilib.driverstation.DriverStationErrors;
import org.wpilib.driverstation.Alliance;

public class AllianceConfig {
  public static Alliance getCurrentAlliance() {
    Alliance alliance = Alliance.RED;
    if (RobotState.isFMSAttached()) {
      Optional<Alliance> currentAlliance = MatchState.getAlliance();
      if (currentAlliance.isPresent()) {
        if (currentAlliance.get() == Alliance.BLUE) {
          alliance = Alliance.BLUE;
        }
      }
    } else {
      Optional<Alliance> currentAlliance = MatchState.getAlliance();
      if (currentAlliance.isPresent()) {
        if (currentAlliance.get() == Alliance.BLUE) {
          alliance = Alliance.BLUE;
        }
      }
    }
    return alliance;
  }
}
