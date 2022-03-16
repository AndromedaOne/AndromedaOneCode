// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.telemetries.Trace;

public class MoveShooterAlignment extends PIDCommand4905 {
  private Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
  private ShooterAlignmentBase m_shooterAlignment;

  /** Creates a new InitializeShooterAlignment. */
  public MoveShooterAlignment(ShooterAlignmentBase shooterAlignment, DoubleSupplier setpoint,
      boolean tunePID, double pValue, double minOutputToMove, double tolerance) {
    super(new PIDController4905SampleStop("shooterAlignment", 0, 0, 0, 0),
        shooterAlignment::getAngle, setpoint, output -> {
          shooterAlignment.rotateShooter(output);
        });
    addRequirements(shooterAlignment);
    m_shooterAlignment = shooterAlignment;
    if (tunePID) {
      getController().setP(pValue);
      getController().setTolerance(tolerance);
      getController().setMinOutputToMove(minOutputToMove);
    } else {
      getController()
          .setP(m_shooterConfig.getDouble(m_shooterAlignment.getShooterName() + ".pValue"));
      getController().setTolerance(
          m_shooterConfig.getDouble(m_shooterAlignment.getShooterName() + ".tolerance"));
      getController().setMinOutputToMove(
          m_shooterConfig.getDouble(m_shooterAlignment.getShooterName() + ".minOutputToMove"));
    }
    getController()
        .setI(m_shooterConfig.getDouble(m_shooterAlignment.getShooterName() + ".iValue"));
    getController()
        .setD(m_shooterConfig.getDouble(m_shooterAlignment.getShooterName() + ".dValue"));
    getController().setMaxOutput(1.0);
  }

  public MoveShooterAlignment(ShooterAlignmentBase shooterAlignment, DoubleSupplier setpoint) {
    this(shooterAlignment, setpoint, false, 0.0, 0.0, 0.0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
    System.out.println(
        "moveShooter setpoint: " + getController().getSetpoint() + " p= " + getController().getP());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooterAlignment.rotateShooter(0);
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_controller.atSetpoint();
  }
}
