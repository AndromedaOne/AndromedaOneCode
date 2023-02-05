// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class BalanceRobot extends PIDCommand4905 {
  private DriveTrain m_driveTrain;
  private double m_maxOutput = 0;
  private Gyro4905 m_gyro4905;
  private Config m_constantConfig = Config4905.getConfig4905().getCommandConstantsConfig();
  private double m_kP = m_constantConfig.getDouble("BalanceRobot.Kp");

  /** Creates a new BalanceRobot. */
  public BalanceRobot(DriveTrain driveTrain, double maxOutput) {
    super(new PIDController4905SampleStop("BalanceRobot", 0, 0, 0, 0),

        Robot.getInstance().getSensorsContainer().getGyro().getYangleDoubleSupplier(), 0,
        output -> {
          driveTrain.moveUsingGyro(-output, 0, false, 0);
        }, driveTrain);
    m_driveTrain = driveTrain;
    m_maxOutput = maxOutput;
    m_gyro4905 = Robot.getInstance().getSensorsContainer().getGyro();
    getController().setP(m_constantConfig.getDouble("BalanceRobot.Kp"));
    getController().setI(m_constantConfig.getDouble("BalanceRobot.Ki"));
    getController().setD(m_constantConfig.getDouble("BalanceRobot.Kd"));
    getController().setMinOutputToMove(m_constantConfig.getDouble("BalanceRobot.minOutputToMove"));
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    getController().setTolerance(0.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrain.stop();
    Trace.getInstance().logCommandStop(this);
    Trace.getInstance().logCommandInfo(this, "ending Y value: " + m_gyro4905.getYAngle());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
