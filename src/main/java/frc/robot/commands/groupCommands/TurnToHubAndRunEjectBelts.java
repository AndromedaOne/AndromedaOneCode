// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.TurnToFieldElement;
import frc.robot.commands.ejectBeltCommands.EjectBeltLeft;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurnToHubAndRunEjectBelts extends SequentialCommandGroup4905 {
  /** Creates a new TurnToHubAndRunEjectBelts. */
  private TurnToFieldElement m_turnCommand;
  private EjectBeltLeft m_ejectBeltCommand;

  public TurnToHubAndRunEjectBelts() {
    // Use addRequirements() here to declare subsystem dependencies.
    m_turnCommand = new TurnToFieldElement(Robot.getInstance().getFieldConstants().getHubPose());
    m_ejectBeltCommand = new EjectBeltLeft();
    addCommands(m_turnCommand, m_ejectBeltCommand);
  }

  // Called when the command is initially scheduled.

  @Override
  public void additionalInitialize() {

  }

}
