// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.teleOpPathCommands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.util.FileVersionException;

import frc.robot.commands.sbsdTeleOpCommands.FinishPath;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.utils.PoseEstimation4905;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FinishF4 extends SequentialCommandGroup4905 {
  /**
   * Creates a new SwervePathPlanningPath.
   * 
   * @throws ParseException
   * @throws IOException
   * @throws FileVersionException
   */

  public FinishF4() throws FileVersionException, IOException, ParseException {
    addCommands(new FinishPath(false, PoseEstimation4905.RegionsForPose.NORTHEAST, false));
  }

}
