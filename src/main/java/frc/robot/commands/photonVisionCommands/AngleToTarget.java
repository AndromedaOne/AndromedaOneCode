// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.photonVisionCommands;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.TargetDetectedAndAngle;
import frc.robot.subsystems.ledlights.LEDs;

public class AngleToTarget extends Command {

  private PhotonVisionBase m_photonVision;
  private IntSupplier m_wantedID;
  private DoubleSupplier m_setpoint;
  private TargetDetectedAndAngle m_targetDetectedAndAngle;
  private boolean m_targetFound;
  private LEDs m_LEDs = Robot.getInstance().getSubsystemsContainer().getWs2812LEDs();

  /** Creates a new AngleToTarget. */
  public AngleToTarget(PhotonVisionBase photonvision, IntSupplier wantedID, DoubleSupplier setpoint,
      TargetDetectedAndAngle targetDetectedAndAngle) {
    m_photonVision = photonvision;
    m_wantedID = wantedID;
    m_setpoint = setpoint;
    m_targetDetectedAndAngle = targetDetectedAndAngle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_LEDs.setTargetFound(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_LEDs.setTargetFound(true);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    TargetDetectedAndAngle target = m_photonVision.getTargetDetectedAndAngle(m_wantedID.getAsInt(),
        m_setpoint.getAsDouble());
    m_targetDetectedAndAngle.setAngle(target.getAngle());
    m_targetDetectedAndAngle.setDetected(target.getDetected());
    m_targetFound = m_targetDetectedAndAngle.getDetected();
    m_LEDs.setTargetFound(m_targetFound);
    return m_targetDetectedAndAngle.getDetected();
  }
}
