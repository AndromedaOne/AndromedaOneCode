// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.TargetDistanceAndAngle;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.PoseEstimation4905;
import frc.robot.utils.PoseEstimation4905.RegionsForPose;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class FinishPath extends SequentialCommandGroup4905 {
  /** Creates a new FinishPath. */
  private DriveTrainBase m_drivetrain;
  private boolean m_isForward = true;
  private int m_index = -1;
  private PoseEstimation4905.RegionsForPose m_region = RegionsForPose.UNKNOWN;
  private boolean m_useLeft = false;
  private ArrayList<PhotonVisionBase> m_photonVision = new ArrayList<PhotonVisionBase>();
  private TargetDistanceAndAngle m_wantedDistanceAndAngle = new TargetDistanceAndAngle(0, 0, false);
  private DoubleSupplier m_distance = m_wantedDistanceAndAngle.getTargetDistanceSupplier();
  private DoubleSupplier m_angle = m_wantedDistanceAndAngle.getTargetAngleSupplier();
  private Command m_command;

  public FinishPath(boolean isForward, PoseEstimation4905.RegionsForPose region, boolean useLeft) {
    m_drivetrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    m_isForward = isForward;
    if (m_isForward) {
      m_index = 0;
    } else {
      m_index = 3;
    }
    m_region = region;
    m_useLeft = useLeft;
    m_photonVision = Robot.getInstance().getSensorsContainer().getPhotonVisionList();
    addCommands(new MoveUsingEncoder(m_drivetrain, m_angle, m_distance, 1.0));
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    int aprilTag = m_drivetrain.regionToAprilTag(m_region);
    m_photonVision.get(m_index).computeDistanceAndAngle(aprilTag, true, m_useLeft,
        m_wantedDistanceAndAngle);
    // m_wantedDistanceAndAngle.setDistance(m_wantedDistanceAndAngle.getDistance() -
    // 1);
    if (!m_isForward) {
      m_wantedDistanceAndAngle.setDistance(-(m_wantedDistanceAndAngle.getDistance() - 1));
    }
    m_angle = m_wantedDistanceAndAngle.getTargetAngleSupplier();
    m_distance = m_wantedDistanceAndAngle.getTargetDistanceSupplier();
    Trace.getInstance().logInfo("angle value: " + m_angle.getAsDouble());
    Trace.getInstance().logInfo("distance value: " + m_distance.getAsDouble());
    Trace.getInstance().logInfo("april tag: " + aprilTag);
    // m_command = new MoveUsingEncoder(m_drivetrain, m_angle, m_distance, 1.0);
    // m_command.schedule();
  }

  @Override
  public boolean isFinished() {
    return super.isFinished();
  }
}
