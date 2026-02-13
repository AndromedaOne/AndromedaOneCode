package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
