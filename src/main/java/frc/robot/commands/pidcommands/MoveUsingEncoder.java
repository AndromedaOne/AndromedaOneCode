/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MoveUsingEncoder extends PIDCommand4905 {
  private DriveTrain m_driveTrain;
  private double m_distance = 0;
  private double m_target;

  /**
   * Creates a new MoveUsingEncoder.
   */
  public MoveUsingEncoder(DriveTrain drivetrain, double distance) {
    super(
        // The controller that the command will use
        new PIDController4905("MoveUsingEncoder", 0, 0, 0, 0),
        // This should return the measurement
        drivetrain::getRobotPositionInches,
        // This should return the setpoint (can also be a constant)
        0,
        // This uses the output
        output -> {
          // Use the output here
          drivetrain.moveUsingGyro(output, 0, true, false);
        });
    m_distance = distance;
    m_setpoint = this::getSetpoint;
    m_driveTrain = drivetrain;
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    addRequirements(drivetrain);
  }

  public void initialize() {
    Config pidConstantsConfig = Config4905.getConfig4905().getPidConstantsConfig();
    super.initialize();
    Trace.getInstance().logCommandStart(this);
    setDistance(m_distance);

    getController().setP(pidConstantsConfig.getDouble("MoveUsingEncoder.Kp"));
    getController().setI(pidConstantsConfig.getDouble("MoveUsingEncoder.Ki"));
    getController().setD(pidConstantsConfig.getDouble("MoveUsingEncoder.Kd"));
    getController().setMinOutputToMove(pidConstantsConfig.getDouble("MoveUsingEncoder.minOutputToMove"));
    getController().setTolerance(pidConstantsConfig.getDouble("MoveUsingEncoder.positionTolerance"),
        pidConstantsConfig.getDouble("MoveUsingEncoder.velocityTolerance"));
    if (pidConstantsConfig.hasPath("MoveUsingEncoder.maxOutput")) {
      getController().setMaxOutput(pidConstantsConfig.getDouble("MoveUsingEncoder.maxOutput"));
    }
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
    Trace.getInstance().logCommandInfo(this, "Position = " + m_driveTrain.getRobotPositionInches());
  }
}
