package frc.robot.commands.photonVisionCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class SetPoseUsingSmartDashboard extends Command {

  private DriveTrainBase m_drivetrain;

  public SetPoseUsingSmartDashboard(DriveTrainBase drivetrain) {
    m_drivetrain = drivetrain;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_drivetrain.resetOdometry(new Pose2d(SmartDashboard.getNumber("Set Pose X", 0),
        SmartDashboard.getNumber("Set Pose Y", 0),
        new Rotation2d(SmartDashboard.getNumber("Set Pose Angle", 0))));
  }
}
