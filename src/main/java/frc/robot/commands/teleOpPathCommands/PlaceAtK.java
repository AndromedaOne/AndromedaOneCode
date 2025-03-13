// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.teleOpPathCommands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PlaceAtK extends SequentialCommandGroup4905 {
  /**
   * Creates a new SwervePathPlanningPath.
   * 
   * @throws ParseException
   * @throws IOException
   * @throws FileVersionException
   */
  DriveTrainBase m_driveTrainBase;

  public PlaceAtK() throws FileVersionException, IOException, ParseException {
    m_driveTrainBase = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    Pose2d targetPose = new Pose2d(3.508, 5.439, Rotation2d.fromDegrees(300));
    PathConstraints constraints = new PathConstraints(5.0, 3.0, Units.degreesToRadians(540),
        Units.degreesToRadians(720));
    Command pathFindingCommand = AutoBuilder.pathfindToPose(targetPose, constraints, 0.0);
    addCommands(pathFindingCommand);
  }
}
