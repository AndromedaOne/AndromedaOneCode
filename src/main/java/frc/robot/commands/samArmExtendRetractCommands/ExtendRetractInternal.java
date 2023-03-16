// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.ArmRotationExtensionSingleton;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.telemetries.Trace;

public class ExtendRetractInternal extends PIDCommand4905 {
  private SamArmExtRetBase m_armExtRet;
  private boolean m_needToEnd = false;
  private boolean m_useSmartDashboard = false;
  private boolean m_useSingletonValue = false;

  public ExtendRetractInternal(SamArmExtRetBase armExtRet, DoubleSupplier position,
      boolean needToEnd, boolean useSmartDashboard, boolean useSingletonValue) {

    super(new PIDController4905SampleStop("ArmExtRet"), armExtRet::getPosition, position,
        output -> {
          armExtRet.extendRetract(output);
        });
    addRequirements(armExtRet);
    m_armExtRet = armExtRet;
    m_needToEnd = needToEnd;
    m_useSmartDashboard = useSmartDashboard;
    m_useSingletonValue = useSingletonValue;
  }

  public ExtendRetractInternal(SamArmExtRetBase armExtRet, DoubleSupplier position,
      boolean needToEnd) {
    this(armExtRet, position, needToEnd, false, false);
  }

  public ExtendRetractInternal(SamArmExtRetBase armExtRet, boolean needToEnd) {
    this(armExtRet, () -> 0, needToEnd, false, true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
    super.initialize();

    if (m_useSmartDashboard) {
      setSetpoint(() -> SmartDashboard.getNumber("Extend Arm Position Value", 0));
    } else if (m_useSingletonValue) {
      setSetpoint(ArmRotationExtensionSingleton.getInstance().getPosition());
    }
    ArmRotationExtensionSingleton.getInstance().setPosition(m_setpoint.getAsDouble());

    getController().setP(pidConstantsConfig.getDouble("ArmExtRet.Kp"));
    getController().setI(pidConstantsConfig.getDouble("ArmExtRet.Ki"));
    getController().setD(pidConstantsConfig.getDouble("ArmExtRet.Kd"));
    getController().setMinOutputToMove(pidConstantsConfig.getDouble("ArmExtRet.minOutputToMove"));
    getController().setTolerance(pidConstantsConfig.getDouble("ArmExtRet.tolerance"));
    getController().setMaxOutput(0.75);
    Trace.getInstance().logCommandStart(this);
    Trace.getInstance().logCommandInfo(this, "Extend Retract Arm to: " + m_setpoint.getAsDouble());
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
    if (m_needToEnd && isOnTarget()) {
      return true;
    }
    return false;
  }

  public boolean isOnTarget() {
    return getController().atSetpoint();
  }
}