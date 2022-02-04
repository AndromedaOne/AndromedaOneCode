package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.ConfigReload;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class MoveUsingEncoderTester extends CommandBase {
  private DriveTrain m_driveTrain;

  public MoveUsingEncoderTester(DriveTrain drivetrain) {
    m_driveTrain = drivetrain;
  }

  @Override
  public void initialize() {
    double distance = SmartDashboard.getNumber("MoveUsingEncoderTester Distance To Move", 24);
    CommandScheduler.getInstance()
        .schedule(new SequentialCommandGroup(new ConfigReload(),
            new MoveUsingEncoder(m_driveTrain, distance,
                Robot.getInstance().getSensorsContainer().getGyro().getCompassHeading(), 0.7),
            new TurnDeltaAngle(180)));
    Trace.getInstance().logCommandStart(this);
    Trace.getInstance().logCommandInfo(this, "Moving distance: " + distance + " inches");
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

}
