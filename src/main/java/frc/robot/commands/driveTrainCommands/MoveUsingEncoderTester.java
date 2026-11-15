package frc.robot.commands.driveTrainCommands;

import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.SequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

public class MoveUsingEncoderTester extends Command {
  private DriveTrainBase m_driveTrain;

  public MoveUsingEncoderTester(DriveTrainBase drivetrain) {
    m_driveTrain = drivetrain;
  }

  @Override
  public void initialize() {
    double distance = SmartDashboard.getNumber("MoveUsingEncoderTester Distance To Move", 24);
    double angle = SmartDashboard.getNumber("MoveUsingEncoderTester Angle To Move", 0);
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
        new MoveUsingEncoder(m_driveTrain, () -> angle, () -> distance, 1)));
    Trace.getInstance().logCommandInfo(this, "Moving distance: " + distance);
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void end(boolean interrupted) {
  }

}
