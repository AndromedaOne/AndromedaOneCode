// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.ArmRotationExtensionSingleton;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase.RetractLimitSwitchState;
import frc.robot.telemetries.Trace;

public class ExtendRetract extends SequentialCommandGroup4905 {
  /** Creates a new ExtRetSeq. */
  public ExtendRetract(SamArmExtRetBase armExtRet, boolean needToEnd, boolean useSmartDashboard) {
    addCommands(new InitializeArmExtRet(armExtRet),
        new ExtendRetractInternal(armExtRet, needToEnd, useSmartDashboard));
  }

  public ExtendRetract(SamArmExtRetBase armExtRet, boolean useSmartDashboard) {
    this(armExtRet, true, true);
    SmartDashboard.putNumber("Extend Arm Position Value", 0);
  }

  public ExtendRetract(SamArmExtRetBase armExtRet) {
    this(armExtRet, true, false);
  }

  private class ExtendRetractInternal extends PIDCommand4905 {
    private SamArmExtRetBase m_armExtRet;
    private boolean m_needToEnd = false;
    private boolean m_useSmartDashboard = false;
    private double m_extendRetractJoystickPvalue = 0.001;

    public ExtendRetractInternal(SamArmExtRetBase armExtRet, boolean needToEnd,
        boolean useSmartDashboard) {

      super(new PIDController4905SampleStop("ArmExtRet"), armExtRet::getPosition, () -> 0,
          output -> {
            armExtRet.extendRetract(output);
          });
      addRequirements(armExtRet);
      m_armExtRet = armExtRet;
      m_needToEnd = needToEnd;
      m_useSmartDashboard = useSmartDashboard;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();

      if (m_useSmartDashboard) {
        ArmRotationExtensionSingleton.getInstance()
            .setPosition(SmartDashboard.getNumber("Extend Arm Position Value", 0));
      }
      setSetpoint(ArmRotationExtensionSingleton.getInstance().getPosition());

      getController().setP(pidConstantsConfig.getDouble("ArmExtRet.Kp"));
      getController().setI(pidConstantsConfig.getDouble("ArmExtRet.Ki"));
      getController().setD(pidConstantsConfig.getDouble("ArmExtRet.Kd"));
      getController().setMinOutputToMove(pidConstantsConfig.getDouble("ArmExtRet.minOutputToMove"));
      getController().setTolerance(pidConstantsConfig.getDouble("ArmExtRet.tolerance"));
      getController().setMaxOutput(1);
      Trace.getInstance().logCommandStart(this);
      Trace.getInstance().logCommandInfo(this,
          "Extend Retract Arm to: " + getController().getSetpoint());
    }

    @Override
    public void execute() {
      super.execute();
      double joystickValue = Robot.getInstance().getOIContainer().getSubsystemController()
          .getArmExtendRetractJoystickValue();
      if (DriverStation.isTeleop() && (joystickValue != 0)) {
        ArmRotationExtensionSingleton.getInstance()
            .setPosition(ArmRotationExtensionSingleton.getInstance().getPosition().getAsDouble()
                + (joystickValue * m_extendRetractJoystickPvalue));
      }
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

  private class InitializeArmExtRet extends CommandBase {
    private SamArmExtRetBase m_armExtRetBase;

    /** Creates a new InitializeArmExtRet. */
    public InitializeArmExtRet(SamArmExtRetBase samArmExtRet) {
      m_armExtRetBase = samArmExtRet;
      addRequirements(m_armExtRetBase);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      Trace.getInstance().logCommandStart(this);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      if (!m_armExtRetBase.isInitialized()) {
        m_armExtRetBase.retractArmInitialize();
      }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      if (!m_armExtRetBase.isInitialized()) {
        m_armExtRetBase.setZeroOffset();
        m_armExtRetBase.setInitialized();
      }
      Trace.getInstance().logCommandStop(this);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return (m_armExtRetBase.getRetractLimitSwitchState() == RetractLimitSwitchState.CLOSED)
          || m_armExtRetBase.isInitialized();
    }
  }
}
