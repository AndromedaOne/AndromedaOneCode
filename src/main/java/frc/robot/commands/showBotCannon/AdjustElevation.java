// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.showBotCannon.CannonBase;

public class AdjustElevation extends CommandBase {
  /** Creates a new AdjustElevation. */
  private CannonBase m_cannon;

  public AdjustElevation(CannonBase cannon) {
    m_cannon = cannon;
    addRequirements(m_cannon);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = Robot.getInstance().getOIContainer().getSubsystemController()
        .getElevatorAdjustElevationStick();
    speed = speed * 0.25;
    m_cannon.changeElevation(speed);
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
