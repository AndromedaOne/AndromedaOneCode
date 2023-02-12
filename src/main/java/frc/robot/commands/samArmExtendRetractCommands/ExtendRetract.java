// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.telemetries.Trace;

public class ExtendRetract extends PIDCommand4905 {
  /** Creates a new ExtendRetract. */
  private DoubleSupplier m_position;
  private SamArmExtRetBase m_armExtRet;

  public ExtendRetract(SamArmExtRetBase armExtRet, DoubleSupplier position) {

    super(new PIDController4905SampleStop("ArmExtRet"), armExtRet::getPosition, position,
        output -> {
          armExtRet.extendRetract(output);
        }, armExtRet);
    m_position = position;
    m_armExtRet = armExtRet;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
    super.initialize();

    getController().setP(pidConstantsConfig.getDouble("ArmExtRet.Kp"));
    getController().setI(pidConstantsConfig.getDouble("ArmExtRet.Ki"));
    getController().setD(pidConstantsConfig.getDouble("ArmExtRet.Kd"));
    getController().setMinOutputToMove(pidConstantsConfig.getDouble("ArmExtRet.minOutputToMove"));
    getController().setTolerance(pidConstantsConfig.getDouble("ArmExtRet.tolerance"));
    Trace.getInstance().logCommandInfo(this, "Extend Retract Arm to: " + m_position);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_armExtRet.extendRetract(0);
    Trace.getInstance().logCommandStop(this);
    Trace.getInstance().logCommandInfo(this, "Ending Position: " + m_armExtRet.getPosition());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public boolean isOnTarget() {
    return getController().atSetpoint();
  }
}
