// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.examplePathCommands;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.TargetDistanceAndAngle;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class FinishPathTest extends Command {
  /** Creates a new FinishPathTest. */
  private DriveTrainBase m_drivetrain;
  private boolean m_move = false;
  private boolean m_useLeft = false;
  private SequentialCommandGroup4905 m_command;
  private ArrayList<PhotonVisionBase> m_photonVision = new ArrayList<PhotonVisionBase>();
  private TargetDistanceAndAngle m_wantedDistanceAndAngle;

  public FinishPathTest(DriveTrainBase drivetrain, boolean move, boolean useLeft) {
    m_drivetrain = drivetrain;
    m_move = move;
    m_useLeft = useLeft;
    m_photonVision = Robot.getInstance().getSensorsContainer().getPhotonVisionList();

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    int index = (int) SmartDashboard.getNumber("Camera index to use", 0);
    m_wantedDistanceAndAngle = m_photonVision.get(index).computeDistanceAndAngle(18, true,
        m_useLeft);
    if (index == 3) {
      m_wantedDistanceAndAngle.setDistance(-m_wantedDistanceAndAngle.getDistance());
    }
    m_command = new MoveUsingEncoder(m_drivetrain, () -> m_wantedDistanceAndAngle.getAngle(),
        () -> m_wantedDistanceAndAngle.getDistance(), 1.0);
    if (m_move && m_wantedDistanceAndAngle.getDetected()) {
      CommandScheduler.getInstance().schedule(m_command);
    }

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
    if (m_move && m_wantedDistanceAndAngle.getDetected()) {
      return m_command.isFinished();
    }
    return true;
  }
}
