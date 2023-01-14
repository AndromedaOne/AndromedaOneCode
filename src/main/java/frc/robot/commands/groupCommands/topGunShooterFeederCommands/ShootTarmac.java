// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.topGunShooterFeederCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunShooter.ShooterAlignmentBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class ShootTarmac extends SequentialCommandGroup {
  double m_shooterSetpoint;
  double m_shooterAngle;
  double m_feederSetpoint;

  public ShootTarmac(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment) {

    addCommands(new ShootCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
        () -> m_shooterSetpoint, () -> m_shooterAngle, () -> m_feederSetpoint));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getShootBackwardButtonPressed()) {
      m_shooterSetpoint = 3200.0;
      m_shooterAngle = 64.5;
      m_feederSetpoint = 1.0;
    } else if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getShootLowHubButtonPressed()) {
      m_shooterSetpoint = 3000.0;
      m_shooterAngle = 29.1;
      m_feederSetpoint = 1.0;
    } else {
      m_shooterSetpoint = 3200.0;
      m_shooterAngle = 47.5;
      m_feederSetpoint = 1.0;
    }
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

}