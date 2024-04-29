// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.photonVisionCommands;

import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.TargetDetectedAndDistance;
import frc.robot.subsystems.ledlights.LEDs;

public class DistanceToTarget extends Command {

  private PhotonVisionBase m_photonVision;
  private IntSupplier m_wantedID;

  private boolean m_targetFound;
  private LEDs m_LEDs = Robot.getInstance().getSubsystemsContainer().getWs2812LEDs();

  /** Creates a new AngleToTarget. */
  public DistanceToTarget(PhotonVisionBase photonvision, IntSupplier wantedID) {
    m_photonVision = photonvision;
    m_wantedID = wantedID;
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
    TargetDetectedAndDistance target = m_photonVision
        .getTargetDetectedAndDistance(m_wantedID.getAsInt());
    m_targetFound = target.getDetected();
    m_LEDs.setTargetFound(m_targetFound);
    return m_targetFound;
  }
}
