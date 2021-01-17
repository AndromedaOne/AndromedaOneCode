package frc.robot.pathgeneration.waypoints;

import java.awt.geom.Point2D;

import frc.robot.utils.AngleConversionUtils;

public class Waypoint extends Point2D{
    private double m_x;
    private double m_y;
    private double m_heading;

    public Waypoint(double x, double y, double heading) {
        m_x = x;
        m_y = y;
        m_heading = AngleConversionUtils.ConvertAngleToCompassHeading(heading);
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

    public Waypoint subtract(Waypoint w) {
        double deltaX = m_x - w.getX();
        double deltaY = m_y - w.getY();
        double deltaHeading = AngleConversionUtils.ConvertAngleToCompassHeading(m_heading - w.getHeading());

        return new Waypoint(deltaX, deltaY, deltaHeading);
    }
}
