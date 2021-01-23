package frc.robot.groupcommands.athomechallengepathways;

import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class TestPath extends WaypointsBase{

    @Override
    protected void loadWaypoints() {
        addWayPoint(AtHomeChallengePoints.B10);
        addWayPoint(AtHomeChallengePoints.E8);
        addWayPoint(AtHomeChallengePoints.A6);
        addWayPoint(AtHomeChallengePoints.B4);
        addWayPoint(AtHomeChallengePoints.E3);

    }
    
}
