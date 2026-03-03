package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Config4905;
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
    m_command = new TurnToCompassHeading(getFieldElementAngle());
    m_objPose = objPose;
    addCommands(m_command);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    Trace.getInstance().logCommandInfo(this, "Starting turn to field element command with angle of "
        + getFieldElementAngle().getAsDouble());
    Trace.getInstance().logCommandInfo(this, "Current robot angle of "
        + m_driveTrain.getPose().getRotation().getDegrees() + " from -180 to 180");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {

  }

  private DoubleSupplier getFieldElementAngle() {
    Pose2d robotPose = m_driveTrain.currentPose2d();
    double offset = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getDouble("robotToFieldElementAngleOffset");
    double a = m_objPose.getX() - robotPose.getX();
    double b = m_objPose.getY() - robotPose.getY();
    double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    double angle = (Math.toDegrees(Math.asin(a / c))) - 90.0; // 90 normalizes the angle;
    // adjusting the angle based on being on the left side of the field
    if (b < 0) {
      angle = angle + offset;
      angle = (-1) * angle;
    } else {
      angle = angle - offset;
    }
    double middleAngle = angle;
    return () -> middleAngle;
  }

}
