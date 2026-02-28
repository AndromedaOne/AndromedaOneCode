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
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;
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
  private DoubleSupplier m_setpoint;
  private InterpolatingMap m_distanceRPMMap;
  private Config m_config = Config4905.getConfig4905().getShooterConfig();

  public ShootBasedOnDistance() {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = Robot.getInstance().getSubsystemsContainer().getShooter();
    m_distanceRPMMap = new InterpolatingMap(m_config, "shotShootingRPM");
    addCommands(new RunShooterRPM(m_shooter, m_setpoint, false));
  }

  public void additionalInitialize() {
    // get the distance from the hub here! when we eventually add a rotation,
    // we would get the rotation angle here.
    double distance = 0.0;
    m_setpoint = () -> m_distanceRPMMap.getInterpolatedValue(distance);
    Trace.getInstance().logCommandInfo(this, "setting the setpoint to " + m_setpoint.getAsDouble());
    Trace.getInstance().logCommandInfo(this, "the distance was " + distance);
  }

}
