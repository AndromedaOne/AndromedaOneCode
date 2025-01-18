// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.armTestBedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Robot;
import frc.robot.subsystems.armTestBed.ArmTestBedBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SysIdCommand extends Command {
  private ArmTestBedBase m_armTestBed;

  public SysIdCommand() {
    m_armTestBed = Robot.getInstance().getSubsystemsContainer().getArmTestBed();
    addRequirements(m_armTestBed.getSubsystemBase());
    SmartDashboard.getBoolean("SysId Static", true);
    SmartDashboard.getBoolean("SysId Forward", true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    boolean stat = SmartDashboard.getBoolean("SysId Static", true);
    boolean forward = SmartDashboard.getBoolean("SysId Forward", true);
    Command com;
    if (stat && forward) {
      com = m_armTestBed.sysIdQuasistatic(SysIdRoutine.Direction.kForward);
    } else if (stat && !forward) {
      com = m_armTestBed.sysIdQuasistatic(SysIdRoutine.Direction.kReverse);
    } else if (!stat && forward) {
      com = m_armTestBed.sysIdDynamic(SysIdRoutine.Direction.kForward);
    } else {
      com = m_armTestBed.sysIdDynamic(SysIdRoutine.Direction.kReverse);
    }
    CommandScheduler.getInstance().schedule(com);
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
    return false;
  }

}
