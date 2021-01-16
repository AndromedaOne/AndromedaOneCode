/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib.WayPointPath;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;

import java.util.*;

/**
 * Add your docs here.
 */
public abstract class PathGenerator {

    private WayPoints m_wayPoints;

    public PathGenerator(WayPoints wayPoints)
    {
        m_wayPoints = wayPoints;
    }

    public abstract CommandBase generatePath();

    protected Iterator<WayPoint> getWayPointIterator()
    {
        return(m_wayPoints.iterator());
    }

    
}
