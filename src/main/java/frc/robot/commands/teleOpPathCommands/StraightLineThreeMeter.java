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

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class StraightLineThreeMeter extends SequentialCommandGroup4905 {
  /** Creates a new StraightLineHalfMeter. */

  DriveTrainBase m_driveTrainBase;

  public StraightLineThreeMeter() throws FileVersionException, IOException, ParseException {
    m_driveTrainBase = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    addRequirements(m_driveTrainBase.getSubsystemBase());
    PathPlannerPath path = PathPlannerPath.fromPathFile("Straight Line 3");
    Command pathCommand = AutoBuilder.followPath(path);
    addCommands(pathCommand);
  }
}
