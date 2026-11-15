/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

public class MoveUsingDistanceSensor extends SequentialCommandGroup4905 {
  // move the robot until the distance sensor is at the targetDistance
  public MoveUsingDistanceSensor(DriveTrainBase drivetrain, double targetDistance,
      DoubleSupplier angle, double maxOutput, boolean useCurrentHeading, DistanceSensorBase tof1) {
    addCommands(new SwerveDriveSetWheelsToNinetyDegrees(drivetrain),
        new MoveUsingDistanceSensorInternal(drivetrain, targetDistance, angle, maxOutput,
            useCurrentHeading, tof1));
  }

  // Use this constructor to move the robot in the heading passed in
  public MoveUsingDistanceSensor(DriveTrainBase drivetrain, double targetDistance,
      DoubleSupplier angle, double maxOutput, DistanceSensorBase tof1) {
    this(drivetrain, targetDistance, angle, maxOutput, false, tof1);
  }

  // Use this constructor to move the robot in the direction it's already pointing
  public MoveUsingDistanceSensor(DriveTrainBase driveTrain, double targetDistance, double maxOutput,
      DistanceSensorBase tof1) {
    this(driveTrain, targetDistance, () -> 0, maxOutput, true, tof1);
  }

  // this is the actual PID loop command
  private class MoveUsingDistanceSensorInternal extends PIDCommand4905 {
    private DriveTrainBase m_driveTrain;
    private double m_targetDistance = 0;
    private double m_maxOutput = 0;
    private boolean m_useCurrentHeading = false;
    private DistanceSensorBase m_tof1;

    /**
     * Creates a new MoveUsingDistanceSensor.
     */
    public MoveUsingDistanceSensorInternal(DriveTrainBase drivetrain, double targetDistance,
        DoubleSupplier angle, double maxOutput, boolean useCurrentHeading,
        DistanceSensorBase tof1) {
      super(
          // The controller that the command will use
          new PIDController4905SampleStop("MoveUsingDistanceSensor"),
          // This should return the measurement
          tof1.getDistanceInchesAsSupplier(),
          // This should return the setpoint (can also be a constant)
          () -> targetDistance,
          // This uses the output
          output -> {
            // Use the output here
            drivetrain.moveUsingGyroStrafe(output, angle.getAsDouble(), false);
          });
      m_targetDistance = targetDistance;
      m_driveTrain = drivetrain;
      m_maxOutput = maxOutput;
      m_useCurrentHeading = useCurrentHeading;
      m_tof1 = tof1;
      // Configure additional PID options by calling `getController` here.
      addRequirements(drivetrain.getSubsystemBase());
    }

    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();
      Trace.getInstance().logCommandInfo(this, "Target Distance: " + m_targetDistance);
      getController().setP(pidConstantsConfig.getDouble("MoveUsingDistanceSensor.Kp"));
      getController().setI(pidConstantsConfig.getDouble("MoveUsingDistanceSensor.Ki"));
      getController().setD(pidConstantsConfig.getDouble("MoveUsingDistanceSensor.Kd"));
      getController().setMinOutputToMove(
          pidConstantsConfig.getDouble("MoveUsingDistanceSensor.minOutputToMove"));
      getController()
          .setTolerance(pidConstantsConfig.getDouble("MoveUsingDistanceSensor.positionTolerance"));
      // Allows anyone who calls MoveUsingDistanceSensor to override the maxOutput
      // defined in
      // config (if present)
      if (m_maxOutput != 0) {
        getController().setMaxOutput(m_maxOutput);
      } else if (pidConstantsConfig.hasPath("MoveUsingDistanceSensor.maxOutput")) {
        getController()
            .setMaxOutput(pidConstantsConfig.getDouble("MoveUsingDistanceSensor.maxOutput"));
      }
      if (m_useCurrentHeading) {
        double heading = Robot.getInstance().getSensorsContainer().getGyro().getCompassHeading();
        super.setOutput(output -> {
          m_driveTrain.moveUsingGyro(output, 0, false, heading);
        });
      }
      Trace.getInstance().logCommandInfo(this,
          "Moving with DistanceSensor to position: " + getSetpoint().getAsDouble());
      Trace.getInstance().logCommandInfo(this,
          "Starting DistanceSensor position: " + m_tof1.getDistance_Inches());
    }

    public DoubleSupplier getSetpoint() {
      return () -> m_targetDistance;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return (getController().atSetpoint()) || (!m_tof1.isSensorDetecting());
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      m_driveTrain.stop();
      Trace.getInstance().logCommandInfo(this,
          "Ending Distance Sensor Reading: " + m_tof1.getDistance_Inches());
    }
  }
}
