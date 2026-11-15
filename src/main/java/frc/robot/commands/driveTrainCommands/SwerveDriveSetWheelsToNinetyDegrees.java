package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class SwerveDriveSetWheelsToNinetyDegrees extends Command {
  DriveTrainBase m_driveTrainBase;
  int m_count = 0;

  /** Creates a new SwerveDriveSetWheelsToZeroDegrees. */
  public SwerveDriveSetWheelsToNinetyDegrees(DriveTrainBase drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain.getSubsystemBase());
    m_driveTrainBase = drivetrain;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_driveTrainBase.setToNinety();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ++m_count;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrainBase.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_count >= 2) {
      return true;
    }
    return false;
  }
}
