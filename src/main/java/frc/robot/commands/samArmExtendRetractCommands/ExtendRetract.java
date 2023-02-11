// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;

public class ExtendRetract extends PIDCommand4905 {
  /** Creates a new ExtendRetract. */
  public ExtendRetract(SamArmExtRetBase armExtRet, double position) {

    super(
      new PIDController4905SampleStop("ArmExtRet"), 
      armExtRet::getPosition, 
      0, 
      output -> {
        armExtRet.extendRetract(output);
      }, 
      armExtRet);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
    getController().setP(pidConstantsConfig.getDouble("ArmExtRet.Kp"));
    getController().setI(pidConstantsConfig.getDouble("ArmExtRet.Ki"));
    getController().setD(pidConstantsConfig.getDouble("ArmExtRet.Kd"));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
