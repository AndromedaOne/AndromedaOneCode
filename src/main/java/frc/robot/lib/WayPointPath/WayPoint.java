/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib.WayPointPath;

/**
 * Add your docs here.
 */
public class WayPoint extends java.awt.geom.Point2D {

    private double m_x;
    private double m_y;

    public WayPoint(double x, double y)
    {
        m_x = x;
        m_y = y;
    }

    @Override
    public double getX() {
        // TODO Auto-generated method stub
        return m_x;
    }

    @Override
    public double getY() {
        // TODO Auto-generated method stub
        return m_y;
    }

    @Override
    public void setLocation(double arg0, double arg1) {
        m_x = arg0;
        m_y = arg1;
    }
}
