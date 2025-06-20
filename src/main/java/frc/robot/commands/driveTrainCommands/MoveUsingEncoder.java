/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

public class MoveUsingEncoder extends SequentialCommandGroup4905 {
  public MoveUsingEncoder(DriveTrainBase drivetrain, DoubleSupplier distance, double heading,
      double maxOutput, boolean useCurrentHeading, DoubleSupplier angle) {
    // COUNTER CLOCKWISE POSITIVE!!!!!
    addCommands(new SwerveDriveSetWheelsToAngle(drivetrain, angle), new MoveUsingEncoderInternal(
        drivetrain, angle, distance, heading, maxOutput, useCurrentHeading));
  }

  // Use this constructor to move the robot in the heading passed in
  public MoveUsingEncoder(DriveTrainBase drivetrain, DoubleSupplier distance, double heading,
      double maxOutput) {
    this(drivetrain, distance, heading, maxOutput, false, () -> 0);
  }

  // Use this constructor to move the robot in the direction it's already pointing
  public MoveUsingEncoder(DriveTrainBase driveTrain, DoubleSupplier distance, double maxOutput) {
    // COUNTER CLOCKWISE POSITIVE!!!!!
    this(driveTrain, distance, 0, maxOutput, true, () -> 0);
  }

  // Use this constructor to move the robot in the direction it's already pointing
  public MoveUsingEncoder(DriveTrainBase driveTrain, DoubleSupplier angle, DoubleSupplier distance,
      double maxOutput) {
    // COUNTER CLOCKWISE POSITIVE!!!!!
    this(driveTrain, distance, 0, maxOutput, true, angle);
  }

  private class MoveUsingEncoderInternal extends PIDCommand4905 {
    private DriveTrainBase m_driveTrain;
    private DoubleSupplier m_distance = () -> 0;
    private double m_maxOutput = 0;
    private DoubleSupplier m_angle = () -> 0;
    private double m_target = 0;
    private boolean m_useCurrentHeading = false;

    /**
     * Creates a new MoveUsingEncoder.
     */
    public MoveUsingEncoderInternal(DriveTrainBase drivetrain, DoubleSupplier angle,
        DoubleSupplier distance, double heading, double maxOutput, boolean useCurrentHeading) {
      super(
          // The controller that the command will use
          new PIDController4905SampleStop("MoveUsingEncoder"),
          // This should return the measurement
          () -> drivetrain.getRobotPositionInchesBasedOnAngle(angle.getAsDouble()),
          // This should return the setpoint (can also be a constant)
          () -> 0,
          // This uses the output
          output -> {
            // Use the output here
            drivetrain.moveUsingGyroStrafe(output, angle.getAsDouble(), false, heading);
          });
      double correctedAngle;
      correctedAngle = angle.getAsDouble();
      if (distance.getAsDouble() < 0) {
        correctedAngle = angle.getAsDouble() + 360;
      }

      /*
       * if ((correctedAngle >= 0 && correctedAngle < 180 && distance.getAsDouble() >
       * 0) || (correctedAngle <= 180 && distance.getAsDouble() < 0)) { m_distance =
       * () -> (distance.getAsDouble() * 7 / 6); } else { m_distance = distance; }
       */
      m_distance = distance;

      // m_distance = distance.getAsDouble();
      Trace.getInstance().logCommandInfo(this, "New Distance: " + m_distance.getAsDouble());
      m_driveTrain = drivetrain;
      m_maxOutput = maxOutput;
      // the setpoint can be changed on the fly by updating m_target
      setSetpoint(() -> m_target);
      m_useCurrentHeading = useCurrentHeading;
      m_angle = angle;
      // Configure additional PID options by calling `getController` here.
      addRequirements(drivetrain.getSubsystemBase());
    }

    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();
      setDistance(m_distance);
      Trace.getInstance().logCommandInfo(this, "Distance: " + m_distance.getAsDouble());
      Trace.getInstance().logCommandInfo(this, "Angle: " + m_angle.getAsDouble());
      getController().setP(pidConstantsConfig.getDouble("MoveUsingEncoder.Kp"));
      getController().setI(pidConstantsConfig.getDouble("MoveUsingEncoder.Ki"));
      getController().setD(pidConstantsConfig.getDouble("MoveUsingEncoder.Kd"));
      getController()
          .setMinOutputToMove(pidConstantsConfig.getDouble("MoveUsingEncoder.minOutputToMove"));
      getController()
          .setTolerance(pidConstantsConfig.getDouble("MoveUsingEncoder.positionTolerance"));
      getController().setIZone(pidConstantsConfig.getDouble("MoveUsingEncoder.iZone"));
      // Allows anyone who calls MoveUsingEncoder to override the maxOutput defined in
      // config (if present)
      if (m_maxOutput != 0) {
        getController().setMaxOutput(m_maxOutput);
      } else if (pidConstantsConfig.hasPath("MoveUsingEncoder.maxOutput")) {
        getController().setMaxOutput(pidConstantsConfig.getDouble("MoveUsingEncoder.maxOutput"));
      }
      if (m_useCurrentHeading) {
        double heading = Robot.getInstance().getSensorsContainer().getGyro().getCompassHeading();
        super.setOutput(output -> {
          m_driveTrain.moveUsingGyroStrafe(output, m_angle.getAsDouble(), false, heading);
        });
      }
      setSetpoint(() -> m_target);
      Trace.getInstance().logCommandInfo(this,
          "Moving with encoder to position: " + getSetpoint().getAsDouble());
      Trace.getInstance().logCommandInfo(this, "Starting encoder position: "
          + m_driveTrain.getRobotPositionInchesBasedOnAngle(m_angle.getAsDouble()));
      m_driveTrain.disableAccelerationLimiting();
      SmartDashboard.putBoolean("MoveUsingEncoder OnTarget ", false);
    }

    public void setDistance(DoubleSupplier distance) {
      m_distance = distance;
      m_target = m_driveTrain.getRobotPositionInchesBasedOnAngle(m_angle.getAsDouble())
          + m_distance.getAsDouble();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return getController().atSetpoint();
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      m_driveTrain.stop();
      Trace.getInstance().logCommandInfo(this, "Ending position: "
          + m_driveTrain.getRobotPositionInchesBasedOnAngle(m_angle.getAsDouble()));
      m_driveTrain.enableAccelerationLimiting();
      SmartDashboard.putBoolean("MoveUsingEncoder OnTarget ", true);
    }
  }
}
