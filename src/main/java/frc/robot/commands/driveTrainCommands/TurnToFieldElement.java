package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Robot;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

//This was entirely taken from topGun so there are definitely problems
public class TurnToFieldElement extends ParallelCommandGroup4905 {

  private DriveTrainBase m_driveTrain;
  private TurnToCompassHeading m_command;
  private Pose2d m_objPose;

  public TurnToFieldElement(Pose2d objPose) {
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    m_objPose = objPose;
    m_command = new TurnToCompassHeading(
        m_driveTrain.getRobotToFieldElementAngleSupplier(m_objPose));
    addCommands(m_command);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_objPose == null) {
      // yucky way to fix issue of the hub pose not being correctly passed in
      // we can make this work better LATER we are rushing too hard
      m_objPose = Robot.getInstance().getFieldConstants().getHubPose();
    }
    Trace.getInstance().logCommandInfo(this, "Starting turn to field element command with angle of "
        + m_driveTrain.getRobotToFieldElementAngle(m_objPose));
    Trace.getInstance().logCommandInfo(this, "Current robot angle of "
        + m_driveTrain.getPose().getRotation().getDegrees() + " from -180 to 180");
    m_command.setSetpoint(m_driveTrain.getRobotToFieldElementAngleSupplier(m_objPose));
    Trace.getInstance().logCommandInfo(this,
        "current setpoint of " + m_command.getSetpoint().getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {

  }

}
