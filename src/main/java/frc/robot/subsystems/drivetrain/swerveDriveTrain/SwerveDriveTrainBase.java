package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.actuators.SwerveModule;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.sensors.gyro.Gyro4905;

public interface SwerveDriveTrainBase extends DriveTrainBase {

  public abstract void setModuleStates(SwerveModuleState[] desiredStates);

  public abstract SwerveModuleState[] getStates();

  public abstract SwerveModulePosition[] getPositions();

  public abstract Gyro4905 getGyro();
  public abstract SwerveDriveOdometry getSwerveOdometry();

  public abstract void setSwerveOdometry(SwerveDriveOdometry swerveOdometry);

  public abstract SwerveModule[] getmSwerveMods();

  public abstract void setmSwerveMods(SwerveModule[] mSwerveMods);

  public abstract Field2d getField();

  public abstract void setField(Field2d field);

}