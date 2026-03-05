// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.FuelRaiderCommands.RunShooterRPM;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.FieldConstants;
import frc.robot.utils.InterpolatingMap;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootBasedOnDistance extends SequentialCommandGroup4905 {
  /** Creates a new ShootBasedOnDistance. */
  // okay so basically grab the distance from what gio is currently doing
  // (will have to be filled in later) and use said distance to
  // calculate the RPM based on the interpolation maps
  // because the maps are not filled out correctly right now (2/28),
  // this should not be run until they are.
  private ShooterBase m_shooter;

  private double m_setpoint;
  private DoubleSupplier m_setpointSupplier = () -> m_setpoint;
  private InterpolatingMap m_distanceRPMMap;
  private Config m_config = Config4905.getConfig4905().getShooterConfig();
  private Pose2d m_hubPose = new FieldConstants().getHubPose();
  private DriveTrainBase m_driveTrain;
  private RunShooterRPM m_command;

  public ShootBasedOnDistance() {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = Robot.getInstance().getSubsystemsContainer().getShooter();
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    m_distanceRPMMap = new InterpolatingMap(m_config, "shotShootingRPM");
    m_command = new RunShooterRPM(m_shooter, m_setpointSupplier);
    addCommands(m_command);
  }

  public void additionalInitialize() {
    // get the distance from the hub here! when we eventually add a rotation,
    // we would get the rotation angle here.
    // we probably wont add a rotation...
    m_setpoint = () -> m_distanceRPMMap
        .getInterpolatedValue(m_driveTrain.getHubDistanceToRobotInInches());
    m_setpoint = m_distanceRPMMap.getInterpolatedValue(distance * 39.3701);
    Trace.getInstance().logCommandInfo(this, "m_setpoint = " + m_setpoint);
    Trace.getInstance().logCommandInfo(this,
        "setting the setpoint to " + m_setpointSupplier.getAsDouble());
    Trace.getInstance().logCommandInfo(this, "the distance was " + distance + " in meters");
  }

}
