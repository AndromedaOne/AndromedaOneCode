// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.armTestBedCommands;

import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Robot;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.armTestBed.ArmTestBedBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SysIdCommand extends SequentialCommandGroup4905 {
  private ArmTestBedBase m_armTestBed;

  public SysIdCommand() {
    m_armTestBed = Robot.getInstance().getSubsystemsContainer().getArmTestBed();
    addRequirements(m_armTestBed.getSubsystemBase());
    addCommands(m_armTestBed.sysIdQuasistatic(SysIdRoutine.Direction.kForward),
        m_armTestBed.sysIdQuasistatic(SysIdRoutine.Direction.kReverse),
        m_armTestBed.sysIdDynamic(SysIdRoutine.Direction.kForward),
        m_armTestBed.sysIdDynamic(SysIdRoutine.Direction.kReverse));
  }
}
