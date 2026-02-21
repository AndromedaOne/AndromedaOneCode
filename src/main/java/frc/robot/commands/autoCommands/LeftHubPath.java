package frc.robot.commands.autoCommands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

public class LeftHubPath extends SequentialCommandGroup4905 {
  /**
   * Creates a new SwervePathPlanningPath.
   * 
   * @throws ParseException
   * @throws IOException
   * @throws FileVersionException
   */
  public LeftHubPath() throws FileVersionException, IOException, ParseException {
    PathPlannerPath path = PathPlannerPath.fromPathFile("left hub starting path");
    Command pathCommand = AutoBuilder.followPath(path);
    addCommands(pathCommand);
  }
}
