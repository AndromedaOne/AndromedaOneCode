// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class CalibrateGyro extends Command {
  private Gyro4905 m_gyro4905;
  private DriveTrainBase m_driveTrainBase;

  public CalibrateGyro(Gyro4905 gyro4905, DriveTrainBase driveTrainBase) {
    addRequirements(driveTrainBase.getSubsystemBase());
    m_gyro4905 = gyro4905;
    m_driveTrainBase = driveTrainBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_gyro4905.calibrate();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrainBase.stop();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_gyro4905.getIsCalibrated();
  }

}
