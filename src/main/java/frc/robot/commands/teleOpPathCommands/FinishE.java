// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.teleOpPathCommands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FinishE extends SequentialCommandGroup4905 {
  /**
   * Creates a new SwervePathPlanningPath.
   * 
   * @throws ParseException
   * @throws IOException
   * @throws FileVersionException
   */
  DriveTrainBase m_driveTrainBase;

  public FinishE() throws FileVersionException, IOException, ParseException {
    m_driveTrainBase = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    addRequirements(m_driveTrainBase.getSubsystemBase());
    PathPlannerPath path = PathPlannerPath.fromPathFile("Finish E");
    Command pathCommand = AutoBuilder.followPath(path);
    addCommands(pathCommand);
  }

}
