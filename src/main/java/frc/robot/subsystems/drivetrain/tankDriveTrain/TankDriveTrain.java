/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain.tankDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.subsystems.SubsystemInterface;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;

public interface TankDriveTrain extends DriveTrainBase {

  public abstract DifferentialDriveWheelSpeeds getWheelSpeeds();

  public abstract void tankDriveVolts(double leftVolts, double rightVolts);

  public abstract double getLeftRateMetersPerSecond();

  public abstract double getRightRateMetersPerSecond();

}
