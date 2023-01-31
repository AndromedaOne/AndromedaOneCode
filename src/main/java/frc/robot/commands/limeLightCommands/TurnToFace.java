// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.limeLightCommands;

import java.util.function.DoubleSupplier;

import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class TurnToFace extends PIDCommand4905 {
  SensorsContainer m_sensorcontainer = Robot.getInstance().getSensorsContainer();
  LimeLightCameraBase m_limelight;

  public TurnToFace(DoubleSupplier sensor) {
    // controller used for pid command
    super(new PIDController4905("TurnToFace", 0.0, 0.0, 0.0, 0.0),
        // this variable returns the value used for the pid loop
        sensor,
        // set point
        0,
        // uses the output
        output -> {
          Robot.getInstance().getSubsystemsContainer().getDrivetrain().moveUsingGyro(0.0, output,
              false, 0.0);
        });

  }

  @Override
  public void end(boolean interrupted) {
    // TODO Auto-generated method stub
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
    Trace.getInstance().logCommandInfo(this, "turn off limelightl");
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    // TODO Auto-generated method stub
    super.initialize();
    m_limelight = m_sensorcontainer.getLimeLight();
    Trace.getInstance().logCommandInfo(this, "initialize limelightd");
  }

  @Override
  public boolean isFinished() {
    // TODO Auto-generated method stub
    return super.isFinished();
  }

}
