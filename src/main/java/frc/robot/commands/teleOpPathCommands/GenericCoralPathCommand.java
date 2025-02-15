package frc.robot.commands.teleOpPathCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

public class GenericCoralPathCommand extends SequentialCommandGroup4905 {

  DriveController m_driveController;

  public GenericCoralPathCommand(Command... commands) {
    super(commands);
  }

  @Override
  public void additionalInitialize() {
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
  }

  @Override
  public boolean isFinished() {
    if (super.isFinished()) {
      return true;
    }
    // when x button let go, stop command
    return !m_driveController.getCoralScoring();
  }
}
