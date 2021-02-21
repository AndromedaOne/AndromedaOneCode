package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Config4905;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class TwoDDriveTrainPathGenerator extends TwoDPathGenerator {

    DriveTrain m_driveTrain;

    public TwoDDriveTrainPathGenerator(String jsonFileName, DriveTrain driveTrain) {
        super(jsonFileName, Config4905.getConfig4905().getDrivetrainConfig());
        m_driveTrain = driveTrain;
    }

    @Override
    protected Pose2d getPos() {
        return m_driveTrain.getPose();
    }

    @Override
    protected DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return m_driveTrain.getWheelSpeeds();
    }

    @Override
    protected void tankDriveVolts(double left, double right) {
        m_driveTrain.tankDriveVolts(left, right);
    }

    @Override
    protected Subsystem getSubsystem() {
        return m_driveTrain;
    }

    @Override
    protected void resetOdometryToZero() {
        m_driveTrain.resetOdometry(new Pose2d(0,0, new Rotation2d(0)));
    }
    
}
