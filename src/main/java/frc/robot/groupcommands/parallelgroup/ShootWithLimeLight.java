package frc.robot.groupcommands.parallelgroup;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShootWithLimeLight extends ParallelCommandGroup {

    public ShootWithLimeLight(ShooterBase shooter, FeederBase feeder, LimeLightCameraBase limeLight) {
        
    }
}