package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.SequentialCommandGroup;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

public class MoveUsingDistanceSensorTester extends Command {
  private DriveTrainBase m_driveTrain;
  private DoubleSupplier m_angle;
  private DistanceSensorBase m_tof;

  public MoveUsingDistanceSensorTester(DriveTrainBase drivetrain, DoubleSupplier angle,
      DistanceSensorBase tof) {
    m_driveTrain = drivetrain;
    m_angle = angle;
    m_tof = tof;

  }

  @Override
  public void initialize() {
    double distance = SmartDashboard.getNumber("MoveUsingDistanceSensorTester Distance To Move", 6);
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
        new MoveUsingDistanceSensor(m_driveTrain, distance, m_angle, 0.3, m_tof)));
    Trace.getInstance().logCommandInfo(this, "Moving to distance sensor value: " + distance);
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
