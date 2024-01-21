package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.actuators.SwerveModule;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public interface SwerveDriveTrainBase extends DriveTrainBase {

  public abstract SwerveModuleState[] getStates();

  public abstract SwerveModulePosition[] getPositions();

  public abstract SwerveDriveOdometry getSwerveOdometry();

  public abstract void setSwerveOdometry(SwerveDriveOdometry swerveOdometry);

  public abstract SwerveModule[] getSwerveMods();

  public abstract void setSwerveMods(SwerveModule[] swerveMods);

  public abstract Field2d getField();

  public abstract void setField(Field2d field);

}