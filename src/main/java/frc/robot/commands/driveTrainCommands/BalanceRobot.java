// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class BalanceRobot extends CommandBase {
  DriveTrain m_driveTrain;
  int m_counter = 0;
  double m_maxOutput = 0;
  Gyro4905 m_gyro4905;

  /** Creates a new BalanceRobot. */
  public BalanceRobot(DriveTrain driveTrain, double maxOutput) {
    addRequirements(driveTrain);
    m_driveTrain = driveTrain;
    m_maxOutput = maxOutput;
    m_gyro4905 = Robot.getInstance().getSensorsContainer().getGyro();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_counter = 0;
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_gyro4905.getYAngle() > 0.1) {
      m_driveTrain.moveUsingGyro(m_maxOutput, 0, false, m_gyro4905.getCompassHeading());
    } else if (m_gyro4905.getXAngle() < -0.1) {
      m_driveTrain.moveUsingGyro(-m_maxOutput, 0, false, m_gyro4905.getCompassHeading());
    } else {
      m_driveTrain.stop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrain.stop();
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if ((m_gyro4905.getYAngle() < 0.5) && (m_gyro4905.getYAngle() > -0.5)) {
      ++m_counter;
      if (m_counter > 5) {
        return true;
      }
    } else {
      m_counter = 0;
    }
    return false;
  }
}
