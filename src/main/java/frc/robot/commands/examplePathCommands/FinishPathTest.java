// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.examplePathCommands;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.TargetDetectedAndAngle;
import frc.robot.sensors.photonvision.TargetDetectedAndDistance;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class FinishPathTest extends Command {
  /** Creates a new FinishPathTest. */
  private DriveTrainBase m_drivetrain;
  private boolean m_move = false;
  private SequentialCommandGroup4905 m_command;
  private ArrayList<PhotonVisionBase> m_photonVision = new ArrayList<PhotonVisionBase>();
  private double m_coralPipeDistanceInInches = 0.0;

  public FinishPathTest(DriveTrainBase drivetrain, boolean move) {
    m_drivetrain = drivetrain;
    m_move = move;
    m_photonVision = (Robot.getInstance().getSensorsContainer().getPhotonVisionList());

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Pose2d currentPose = m_drivetrain.currentPose2d();
    // Pose2d correctedPose = currentPose; // center of the front bumper
    double robotLength = 17.5 * 0.0254;
    double robotAngleRadians;
    double robotAngleDegrees = currentPose.getRotation().getDegrees();
    if (robotAngleDegrees < 0) {
      robotAngleDegrees = 360 + robotAngleDegrees;
    }
    double xTrans;
    double yTrans;
    if (robotAngleDegrees == 0) {
      xTrans = 0;
      yTrans = robotLength;
    } else if (robotAngleDegrees < 90) {
      robotAngleRadians = Math.toRadians(robotAngleDegrees);
      xTrans = Math.cos(robotAngleRadians) * robotLength;
      yTrans = Math.sin(robotAngleRadians) * robotLength;
    } else if (robotAngleDegrees == 90) {
      xTrans = robotLength;
      yTrans = 0;
    } else if ((robotAngleDegrees > 90) && (robotAngleDegrees < 180)) {
      robotAngleRadians = Math.toRadians(180 - robotAngleDegrees);
      xTrans = -(Math.cos(robotAngleRadians) * robotLength);
      yTrans = Math.sin(robotAngleRadians) * robotLength;
    } else if (robotAngleDegrees == 180) {
      xTrans = 0;
      yTrans = -robotLength;
    } else if ((robotAngleDegrees > 180) && (robotAngleDegrees < 270)) {
      robotAngleRadians = Math.toRadians(robotAngleDegrees - 180);
      xTrans = -(Math.cos(robotAngleRadians) * robotLength);
      yTrans = -(Math.sin(robotAngleRadians) * robotLength);
    } else if (robotAngleDegrees == 270) {
      xTrans = -robotLength;
      yTrans = 0;
    } else if ((robotAngleDegrees > 270) && (robotAngleDegrees < 360)) {
      robotAngleRadians = Math.toRadians(360 - robotAngleDegrees);
      xTrans = Math.cos(robotAngleRadians) * robotLength;
      yTrans = -(Math.sin(robotAngleRadians) * robotLength);
    } else {
      throw new RuntimeException("Error: Angle out of range. Angle: " + robotAngleDegrees);
    }
    Trace.getInstance().logInfo("X offset: " + xTrans);
    Trace.getInstance().logInfo("Y offset: " + yTrans);
    Trace.getInstance().logInfo("Pose: " + currentPose.toString());
    Trace.getInstance().logInfo("Angle: " + robotAngleDegrees);

    Translation2d currentTranslation = new Translation2d(currentPose.getX() + xTrans,
        currentPose.getY() + yTrans);
    Translation2d aprilTagTranslation = new Translation2d(3.6576, 4.02); // tag 18
    // Translation2d aprilTagTranslation = new Translation2d(4.0739, 3.3012); tag 17
    Translation2d coralPipeTranslation = new Translation2d(3.6576, 4.1859);
    double aprilTagDistance = currentTranslation.getDistance(aprilTagTranslation); // c
    double coralPipeDistance = currentTranslation.getDistance(coralPipeTranslation); // a
    double aprilTagDistanceInInches = aprilTagDistance * 39.3701;
    m_coralPipeDistanceInInches = coralPipeDistance * 39.3701;
    // should always be 6.5 inches / 0.1651 meters
    double aprilTagToCoralPipe = aprilTagTranslation.getDistance(coralPipeTranslation); // b
    Trace.getInstance().logInfo("Robot to april tag distance: " + aprilTagDistance);
    Trace.getInstance().logInfo("Robot to coral pipe distance: " + coralPipeDistance);
    Trace.getInstance().logInfo("April tag to coral pipe distance: " + aprilTagToCoralPipe);
    // travelangle is in degrees
    double travelAngle;
    travelAngle = ((Math.pow(coralPipeDistance, 2)) + (Math.pow(aprilTagDistance, 2)))
        - (Math.pow(aprilTagToCoralPipe, 2));
    travelAngle = travelAngle / (2 * coralPipeDistance * aprilTagDistance);
    travelAngle = Math.acos(travelAngle);
    // this is where travelangle is set to degrees
    travelAngle = Math.toDegrees(travelAngle);
    Trace.getInstance().logInfo("Travel angle: " + travelAngle);
    Trace.getInstance()
        .logInfo("Robot to april tag distance in inches: " + aprilTagDistanceInInches);
    Trace.getInstance()
        .logInfo("Robot to coral pipe distance in inches: " + m_coralPipeDistanceInInches);
    TargetDetectedAndDistance targetDistance = m_photonVision.get(0)
        .getTargetDetectedAndDistance(18);
    TargetDetectedAndAngle targetAngle = m_photonVision.get(0).getTargetDetectedAndAngle(17, 0);
    Trace.getInstance().logInfo("Target distance: " + targetDistance.getDistance());
    Trace.getInstance().logInfo("Target distance detected: " + targetDistance.getDetected());
    Trace.getInstance().logInfo("Target angle: " + targetAngle.getAngle());
    Trace.getInstance().logInfo("Target angle detected: " + targetAngle.getDetected());
    m_command = new MoveUsingEncoder(m_drivetrain, travelAngle, () -> m_coralPipeDistanceInInches,
        1.0);
    if (m_move) {
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
    if (m_move) {
      return m_command.isFinished();
    }
    return true;
  }
}
