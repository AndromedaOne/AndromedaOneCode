package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SwerveModule;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.SubsystemInterface;

public interface SwerveDriveTrainBase extends SubsystemInterface {

  public abstract void drive(Translation2d translation, double rotation, boolean fieldRelative,
      boolean isOpenLoop);

  public abstract void setModuleStates(SwerveModuleState[] desiredStates);

  public abstract Pose2d getPose();

  public abstract void resetOdometry(Pose2d pose);

  public abstract SwerveModuleState[] getStates();

  public abstract SwerveModulePosition[] getPositions();

  public abstract Rotation2d getYaw();

  // @Override
  public abstract void periodic();

  // @Override
  public abstract SubsystemBase getSubsystemBase();

  // @Override
  public abstract void setDefaultCommand(CommandBase command);

  // @Override
  public abstract void init();

  public abstract Gyro4905 getGyro();

  public abstract SwerveDriveOdometry getSwerveOdometry();

  public abstract void setSwerveOdometry(SwerveDriveOdometry swerveOdometry);

  public abstract SwerveModule[] getmSwerveMods();

  public abstract void setmSwerveMods(SwerveModule[] mSwerveMods);

  public abstract Field2d getField();

  public abstract void setField(Field2d field);

}