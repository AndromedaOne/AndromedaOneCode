// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.FuelRaiderCommands.RunShooterRPM;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShuttleShot extends SequentialCommandGroup4905 {
  /** Creates a new ShootBasedOnDistance. */
  // okay so basically grab the distance from what gio is currently doing
  // (will have to be filled in later) and use said distance to
  // calculate the RPM based on the interpolation maps
  // because the maps are not filled out correctly right now (2/28),
  // this should not be run until they are.

  private ShooterBase m_bottomShooter;
  private ShooterBase m_topShooter;
  private double m_bottomSetpoint;
  private DoubleSupplier m_bottomSetpointSupplier = () -> m_bottomSetpoint;
  private InterpolatingMap m_bottomDistanceRPMMap;
  private double m_topSetpoint;
  private DoubleSupplier m_topSetpointSupplier = () -> m_topSetpoint;
  private InterpolatingMap m_topDistanceRPMMap;
  private Config m_config = Config4905.getConfig4905().getShooterConfig();
  private DriveTrainBase m_driveTrain;
  private RunShooterRPM m_command;
  private DoubleSupplier m_distance;

  public ShuttleShot() {
    // Use addRequirements() here to declare subsystem dependencies.
    m_bottomShooter = Robot.getInstance().getSubsystemsContainer().getBottomShooter();
    m_topShooter = Robot.getInstance().getSubsystemsContainer().getTopShooter();
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    m_bottomDistanceRPMMap = new InterpolatingMap(m_config, "bottomShooter.shotShootingRPM");
    m_topDistanceRPMMap = new InterpolatingMap(m_config, "topShooter.shotShootingRPM");
    m_distance = m_driveTrain.getHubDistanceToRobotInInchesSupplier();
    m_bottomSetpointSupplier = () -> (m_bottomDistanceRPMMap
        .getInterpolatedValue(m_distance.getAsDouble()));
    m_topSetpointSupplier = () -> (m_topDistanceRPMMap
        .getInterpolatedValue(m_distance.getAsDouble()));
    m_command = new RunShooterRPM(m_bottomShooter, m_bottomSetpointSupplier, m_topShooter,
        m_topSetpointSupplier);
    addCommands(m_command);
  }

  public void additionalInitialize() {
    // get the distance from the SHUTTLE SPOT here! when we eventually add a
    // rotation, we would get the rotation angle here.
    // we probably wont add a rotation...
    Trace.getInstance().logCommandInfo(this, "bottom setpoint:" + m_bottomSetpoint);
    Trace.getInstance().logCommandInfo(this, "top setpoint:" + m_topSetpoint);
    Trace.getInstance().logCommandInfo(this,
        "the distance was " + m_distance.getAsDouble() + " inches");
  }

}
