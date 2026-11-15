package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.SequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

public class MoveUsingDistanceSensorDifferenceTester extends Command {
  private DriveTrainBase m_driveTrain;
  private DoubleSupplier m_angle;

  public MoveUsingDistanceSensorDifferenceTester(DriveTrainBase drivetrain) {
    m_driveTrain = drivetrain;

  }

  @Override
  public void initialize() {
    m_angle = () -> SmartDashboard.getNumber("SensorDifferenceTesterAngle", 0);
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
        new MoveUsingDistanceSensorDifference(m_driveTrain, 0, m_angle, 0.3)));
    Trace.getInstance().logCommandInfo(this, "Moving to angle: " + m_angle.getAsDouble());
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void end(boolean interrupted) {
  }

}
