/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrainCommands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MoveUsingEncoder extends PIDCommand4905 {
  private DriveTrain m_driveTrain;
  private double m_distance = 0;
  private double m_maxOutput = 0;
  private double m_target;
  private boolean m_useCurrentHeading = false;

  /**
   * Creates a new MoveUsingEncoder.
   */
  public MoveUsingEncoder(DriveTrain drivetrain, double distance, double heading, double maxOutput,
      boolean useCurrentHeading) {
    super(
        // The controller that the command will use
        new PIDController4905SampleStop("MoveUsingEncoder", 0, 0, 0, 0),
        // This should return the measurement
        drivetrain::getRobotPositionInches,
        // This should return the setpoint (can also be a constant)
        0,
        // This uses the output
        output -> {
          // Use the output here
          drivetrain.moveUsingGyro(output, 0, false, heading);
        });
    m_distance = distance;
    m_setpoint = this::getSetpoint;
    m_driveTrain = drivetrain;
    m_maxOutput = maxOutput;
    m_useCurrentHeading = useCurrentHeading;
    // Configure additional PID options by calling `getController` here.
    addRequirements(drivetrain);
  }

  // Use this constructor to move the robot in the heading passed in
  public MoveUsingEncoder(DriveTrain drivetrain, double distance, double heading,
      double maxOutput) {
    this(drivetrain, distance, heading, maxOutput, false);
  }

  // Use this constructor to move the robot in the direction it's already pointing
  public MoveUsingEncoder(DriveTrain driveTrain, double distance, double maxOutput) {
    this(driveTrain, distance, 0, maxOutput, true);
  }

  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
    super.initialize();
    setDistance(m_distance);

    getController().setP(pidConstantsConfig.getDouble("MoveUsingEncoder.Kp"));
    getController().setI(pidConstantsConfig.getDouble("MoveUsingEncoder.Ki"));
    getController().setD(pidConstantsConfig.getDouble("MoveUsingEncoder.Kd"));
    getController()
        .setMinOutputToMove(pidConstantsConfig.getDouble("MoveUsingEncoder.minOutputToMove"));
    getController()
        .setTolerance(pidConstantsConfig.getDouble("MoveUsingEncoder.positionTolerance"));
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
        m_driveTrain.moveUsingGyro(output, 0, false, heading);
      });
    }
    Trace.getInstance().logCommandInfo(this, "Moving with encoder to position: " + getSetpoint());

  }

  public double getSetpoint() {
    return m_target;
  }

  public void setDistance(double distance) {
    m_distance = distance;
    m_target = m_driveTrain.getRobotPositionInches() + m_distance;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  public void end(boolean interrupted) {
    super.end(interrupted);
    m_driveTrain.stop();
    Trace.getInstance().logCommandStop(this);
  }
}
