package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class TestDriveTrainVelocityPID extends PIDCommand {

  private static DriveTrain m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();
  private SimpleMotorFeedforward m_feedForward = new SimpleMotorFeedforward(1, 0);
  private static double m_setPoint = 1000;
  private static double m_feedForwardCalculated = 0;

  public TestDriveTrainVelocityPID() {
    super(
        // The controller that the command will use
        new PIDController4905(1, 0, 0, 0),
        // This should return the measurement
        m_driveTrain::getRobotVelocityInches,
        // This should return the setpoint (can also be a constant)
        m_setPoint,
        // This uses the output
        output -> {
          // Use the output here
          m_driveTrain.move(output + m_feedForwardCalculated, 0, false);
        });
  }

  @Override
  public void execute() {
    super.execute();
    SmartDashboard.putNumber("DriveTrain_Velocity", m_driveTrain.getRobotVelocityInches());
    m_feedForwardCalculated = m_feedForward.calculate(m_setPoint);
  }

}
