package frc.robot.commands.sbsdArmCommands;

import frc.robot.Robot;
import frc.robot.commands.sbsdTeleOpCommands.sbsdMoveArmAndEndEffector;
import frc.robot.oi.SubsystemController;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

public class ManualModeCoralScore extends SequentialCommandGroup4905 {
  private ArmSetpoints m_level = ArmSetpoints.CORAL_LOAD;
  private sbsdMoveArmAndEndEffector m_armAndEndEffectorCommand;

  public ManualModeCoralScore() {
    m_armAndEndEffectorCommand = new sbsdMoveArmAndEndEffector(() -> m_level);

  }

  public void additionalInitialize() {
    SubsystemController subsystemController = Robot.getInstance().getOIContainer()
        .getSubsystemController();
    if (subsystemController.getScoreLevelOne().getAsBoolean()) {
      m_level = ArmSetpoints.LEVEL_1;
    } else if (subsystemController.getScoreLevelTwo().getAsBoolean()) {
      m_level = ArmSetpoints.LEVEL_2;
    } else if (subsystemController.getScoreLevelThree().getAsBoolean()) {
      m_level = ArmSetpoints.LEVEL_3;
    } else if (subsystemController.getScoreLevelFour().getAsBoolean()) {
      m_level = ArmSetpoints.LEVEL_4;
    }
    // cancels if button not pressed
    if (m_level == ArmSetpoints.CORAL_LOAD) {
      return;
    }
    m_armAndEndEffectorCommand.schedule();

    // Get the level from the subsytem controller (x,a,b,y buttons)
  }

}
