package frc.robot.commands;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class RunWheels extends CommandBase {
  private DriveTrain m_driveTrain;

  public RunWheels(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;
  }

  @Override
  public void execute() {
    m_driveTrain.tankDriveVolts(6, 6);
    DifferentialDriveWheelSpeeds wheelSpeeds = m_driveTrain.getWheelSpeeds();
    Trace.getInstance().addTrace(true, "DriveTrainWheels",
        new TracePair<Double>("LeftVelocity", wheelSpeeds.leftMetersPerSecond),
        new TracePair<Double>("RightVelocity", wheelSpeeds.rightMetersPerSecond));
  }

}
