package frc.robot.commands.driveTrainCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveConstarts;
import frc.robot.telemetries.Trace;

public class SwerveTeleOpCommand extends CommandBase {
  private DriveTrainBase m_swerveDrive;
  DriveController m_driveController;
  private BooleanSupplier m_robotCentricSup;

  private SlewRateLimiter translationLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter strafeLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter rotationlimiter = new SlewRateLimiter(3.0);
// private DriveTrain m_driveTrain =
// Robot.getInstance().getSubsystemContainer().getDriveTrain();

  public SwerveTeleOpCommand(BooleanSupplier robotCentricSup) {

    m_swerveDrive = Robot.getInstance().getSubsystemsContainer().getDriveTrain();

    addRequirements(m_swerveDrive.getSubsystemBase());

    m_driveController = Robot.getInstance().getOIContainer().getDriveController();

    m_robotCentricSup = robotCentricSup;
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void execute() {
    double translationAxis = m_driveController.getSwerveDriveTrainTranslationAxis();
    double translationLim = translationLimiter.calculate(translationAxis);
    double strafeAxis = m_driveController.getSwerveDriveTrainStrafeAxis();
    double strafeLim = strafeLimiter.calculate(strafeAxis);
    double rotationAxis = m_driveController.getSwerveDriveTrainRotationAxis();
    double rotationLim = rotationlimiter.calculate(rotationAxis);

    m_swerveDrive.move(
        new Translation2d(translationLim, strafeLim).times(SwerveDriveConstarts.Swerve.maxSpeed),
        rotationLim * SwerveDriveConstarts.Swerve.maxAngularVelocity,
        !m_robotCentricSup.getAsBoolean(), true);
  }

  @Override
  public void end(boolean interupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
