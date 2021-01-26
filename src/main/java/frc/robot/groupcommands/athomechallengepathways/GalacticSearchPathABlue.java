package frc.robot.groupcommands.athomechallengepathways;

import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class GalacticSearchPathABlue extends WaypointsBase {

  @Override
  protected void loadWaypoints() {
    addWayPoint(AtHomeChallengePoints.C1);
    addWayPoint(AtHomeChallengePoints.E6);
    addWayPoint(AtHomeChallengePoints.B7);
    addWayPoint(AtHomeChallengePoints.D11);
  }

}
