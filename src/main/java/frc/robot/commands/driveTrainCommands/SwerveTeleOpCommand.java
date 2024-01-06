package frc.robot.commands.driveTrainCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.lib.config.Constants;
import frc.robot.oi.DriveController;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class SwerveTeleOpCommand extends CommandBase {
  private DriveTrainBase m_swerveDrive;
  private DoubleSupplier translationSup;
  private DoubleSupplier strafeSup;
  private DoubleSupplier rotationSup;
  private BooleanSupplier robotCentricSup;

  private SlewRateLimiter translationLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter strafeLimiter = new SlewRateLimiter(3.0);
  private SlewRateLimiter rotationlimiter = new SlewRateLimiter(3.0);
// private DriveTrain m_driveTrain =
// Robot.getInstance().getSubsystemContainer().getDriveTrain();

  public SwerveTeleOpCommand(BooleanSupplier robotCentricSup) {

    m_swerveDrive = Robot.getInstance().getSubsystemsContainer().getDriveTrain();

    addRequirements(m_swerveDrive.getSubsystemBase());

    /* Drive Controls */
    DriveController m_driveController = Robot.getInstance().getOIContainer().getDriveController();

    // XboxController.Axis.kLeftY.value;

    double translationAxis = m_driveController.getSwerveDriveTrainTranslationAxis();
    // XboxController.Axis.kLeftX.value;

    double strafeAxis = m_driveController.getSwerveDriveTrainStrafeAxis();

    // XboxController.Axis.kRIghtX.value;
    double rotationAxis = m_driveController.getSwerveDriveTrainRotationAxis();

    this.translationSup = () -> translationAxis;
    this.strafeSup = () -> strafeAxis;
    this.rotationSup = () -> rotationAxis;
    this.robotCentricSup = robotCentricSup;
  }

  @Override
  public void execute() {
    /* Get Values, Deadband */
    double translationVal = translationLimiter.calculate(
        MathUtil.applyDeadband(translationSup.getAsDouble(), Constants.Swerve.stickDeadband));
    double strafeVal = strafeLimiter
        .calculate(MathUtil.applyDeadband(strafeSup.getAsDouble(), Constants.Swerve.stickDeadband));
    double rotationVal = rotationlimiter.calculate(
        MathUtil.applyDeadband(rotationSup.getAsDouble(), Constants.Swerve.stickDeadband));

    /* Drive */
    m_swerveDrive.move(
        new Translation2d(translationVal, strafeVal).times(Constants.Swerve.maxSpeed),
        rotationVal * Constants.Swerve.maxAngularVelocity, !robotCentricSup.getAsBoolean(), true);
  }
}
