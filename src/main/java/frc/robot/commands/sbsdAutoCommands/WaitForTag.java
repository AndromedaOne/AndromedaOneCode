// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdAutoCommands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.utils.PoseEstimation4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class WaitForTag extends Command {

  private DriveTrainBase m_driveTrain;
  private ArrayList<PhotonVisionBase> m_photonVison = new ArrayList<PhotonVisionBase>();
  private int m_index = -1;
  private int m_tag = -1;
  private boolean m_finished = false;

  /** Creates a new WaitForTag. */
  public WaitForTag(PoseEstimation4905.RegionsForPose region, boolean isForward) {
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    m_photonVison = Robot.getInstance().getSensorsContainer().getPhotonVisionList();
    if (isForward) {
      m_index = 0;
    } else {
      m_index = 3;
    }
    m_tag = m_driveTrain.regionToAprilTag(region);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_photonVison.get(m_index).doesTargetExist(m_tag)) {
      m_finished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finished;
  }
}
