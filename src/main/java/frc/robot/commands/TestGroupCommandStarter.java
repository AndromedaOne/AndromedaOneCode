/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.groupcommands.TestGroupCommand;

public class TestGroupCommandStarter extends CommandBase {
  /**
   * Creates a new TestGroupCommandStarter.
   */
  BooleanSupplier m_isFinishedCondition;
  public TestGroupCommandStarter(BooleanSupplier isFinishedCondition) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_isFinishedCondition = isFinishedCondition;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("In Initialize");
    if(!m_isFinishedCondition.getAsBoolean()){
      System.out.println("Scheduling this now");
      CommandScheduler.getInstance().schedule(new TestGroupCommand(m_isFinishedCondition));
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
