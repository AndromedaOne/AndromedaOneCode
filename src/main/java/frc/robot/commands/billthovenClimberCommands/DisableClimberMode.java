// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billClimber.BillClimberBase;
import frc.robot.telemetries.Trace;

public class DisableClimberMode extends SequentialCommandGroup4905 {
  /** Creates a new disableClimberMode. */
  private BillClimberBase m_climber;
  private BillArmRotateBase m_armRotate;

  public DisableClimberMode(BillClimberBase climber, BillArmRotateBase armRotate) {
    m_climber = climber;
    m_armRotate = armRotate;
    addCommands(new ArmRotate(m_armRotate, () -> 333, true, true),
        new DisableClimberModeInternal());
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {

  }

  private class DisableClimberModeInternal extends Command {
    public DisableClimberModeInternal() {
      addRequirements(m_climber.getSubsystemBase(), m_armRotate.getSubsystemBase());
    }

    @Override
    public void initialize() {
      CommandScheduler.getInstance().removeDefaultCommand(m_climber.getSubsystemBase());
      CommandScheduler.getInstance().removeDefaultCommand(m_armRotate.getSubsystemBase());
      CommandScheduler.getInstance().setDefaultCommand(m_climber.getSubsystemBase(),
          new StopClimber(m_climber));
      CommandScheduler.getInstance().setDefaultCommand(m_armRotate.getSubsystemBase(),
          new ArmRotate(m_armRotate, () -> 333, false, true));
      Trace.getInstance().logCommandInfo(this, "Disable Climber Mode Ran");
      Trace.getInstance().logCommandInfo(this,
          CommandScheduler.getInstance().getDefaultCommand(m_climber.getSubsystemBase()).getName());
      BillClimberSingleton.getInstance().setClimberDisabled();
    }

    @Override
    public boolean isFinished() {
      return true;
    }
  }
}
