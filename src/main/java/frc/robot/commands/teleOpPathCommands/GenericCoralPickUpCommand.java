package frc.robot.commands.teleOpPathCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;

public class GenericCoralPickUpCommand extends ParallelCommandGroup4905 {

  DriveController m_DriveController;

  public GenericCoralPickUpCommand(Command... commands) {
    super(commands);
  }

  @Override
  public void additionalInitialize() {
    m_DriveController = Robot.getInstance().getOIContainer().getDriveController();
  }

  @Override
  public boolean isFinishedAdditional() {
    // when x button let go, stop command
    if (m_DriveController.getCoralLoadWall() == false
        && m_DriveController.getCoralLoadDriver() == false) {
      return true;
    } else {
      return false;
    }
  }
}
