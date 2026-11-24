// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.sensors.targetDetection.TargetDetectionBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MoveUsingTargetDetectionTester extends Command {
  private DriveTrainBase m_driveTrain;
  private TargetDetectionBase m_targetDetection;
  private double m_yaw = 0;
  private double m_maxOutput = .5;

  /** Creates a new MoveUsingTargetDetectionTester. */
  public MoveUsingTargetDetectionTester(DriveTrainBase drivetrain,
      TargetDetectionBase targetDetection) {
    m_driveTrain = drivetrain;
    m_targetDetection = targetDetection;
    SmartDashboard.putNumber("TargetDetectionTester/TargetYaw", m_yaw);
    SmartDashboard.putNumber("TargetDetectionTester/MaxOutput", m_maxOutput);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_yaw = SmartDashboard.getNumber("TargetDetectionTester/TargetYaw", 0);
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
        new MoveUsingTargetDetection(m_driveTrain, m_yaw, m_maxOutput, m_targetDetection)

    ));
    Trace.getInstance().logCommandInfo(this, "Moving to Target Yaw: " + m_yaw);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
