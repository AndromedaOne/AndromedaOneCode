package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.intake.IntakeBase;

public class DeployAndRunIntakeStarter extends CommandBase {

  IntakeBase m_intakeBase;
  BooleanSupplier m_finishedCondition;

  public DeployAndRunIntakeStarter(IntakeBase intakeBase, BooleanSupplier finishedCondition) {
    m_intakeBase = intakeBase;
    m_finishedCondition = finishedCondition;
  }

  @Override
  public void initialize() {
    if (!m_finishedCondition.getAsBoolean()) {
      CommandScheduler.getInstance().schedule(new DeployAndRunIntake(m_intakeBase, m_finishedCondition));
    }

  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);

  }

}