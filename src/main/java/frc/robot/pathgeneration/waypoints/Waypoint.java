package frc.robot.pathgeneration.waypoints;

import java.awt.geom.Point2D;

public class Waypoint extends Point2D{
    private double m_x;
    private double m_y;
    private double m_heading;

    public Waypoint(double x, double y, double heading) {
        m_x = x;
        m_y = y;
        m_heading = heading;
    }

    public double getX() {
        return m_x;
    }

    public double getY() {
        return m_y;
    }

    @Override
    public void setLocation(double x, double y) {
        x = m_x;
        y = m_y;

    }

    public double getHeading() {
        return m_heading;
    }
}
