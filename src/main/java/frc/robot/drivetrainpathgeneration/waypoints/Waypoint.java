package frc.robot.drivetrainpathgeneration.waypoints;

import java.util.Iterator;

public class Waypoint {
    private double m_x;
    private double m_y;

    public Waypoint(double x, double y) {
        m_x = x;
        m_y = y;
    }

    public double getX() {
        return m_x;
    }

    public double getY() {
        return m_y;
    }
}
