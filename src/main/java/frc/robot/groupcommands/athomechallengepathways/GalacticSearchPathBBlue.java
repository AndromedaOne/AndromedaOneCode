package frc.robot.groupcommands.athomechallengepathways;

import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class GalacticSearchPathBBlue extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
        addWayPoint(AtHomeChallengePoints.B1);
        addWayPoint(AtHomeChallengePoints.D6);
        addWayPoint(AtHomeChallengePoints.B8);
        addWayPoint(AtHomeChallengePoints.E11);
    }
}
