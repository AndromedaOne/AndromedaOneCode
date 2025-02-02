// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.armTestBedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.armTestBed.ArmTestBedBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ArmControlCommand extends Command {
  private ArmTestBedBase m_armTestBed;
  private boolean m_useSmartDashboard = false;
  private double m_setpoint = 0;

  public ArmControlCommand(boolean useSmartDashboard) {
    m_useSmartDashboard = useSmartDashboard;
    m_armTestBed = Robot.getInstance().getSubsystemsContainer().getArmTestBed();
    addRequirements(m_armTestBed.getSubsystemBase());
    if (m_useSmartDashboard) {
      SmartDashboard.putNumber("Arm Test Bed goal degrees", 0);
    }
  }

  public ArmControlCommand(double setpoint) {
    this(false);
    m_setpoint = setpoint;
  }

  public ArmControlCommand(ArmSetpoints setpoint) {
    this(setpoint.getAngleInDeg());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_useSmartDashboard) {
      m_armTestBed.setGoalDeg(SmartDashboard.getNumber("Arm Test Bed goal degrees", 0));
    } else {
      m_armTestBed.setGoalDeg(m_setpoint);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_useSmartDashboard) {
      m_armTestBed.setGoalDeg(SmartDashboard.getNumber("Arm Test Bed goal degrees", 0));
    }
    m_armTestBed.calculateSpeed();
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
