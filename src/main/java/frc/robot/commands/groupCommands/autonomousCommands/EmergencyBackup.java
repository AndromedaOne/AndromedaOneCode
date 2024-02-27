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
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.DrivePositionCommand;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
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
  EmergencyBackupConfigSupplier m_configSupplier = new EmergencyBackupConfigSupplier();
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;

  public EmergencyBackup() {
    // Both
    // Positioned by the Amp
    // Move foward out of the starting zone and park
    // wait for teleop
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    emergencyBackupConfigRed.m_waypoint1 = redConfig.getDouble("EmergencyBackup.WayPoint1");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    emergencyBackupConfigBlue.m_waypoint1 = blueConfig.getDouble("EmergencyBackup.WayPoint1");
    m_configSupplier.setConfig(emergencyBackupConfigRed);
    addCommands(
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1)),
        new DrivePositionCommand(m_endEffector, m_armRotate));
  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_configSupplier.setConfig(emergencyBackupConfigBlue);
    } else {
      m_configSupplier.setConfig(emergencyBackupConfigRed);
    }
    CommandScheduler.getInstance().schedule(
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1)),
        new DrivePositionCommand(m_endEffector, m_armRotate));
  }

  private class EmergencyBackupConfigSupplier {
    EmergencyBackupConfig m_config;

    public void setConfig(EmergencyBackupConfig config) {
      m_config = config;
    }

    public EmergencyBackupConfig getConfig() {
      return m_config;
    }
  }
}
