// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmRotateCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.samArmRotate.ArmAngleBrakeState;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class RotateArm extends PIDCommand4905 {
  /** Creates a new RotateArm. */
  private DoubleSupplier m_angle;
  private SamArmRotateBase m_armRotate;
  private boolean m_needToEnd = false;

  public RotateArm(SamArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd) {

    super(new PIDController4905SampleStop("ArmRotate"), armRotate::getAngle, angle, output -> {
      armRotate.rotate(output);
    }, armRotate);
    m_angle = angle;
    m_armRotate = armRotate;
    m_needToEnd = needToEnd;
    addRequirements(armRotate);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
    super.initialize();

    getController().setP(pidConstantsConfig.getDouble("ArmRotate.Kp"));
    getController().setI(pidConstantsConfig.getDouble("ArmRotate.Ki"));
    getController().setD(pidConstantsConfig.getDouble("ArmRotate.Kd"));
    getController().setMinOutputToMove(pidConstantsConfig.getDouble("ArmRotate.minOutputToMove"));
    getController().setTolerance(pidConstantsConfig.getDouble("ArmRotate.tolerance"));
    Trace.getInstance().logCommandInfo(this, "Rotate Arms to: " + m_angle);
    if (m_armRotate.getState() == ArmAngleBrakeState.ENGAGEARMBRAKE) {
      m_armRotate.disengageArmBrake();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
    Trace.getInstance().logCommandInfo(this, "Ending Angle: " + m_armRotate.getAngle());
    m_armRotate.engageArmBrake();
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
