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
public class TurnToFaceCommand extends PIDCommand4905 {
  protected DoubleSupplier m_sensor;
  protected int m_lostCounter = 0;
  protected int m_targetCounter = 0;
  double m_targetMovingAverageTotal = 0.0;
  double m_targetMovingAverage = 0.0;
  SensorsContainer m_sensorcontainer = Robot.getInstance().getSensorsContainer();
  protected static Config m_conf = Config4905.getConfig4905().getCommandConstantsConfig();
  protected Config m_conf2 = Config4905.getConfig4905().getSensorConfig();
  LimeLightCameraBase m_limelight;
  int targetNotInFOV = 500;

  public TurnToFaceCommand(DoubleSupplier sensor) {
    // controller used for pid command
    super(new PIDController4905("TurnToFace", 0.0, 0.0, 0.0, 0.0), sensor,
        // set point
        0,
        // uses the output
        output -> {
          Robot.getInstance().getSubsystemsContainer().getDrivetrain().moveUsingGyro(0.0, -output,
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
    m_limelight.disableLED();
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    // TODO Auto-generated method stub
    super.initialize();
    m_lostCounter = 0;
    m_targetCounter = 0;
    m_targetMovingAverageTotal = 0;
    m_targetMovingAverage = 0;
    m_limelight = m_sensorcontainer.getLimeLight();
    Trace.getInstance().logCommandInfo(this, "initialize limelightd");
    m_limelight.enableLED();
    m_limelight.enableLED();
    m_limelight.enableLED();
  }

  @Override
  public boolean isFinished() {
    // TODO Auto-generated method stub
    boolean targetInFOV = m_sensorcontainer.getLimeLight().targetLock();
    boolean returnValue;

    m_lostCounter++;
    m_targetCounter++;
    if (targetInFOV == true) {
      m_targetMovingAverageTotal += 1.0;
      m_targetMovingAverage = m_targetMovingAverageTotal / m_targetCounter;
    }
    boolean atSetpoint = this.getController().atSetpoint();

    if (!m_conf2.hasPath("limelight") || (m_conf2.getDouble("limelight.cameraHeight") == 0.0)) {
      Trace.getInstance().logCommandInfo(this, "no limelight found");
      return true;
    }
    if (m_lostCounter == targetNotInFOV) {
      Trace.getInstance().logCommandInfo(this, "limelight finished");
      return true;
    }
    // if ((targetInFOV == true) && (m_targetMovingAverage >= 0.8)) {
    if ((targetInFOV == true) && (atSetpoint == true)) {
      returnValue = true;
      System.out.println("limelight return value " + returnValue);
      System.out.println("target moving average =" + m_targetMovingAverage);
      System.out.println("target seen");
    } else {
      System.out.println("limelight distance to node " + m_limelight.verticalRadiansToTarget() + " "
          + m_limelight.distanceToNode());
      System.out.println("target moving average =" + m_targetMovingAverage);
      System.out.println("target not seen or not a set point");
      returnValue = false;
    }

    return (returnValue);

  }

}
