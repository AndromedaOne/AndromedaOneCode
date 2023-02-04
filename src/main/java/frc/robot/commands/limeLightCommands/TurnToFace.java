// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.limeLightCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.telemetries.Trace;

// comment because gitextentions is upset
/** Add your docs here. */
public class TurnToFace extends PIDCommand4905 {
  protected DoubleSupplier m_sensor;
  SensorsContainer m_sensorcontainer = Robot.getInstance().getSensorsContainer();
  protected static Config m_conf = Config4905.getConfig4905().getCommandConstantsConfig();
  protected Config m_conf2 = Config4905.getConfig4905().getSensorConfig();
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
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
    getController().setP(m_conf.getDouble("TurnToFaceCommand.Kp"));
    getController().setI(m_conf.getDouble("TurnToFaceCommand.Ki"));
    getController().setD(m_conf.getDouble("TurnToFaceCommand.Kd"));
    getController().setMinOutputToMove(m_conf.getDouble("TurnToFaceCommand.minOutputToMove"));
    getController().setTolerance(m_conf.getDouble("TurnToFaceCommand.positionTolerance"));
    this.m_sensor = sensor;

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
