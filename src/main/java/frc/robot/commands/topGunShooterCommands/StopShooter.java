// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.topGunShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;

public class StopShooter extends Command {
  private ShooterWheelBase m_topShooterWheel;
  private ShooterWheelBase m_bottomShooterWheel;

  /** Creates a new DefaultShooter. */
  public StopShooter(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel) {
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    addRequirements(topShooterWheel.getSubsystemBase(), bottomShooterWheel.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_topShooterWheel.setShooterWheelPower(0);
    m_bottomShooterWheel.setShooterWheelPower(0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
