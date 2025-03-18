package frc.robot.commands.teleOpPathCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;

public class GenericCoralPickUpCommand extends ParallelCommandGroup4905 {
  DriveController m_driveController;
  CoralIntakeEjectBase m_coralIntake;

  public GenericCoralPickUpCommand(Command... commands) {
    super(commands);
  }

  @Override
  public void additionalInitialize() {
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
    m_coralIntake = Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();
  }

  @Override
  public boolean isFinishedAdditional() {
    // when x button let go, stop command
    if (m_driveController.getCoralLoadWall() == false
        && m_driveController.getCoralLoadDriver() == false) {
      return true;
    } else if (m_coralIntake.getCoralDetected()) {
      return true;
    } else {
      return false;
    }
  }
}
