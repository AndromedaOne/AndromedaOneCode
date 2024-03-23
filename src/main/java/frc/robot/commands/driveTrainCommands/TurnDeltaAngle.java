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
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TurnDeltaAngle extends PIDCommand4905 {
  private double m_deltaTurnAngle;
  private double m_targetAngle;
  Config pidConfig = Config4905.getConfig4905().getCommandConstantsConfig();
  private Gyro4905 m_gyro;
  private DriveTrainBase m_driveTrain;

  /**
   * Creates a new TurnDeltaAngle.
   */
  public TurnDeltaAngle(double deltaTurnAngle) {
    super(
        // The controller that the command will use
        new PIDController4905SampleStop("TurnDeltaAngle"),
        // This should return the measurement
        Robot.getInstance().getSensorsContainer().getGyro().getZangleDoubleSupplier(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          Robot.getInstance().getSubsystemsContainer().getDriveTrain().move(0, output, false);
        });
    setSetpoint(() -> m_targetAngle);
    addRequirements(m_driveTrain.getSubsystemBase());
    // Configure additional PID options by calling `getController` here.
    m_deltaTurnAngle = deltaTurnAngle;
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    getController().setP(pidConfig.getDouble("GyroPIDCommands.TurningPTerm"));
    getController().setI(pidConfig.getDouble("GyroPIDCommands.TurningITerm"));
    getController().setD(pidConfig.getDouble("GyroPIDCommands.TurningDTerm"));
    getController().setMinOutputToMove(pidConfig.getDouble("GyroPIDCommands.minOutputToMove"));
    getController().setTolerance(pidConfig.getDouble("GyroPIDCommands.positionTolerance"));
  }

  @Override
  public void initialize() {
    super.initialize();
    m_targetAngle = m_gyro.getZAngle() + m_deltaTurnAngle;
    double angle = m_gyro.getZAngle();
    Trace.getInstance().logCommandInfo(this, "Starting Angle:" + angle);
    Trace.getInstance().logCommandInfo(this, "Setpoint: " + getSetpoint().getAsDouble());
    m_driveTrain.disableAccelerationLimiting();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandInfo(this, "Finish Angle" + m_gyro.getZAngle());
    m_driveTrain.enableAccelerationLimiting();
  }
}
