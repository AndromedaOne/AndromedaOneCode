package frc.robot.pathgeneration.pathgenerators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Config4905;
import frc.robot.pathgeneration.waypoints.WaypointWithHeading;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.pidcontroller.TracingPIDController;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class DriveTrainPathGeneration2d extends PathGeneration2d {
    private Config m_drivetrainConfig;
    private double m_maxSpeedMetersPerSecond;
    private double m_maxAccelerationMetersPerSecondSquared;
    private DifferentialDriveKinematics m_driveKinematics;
    private DifferentialDriveVoltageConstraint m_autoVoltageConstraint;
    private double m_trackWidthMeters;
    private double m_sVolts;
    private double m_vVoltSecondsPerMeter;
    private double m_aVoltSecondsSquaredPerMeter;
    private DriveTrain m_driveTrain;
    private double m_ramseteB;
    private double m_ramseteZeta;
    private double m_pDriveVel;
    public DriveTrainPathGeneration2d(WaypointsBase waypoints, WaypointWithHeading initialPoint, WaypointWithHeading finalPoint, DriveTrain driveTrain){
        super(waypoints, initialPoint, finalPoint);
        m_driveTrain = driveTrain;
        m_drivetrainConfig= Config4905.getConfig4905().getDrivetrainConfig();
        m_maxSpeedMetersPerSecond = m_drivetrainConfig.getDouble("pathplanningconstants.kMaxSpeedMetersPerSecond");
        m_maxAccelerationMetersPerSecondSquared = m_drivetrainConfig.getDouble("pathplanningconstants.kMaxAccelerationMetersPerSecondSquared");
        m_trackWidthMeters = m_drivetrainConfig.getDouble("pathplanningconstants.kTrackwidthMeter");
        m_sVolts = m_drivetrainConfig.getDouble("pathplanningconstants.ksVolts");
        m_vVoltSecondsPerMeter = m_drivetrainConfig.getDouble("pathplanningconstants.kvVoltSecondsPerMeter");
        m_aVoltSecondsSquaredPerMeter = m_drivetrainConfig.getDouble("pathplanningconstants.kaVoltSecondsSquaredPerMeter");
        m_driveKinematics = new DifferentialDriveKinematics(m_trackWidthMeters);
        m_autoVoltageConstraint = new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(m_sVolts, m_vVoltSecondsPerMeter, m_aVoltSecondsSquaredPerMeter), m_driveKinematics,10);
        m_ramseteB = m_drivetrainConfig.getDouble("pathplanningconstants.kRamseteB");
        m_ramseteZeta = m_drivetrainConfig.getDouble("pathplanningconstants.kRamseteZeta");
        m_pDriveVel = m_drivetrainConfig.getDouble("pathplanningconstants.kPDriveVel");
    }


    @Override
    protected RamseteCommand getRamsete(Trajectory trajectory) {
        return new RamseteCommand(trajectory, m_driveTrain::getPose,
        new RamseteController(m_ramseteB, m_ramseteZeta),
        new SimpleMotorFeedforward(m_sVolts, m_vVoltSecondsPerMeter, m_aVoltSecondsSquaredPerMeter), m_driveKinematics,
        m_driveTrain::getWheelSpeeds,
        // the following two pidcontrollers are intentionally reversed in order so that
        // positive angle from the gyro correspons to rotating clockwise looking down on
        // the robot.
        new TracingPIDController("RightVelocity", m_pDriveVel, 0.0, 0.0),
        new TracingPIDController("LeftVelocity", m_pDriveVel, 0.0, 0.0),

        // RamseteCommand passes volts to the callback
        m_driveTrain::tankDriveVolts, m_driveTrain);
    }

    @Override
    protected TrajectoryConfig getConfig() {
        TrajectoryConfig config = new TrajectoryConfig(m_maxSpeedMetersPerSecond, m_maxAccelerationMetersPerSecondSquared)
        .setKinematics(m_driveKinematics).addConstraint(m_autoVoltageConstraint);
        return config;
    }

    @Override
    protected void resetOdometry(Pose2d pose) {
        m_driveTrain.resetOdometry(pose);
    }
    
}
