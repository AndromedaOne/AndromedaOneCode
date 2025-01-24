// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.armTestBedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.armTestBed.ArmTestBedBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TrapezoidProfileCommand extends Command {
  private ArmTestBedBase m_armTestBed;
  public TrapezoidProfileCommand() {
    m_armTestBed= Robot.getInstance().getSubsystemsContainer().getArmTestBed();
    addRequirements(m_armTestBed.getSubsystemBase());
    SmartDashboard.putNumber("Arm Test Bed goal", 0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_armTestBed.setGoalDeg(SmartDashboard.getNumber("Arm Test Bed goal", 0));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_armTestBed.calculateAndSetVoltageForGoal();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
