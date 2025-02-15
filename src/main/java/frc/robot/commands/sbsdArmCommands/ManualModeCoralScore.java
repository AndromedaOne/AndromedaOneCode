package frc.robot.commands.sbsdArmCommands;

import frc.robot.Robot;
import frc.robot.oi.SubsystemController;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;

public class ManualModeCoralScore extends ParallelCommandGroup4905 {
  public Integer m_level = 0;

  public ManualModeCoralScore() {
// Needs to add moveArmEndEffector command
  }

  public void additionalInitialize() {
    SubsystemController m_SubsystemController = Robot.getInstance().getOIContainer()
        .getSubsystemController();

    // Get the level from the subsytem controller (x,a,b,y buttons)
  }

}
