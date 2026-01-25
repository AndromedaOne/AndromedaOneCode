package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

public class MoveUsingDistanceSensorTester extends Command {
  private DriveTrainBase m_driveTrain;
  private DoubleSupplier m_distanceValueSupplier;

  public MoveUsingDistanceSensorTester(DriveTrainBase drivetrain,
      DoubleSupplier distanceSensorSupplier) {
    m_driveTrain = drivetrain;
    m_distanceValueSupplier = distanceSensorSupplier;
  }

  @Override
  public void initialize() {
    double distance = SmartDashboard.getNumber("MoveUsingDistanceSensorTester Distance To Move", 6);
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
        new MoveUsingDistanceSensor(m_driveTrain, m_distanceValueSupplier, distance, 0.3)));
    Trace.getInstance().logCommandInfo(this, "Moving to distance sensor value: " + distance);
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void end(boolean interrupted) {
  }

}
