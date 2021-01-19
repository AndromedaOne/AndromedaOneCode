package frc.robot.pathgeneration.waypoints;

import frc.robot.utils.RobotSections;

public class WaypointWithRobotSide extends Waypoint{

    private RobotSections m_robotSides;

    public WaypointWithRobotSide(double x, double y, RobotSections robotside) {
        super(x, y);
        m_robotSides = robotside;
    }

    public RobotSections getSide() {
        return m_robotSides;
    }

    
}
