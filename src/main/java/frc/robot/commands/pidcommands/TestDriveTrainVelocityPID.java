package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class TestDriveTrainVelocityPID extends PIDCommand4905 {

  private static DriveTrain m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();
  private SimpleMotorFeedforward m_feedForward = new SimpleMotorFeedforward(0.1, 0.1);
  private static double m_setPoint = 1000;
  private static double m_feedForwardCalculated = 0;

  public TestDriveTrainVelocityPID() {
    super(
        // The controller that the command will use
        new PIDController4905("TestShooterPID", 1, 0, 0, 0),
        // This should return the measurement
        m_driveTrain::getRobotVelocityRPM,
        // This should return the setpoint (can also be a constant)
        m_setPoint,
        // This uses the output
        output -> {
          // Use the output here
          m_driveTrain.move(output + m_feedForwardCalculated, 0, false);
        });
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart("TestDriveTrainVelocityPID");
    super.initialize();
  }

  @Override
  public void execute() {
    super.execute();
    m_feedForwardCalculated = m_feedForward.calculate(m_setPoint);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupt) {
    Trace.getInstance().logCommandStop("TestDriveTrainVelocityPID");
  }
}
