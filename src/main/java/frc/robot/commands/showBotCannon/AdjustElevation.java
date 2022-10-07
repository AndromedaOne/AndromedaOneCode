// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.showBotCannonElevator.CannonElevatorBase;
import frc.robot.telemetries.Trace;

public class AdjustElevation extends CommandBase {
  /** Creates a new AdjustElevation. */
  private CannonElevatorBase m_cannonElevator;

  public AdjustElevation(CannonElevatorBase cannonElevator) {
    m_cannonElevator = cannonElevator;
    addRequirements(m_cannonElevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double upSpeed = Robot.getInstance().getOIContainer().getDriveController()
        .getShowBotElevatorUpTriggerValue();
    double downSpeed = Robot.getInstance().getOIContainer().getDriveController()
        .getShowBotElevatorDownTriggerValue();
    System.out.println("AdjElev up: " + upSpeed + " down: " + downSpeed);
    double speed = 0;
    if (upSpeed > 0) {
      speed = upSpeed;
    } else {
      speed = -downSpeed;
    }
    speed *= 0.25;
    m_cannonElevator.changeElevation(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
