// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdAutoCommands;

import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdTeleOpCommands.sbsdMoveArmAndEndEffector;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.sbsdcoralendeffector.RealCoralIntakeEject;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class sbsdCoralScoreLevel2 extends SequentialCommandGroup4905 {
  RealCoralIntakeEject m_CoralIntakeEject;

  /** Creates a new sbsdCoralScoreLevel4. */
  public sbsdCoralScoreLevel2() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new sbsdMoveArmAndEndEffector(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_2));
  }

  @Override
  public void additionalInitialize() {
    // TODO: reinstate this after the merge party
    // m_CoralIntakeEject =
    // Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();
  }

  @Override
  public boolean isFinished() {
    if (super.isFinished()) {
      return true;
    }
    // checking state of the coral intake (state machine thingy) use wait for coral
    // state
    return true;
  }
}
