// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdAutoCommands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.telemetries.Trace;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AprilTagSnapshot extends Command {
  /** Creates a new aprilTagSnapshot. */
  private ArrayList<PhotonVisionBase> m_cameraList = new ArrayList<PhotonVisionBase>();

  public AprilTagSnapshot() {
    m_cameraList = Robot.getInstance().getSensorsContainer().getPhotonVisionList();
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_cameraList.get(3).getPhotonCamera().takeOutputSnapshot();
    Trace.getInstance().logCommandInfo(this, "Output snapshot taken");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
