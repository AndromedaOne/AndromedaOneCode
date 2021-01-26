package frc.robot.groupcommands.athomechallengepathways;

import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class GalacticSearchPathBRed extends WaypointsBase {

  @Override
  protected void loadWaypoints() {
    addWayPoint(AtHomeChallengePoints.B1);
    addWayPoint(AtHomeChallengePoints.B3);
    addWayPoint(AtHomeChallengePoints.D5);
    addWayPoint(AtHomeChallengePoints.B7);
    addWayPoint(AtHomeChallengePoints.B11);
  }
}
