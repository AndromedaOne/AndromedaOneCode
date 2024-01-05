package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import com.ctre.phoenix.sensors.Pigeon2;

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
import frc.robot.subsystems.SubsystemInterface;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public interface SwerveDriveTrainBase extends DriveTrainBase {

  public abstract void setModuleStates(SwerveModuleState[] desiredStates);

  public abstract SwerveModuleState[] getStates();

  public abstract SwerveModulePosition[] getPositions();

  public abstract SwerveDriveOdometry getSwerveOdometry();

  public abstract void setSwerveOdometry(SwerveDriveOdometry swerveOdometry);

  public abstract SwerveModule[] getmSwerveMods();

  public abstract void setmSwerveMods(SwerveModule[] mSwerveMods);

  public abstract Field2d getField();

  public abstract void setField(Field2d field);

}