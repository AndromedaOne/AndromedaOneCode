// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class ShootTarmac extends SequentialCommandGroup {
  double m_shooterSetpoint;
  double m_shooterAngle;
  double m_feederSetpoint;
  boolean m_shootBackwards = false;

  public ShootTarmac(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment, boolean shootLow,
      boolean shootBackwards) {

    m_shootBackwards = shootBackwards;

    System.out.println("ShooterSetpoint =" + m_shooterSetpoint);
    System.out.println(" FeederSetpoint =" + m_feederSetpoint);
    System.out.println(" angle = " + m_shooterAngle);

    addCommands(new ShootCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
        () -> m_shooterSetpoint, () -> m_shooterAngle, () -> m_feederSetpoint));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);

    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getShootLowHubButtonPressed()) {
      System.out.println("Tarmac Initialize Button Pressed");
      m_shooterSetpoint = 3000.0;
      m_shooterAngle = 29.1;
      m_feederSetpoint = 0.5;
    } else {
      System.out.println("Tarmac Initialize Button Not Pressed");
      m_shooterSetpoint = 3200.0;
      m_shooterAngle = 47.5;
      if (m_shootBackwards) {
        m_shooterAngle = 62.5;
      }
      m_feederSetpoint = 0.5;
    }
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

}