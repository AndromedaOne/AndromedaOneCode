// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotateCommand;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;
import frc.robot.subsystems.billClimber.BillClimberBase;
import frc.robot.telemetries.Trace;

public class EnableClimberMode extends SequentialCommandGroup4905 {
  /** Creates a new EnableClimberMode. */
  private BillClimberBase m_climber;
  private ArmTestBenchRotateBase m_armRotate;
  private double m_armAngleForClimbing = 255;

  public EnableClimberMode(BillClimberBase climber, ArmTestBenchRotateBase armRotate) {
    m_climber = climber;
    m_armRotate = armRotate;
    addCommands(new ArmRotateCommand(m_armRotate, () -> m_armAngleForClimbing, true, true),
        new EnableClimberModeInternal());
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {

  }

  private class EnableClimberModeInternal extends Command {
    public EnableClimberModeInternal() {
      addRequirements(m_climber.getSubsystemBase(), m_armRotate.getSubsystemBase());
    }

    @Override
    public void initialize() {
      CommandScheduler.getInstance().removeDefaultCommand(m_climber.getSubsystemBase());
      CommandScheduler.getInstance().removeDefaultCommand(m_armRotate.getSubsystemBase());
      CommandScheduler.getInstance().setDefaultCommand(m_climber.getSubsystemBase(),
          new RunBillClimber(m_climber, false));
      CommandScheduler.getInstance().setDefaultCommand(m_armRotate.getSubsystemBase(),
          new ArmRotateCommand(m_armRotate, () -> m_armAngleForClimbing, false, true));
      Trace.getInstance().logCommandInfo(this, "Enable Climber Mode Ran");
      Trace.getInstance().logCommandInfo(this,
          CommandScheduler.getInstance().getDefaultCommand(m_climber.getSubsystemBase()).getName());
      BillClimberSingleton.getInstance().setClimberEnabled();
    }

    @Override
    public boolean isFinished() {
      return true;
    }
  }
}
