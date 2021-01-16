/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib.WayPointPath;

import java.util.*; 

/**
 * Add your docs here.
 */
public class WayPoints {
    private Vector<WayPoint> m_wayPoints = new Vector<WayPoint>();

    public WayPoints()
    {
        m_wayPoints.add(new WayPoint(2, 3));
        m_wayPoints.add(new WayPoint(5, 8));
    }

    public Iterator<WayPoint> iterator()
    {
        return(m_wayPoints.iterator());
    }
}
