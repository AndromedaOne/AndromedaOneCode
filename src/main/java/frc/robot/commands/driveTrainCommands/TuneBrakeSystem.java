// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.actuators.HitecHS322HDpositionalServoMotor;
import frc.robot.actuators.ServoMotorPositional;
import frc.robot.telemetries.Trace;

public class TuneBrakeSystem extends CommandBase {
  /** Creates a new TuneBrakeSystem. */
  ServoMotorPositional m_rightServoMotor;
  ServoMotorPositional m_leftServoMotor;
  Config m_driveTrainConfig = Config4905.getConfig4905().getDrivetrainConfig();

  public TuneBrakeSystem() {
    // send into the servomotor constructor the driveTrain configuration file as
    // well as the
    // motor we are using. In this case for my test purpose I am only hooked up to
    // the
    // right servo motor but future tests if we keep this code will include both the
    // right
    // and left brake motors.
    // m_rightServoMotor = new HitecHS322HDpositionalServoMotor(m_driveTrainConfig,
    // "rightbrakeservomotor");
    // throw on the dashboard a default value
    // SmartDashboard.putNumber("Right Brake Speed", 0.0);
    m_leftServoMotor = new HitecHS322HDpositionalServoMotor(m_driveTrainConfig,
        "leftbrakeservomotor");
    // throw on the dashboard a default value
    SmartDashboard.putNumber("Left Brake Speed", 0.0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    // update the smart dashboard with what the previous brake speed was,
    // retrieve from the dashboard what the user entered for the new speed to test
    // after test get the current speed of the servo
    // SmartDashboard.putNumber("Right Previous Brake Speed",
    // m_rightServoMotor.get());
    // double speed = SmartDashboard.getNumber("Right Brake Speed", 0.0);
    // m_rightServoMotor.set(speed);
    // SmartDashboard.putNumber("Right Current Brake Speed",
    // m_rightServoMotor.get());

    SmartDashboard.putNumber("Left Previous Brake Speed", m_leftServoMotor.get());
    double leftSpeed = SmartDashboard.getNumber("Left Brake Speed", 0.0);
    m_leftServoMotor.set(leftSpeed);
    SmartDashboard.putNumber("Left Current Brake Speed", m_leftServoMotor.get());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
