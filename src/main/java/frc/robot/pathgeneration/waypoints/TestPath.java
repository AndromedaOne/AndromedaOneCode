package frc.robot.pathgeneration.waypoints;

import frc.robot.groupcommands.athomechallengepathways.AtHomeChallengePoints;

public class TestPath extends WaypointsBase{

    @Override
    protected void loadWaypoints() {
        addWayPoint(AtHomeChallengePoints.C3);
    }
    
}
