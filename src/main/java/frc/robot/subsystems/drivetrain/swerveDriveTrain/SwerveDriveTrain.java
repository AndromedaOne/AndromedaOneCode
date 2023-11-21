package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SwerveModule;
import frc.robot.lib.config.Constants;

public class SwerveDriveTrain extends SubsystemBase implements SwerveDriveTrainBase {

  private final Pigeon2 gyro;
  private SwerveDriveOdometry swerveOdometry;
  private SwerveModule[] mSwerveMods;
  private Field2d field;

  public SwerveDriveTrain() {
    gyro = new Pigeon2(Constants.Swerve.pigeonID);
    gyro.configFactoryDefault();
    zeroGyro();

    swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), null);

    mSwerveMods = new SwerveModule[] { new SwerveModule(0, Constants.Swerve.Mod0.constants),
        new SwerveModule(1, Constants.Swerve.Mod1.constants),
        new SwerveModule(2, Constants.Swerve.Mod2.constants),
        new SwerveModule(3, Constants.Swerve.Mod3.constants) };
    field = new Field2d();
    SmartDashboard.putData("Field", field);
  }
  /* All of the following code is only here so the program will build */

  public void drive(Translation2d translation, Double rotation, boolean fieldRelative,
      boolean isOpenLoop) {

  }

  public void setModuleStates(SwerveModuleState[] desiredStates) {

  }

  public Pose2d getPose() {
    return swerveOdometry.getPoseMeters();

  }

  public void resetOdometry(Pose2d pose) {

  }

  public SwerveModuleState[] getStates() {
    return null;

  }

  public SwerveModulePosition[] getPositions() {
    return null;

  }

  public void zeroGyro() {

  }

  public Rotation2d getYaw() {
    return null;

  }

  // @Override
  public void periodic() {

  }

  // @Override
  public SubsystemBase getSubsystemBase() {
    return null;

  }

  // @Override
  public void setDefaultCommand(CommandBase command) {

  }

  // @Override
  public void init() {

  }

  public Pigeon2 getGyro() {
    return null;

  }

  public SwerveDriveOdometry getSwerveOdometry() {
    return null;

  }

  public void getSwerveOdometry(SwerveDriveOdometry swerveOdometry) {

  }

  public SwerveModule[] getmSwerveMods() {
    return null;

  }

  public void getmSwerveMods(SwerveModule[] mSwerveMods) {

  }

  public Field2d getField() {
    return null;

  }

  public void setField(Field2d field) {

  }

}
