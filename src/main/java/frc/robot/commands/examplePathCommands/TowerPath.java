// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.examplePathCommands;

import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TowerPath extends SequentialCommandGroup4905 {
  /**
   * Creates a new TowerPath.
   * 
   * @throws org.json.simple.parser.ParseException
   * @throws IOException
   * @throws FileVersionException
   */
  DriveTrainBase m_drivetrain;

  public TowerPath()
      throws FileVersionException, IOException, org.json.simple.parser.ParseException {
    m_drivetrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    addRequirements(m_drivetrain.getSubsystemBase());
    PathPlannerPath path = PathPlannerPath.fromPathFile("GoToTowerTest");
    Command pathCommand = AutoBuilder.followPath(path);
    addCommands(pathCommand);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.

  // Returns true when the command should end.
}
