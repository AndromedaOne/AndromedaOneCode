/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.lib.WayPointPath;

import java.util.Iterator;
import java.util.Vector;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * Add your docs here.
 */
public class GenerateBoxPath extends PathGenerator {

    public GenerateBoxPath(WayPoints wayPoints) {
        super(wayPoints);
    }

    @Override
    public Vector<CommandBase> generatePath() {
        Iterator<WayPoint> pointIt = getWayPointIterator();
        SequentialCommandGroup commands = new SequentialCommandGroup();
        while(pointIt.hasNext())
        {
            commands.addCommands(arg0);
        }
        return null;
    }
}
