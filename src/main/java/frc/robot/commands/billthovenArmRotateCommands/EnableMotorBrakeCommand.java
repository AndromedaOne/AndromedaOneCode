// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenArmRotateCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;

public class EnableMotorBrakeCommand extends Command {
  private ArmTestBenchRotateBase m_armRotate;

  public EnableMotorBrakeCommand(ArmTestBenchRotateBase armRotate) {
    m_armRotate = armRotate;
    addRequirements(m_armRotate.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_armRotate.enableMotorBrake();
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
    return true;
  }
}
