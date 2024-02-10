// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.utils.AllianceConfig;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class EmergencyBackup extends SequentialCommandGroup4905 {
  private class EmergencyBackupConfig {
    double m_waypoint1;
  }

  EmergencyBackupConfig emergencyBackupConfigRed = new EmergencyBackupConfig();
  EmergencyBackupConfig emergencyBackupConfigBlue = new EmergencyBackupConfig();
  DriveTrainBase m_driveTrain;

  public EmergencyBackup() {
    // Both
    // Positioned by the Amp
    // Move foward out of the starting zone and park
    // wait for teleop
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    emergencyBackupConfigRed.m_waypoint1 = redConfig.getDouble("EmergencyBackup.WayPoint1");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    emergencyBackupConfigBlue.m_waypoint1 = blueConfig.getDouble("EmergencyBackup.WayPoint1");

  }

  public void additionalInitialize() {
    EmergencyBackupConfig config;
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      config = emergencyBackupConfigBlue;
    } else {
      config = emergencyBackupConfigRed;
    }
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup4905(
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint1, 0.5)));
  }
}
