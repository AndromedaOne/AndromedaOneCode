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

public class MoveUsingDistanceSensorDifference extends SequentialCommandGroup4905 {
  // move the robot until the distance sensor is at the targetDistance
  private DistanceSensorBase m_tof0 = Robot.getInstance().getSensorsContainer().getTof0();
  private DistanceSensorBase m_tof2 = Robot.getInstance().getSensorsContainer().getTof2();

  public MoveUsingDistanceSensorDifference(DriveTrainBase drivetrain, double targetDistance,
      DoubleSupplier angle, double maxOutput, boolean useCurrentHeading) {

    addCommands(new SwerveDriveSetWheelsToZeroDegrees(drivetrain),
        new MoveUsingDistanceSensorDifferenceInternal(drivetrain, targetDistance, angle, maxOutput,
            useCurrentHeading));
  }

  // Use this constructor to move the robot in the heading passed in
  public MoveUsingDistanceSensorDifference(DriveTrainBase drivetrain, double targetDistance,
      DoubleSupplier angle, double maxOutput) {

    this(drivetrain, targetDistance, angle, maxOutput, false);
  }

  // Use this constructor to move the robot in the direction it's already pointing
  public MoveUsingDistanceSensorDifference(DriveTrainBase driveTrain, double targetDistance,
      double maxOutput) {

    this(driveTrain, targetDistance, () -> 0, maxOutput, true);
  }

  private DoubleSupplier getDistanceSensorValue() {
    return () -> (-m_tof2.getDistance_Inches() + m_tof0.getDistance_Inches());
  }

  // this is the actual PID loop command
  private class MoveUsingDistanceSensorDifferenceInternal extends PIDCommand4905 {
    private DriveTrainBase m_driveTrain;
    // m_SDV is the current distance between tof0 and tof2
    private DoubleSupplier m_sensorDistanceValue;
    // target distance is the desired distance between tof0 and tof2
    // should be 0 for this command
    private double m_targetDistance = 0;
    private double m_maxOutput = 0;
    private boolean m_useCurrentHeading = false;

    /**
     * Creates a new MoveUsingDistanceSensor.
     */
    public MoveUsingDistanceSensorDifferenceInternal(DriveTrainBase drivetrain,
        double targetDistance, DoubleSupplier angle, double maxOutput, boolean useCurrentHeading) {
      super(
          // The controller that the command will use
          new PIDController4905SampleStop("MoveUsingDistanceSensorDifference"),
          // This should return the measurement
          getDistanceSensorValue(),
          // This should return the setpoint (can also be a constant)
          () -> targetDistance,
          // This uses the output
          output -> {
            // Use the output here
            drivetrain.moveUsingGyroStrafe(output, angle.getAsDouble(), false);
          });
      m_targetDistance = targetDistance;
      m_sensorDistanceValue = getDistanceSensorValue();
      m_driveTrain = drivetrain;
      m_maxOutput = maxOutput;
      m_useCurrentHeading = useCurrentHeading;
      // Configure additional PID options by calling `getController` here.
      addRequirements(drivetrain.getSubsystemBase());
    }

    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();
      Trace.getInstance().logCommandInfo(this, "Target Distance: " + m_targetDistance);
      getController().setP(pidConstantsConfig.getDouble("MoveUsingDistanceSensorDifference.Kp"));
      getController().setI(pidConstantsConfig.getDouble("MoveUsingDistanceSensorDifference.Ki"));
      getController().setD(pidConstantsConfig.getDouble("MoveUsingDistanceSensorDifference.Kd"));
      getController().setMinOutputToMove(
          pidConstantsConfig.getDouble("MoveUsingDistanceSensorDifference.minOutputToMove"));
      getController().setTolerance(
          pidConstantsConfig.getDouble("MoveUsingDistanceSensorDifference.positionTolerance"));
      // Allows anyone who calls MoveUsingDistanceSensor to override the maxOutput
      // defined in
      // config (if present)
      if (m_maxOutput != 0) {
        getController().setMaxOutput(m_maxOutput);
      } else if (pidConstantsConfig.hasPath("MoveUsingDistanceSensorDifference.maxOutput")) {
        getController().setMaxOutput(
            pidConstantsConfig.getDouble("MoveUsingDistanceSensorDifference.maxOutput"));
      }
      if (m_useCurrentHeading) {
        double heading = Robot.getInstance().getSensorsContainer().getGyro().getCompassHeading();
        super.setOutput(output -> {
          m_driveTrain.moveUsingGyro(output, 0, false, heading);
        });
      }
      Trace.getInstance().logCommandInfo(this,
          "Moving with DistanceSensorDifference to position: " + getSetpoint().getAsDouble());
      Trace.getInstance().logCommandInfo(this,
          "Starting DistanceSensorDifference position: " + m_sensorDistanceValue.getAsDouble());
    }

    public DoubleSupplier getSetpoint() {
      return () -> m_targetDistance;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return getController().atSetpoint();
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      m_driveTrain.stop();
      Trace.getInstance().logCommandInfo(this,
          "Ending Distance Sensor Difference Reading: " + m_sensorDistanceValue.getAsDouble());
      Trace.getInstance().logCommandInfo(this, "End of move command.tof2 distance: "
          + m_tof2.getDistance_Inches() + ", tof 0 distance: " + m_tof0.getDistance_Inches());
    }
  }
}
