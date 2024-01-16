// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.topGunIntakeCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.subsystems.topGunIntake.IntakeBase;
import frc.robot.telemetries.Trace;

public class DeployAndRunIntake extends Command {
  /** Creates a new DeployAndRunIntake. */
  private IntakeBase m_intakeBase;
  private Config m_intakeConfig = Config4905.getConfig4905().getIntakeConfig();
  private double m_intakeSpeed = 0;
  private boolean m_runInReverse = false;
  private boolean m_deployed = false;

  public DeployAndRunIntake(IntakeBase intakeBase, boolean runInReverse) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeBase.getSubsystemBase());
    m_intakeBase = intakeBase;
    m_intakeSpeed = m_intakeConfig.getDouble("intakeSpeed");
    m_runInReverse = runInReverse;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double intakeSpeed = m_intakeSpeed;
    if (m_runInReverse) {
      intakeSpeed *= -1;
    } else if (!m_runInReverse) {
      m_intakeBase.deployIntake();
      m_deployed = true;
    }
    m_intakeBase.runIntakeWheels(intakeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeBase.stopIntakeWheels();
    if (m_deployed) {
      m_intakeBase.retractIntake();
    }
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
