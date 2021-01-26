package frc.robot.groupcommands.athomechallengepathways;

import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class GalacticSearchPathARed extends WaypointsBase {

  @Override
  protected void loadWaypoints() {
    addWayPoint(AtHomeChallengePoints.C1);
    addWayPoint(AtHomeChallengePoints.C3);
    addWayPoint(AtHomeChallengePoints.D5);
    addWayPoint(AtHomeChallengePoints.A6);
    addWayPoint(AtHomeChallengePoints.A11);
  }
}
