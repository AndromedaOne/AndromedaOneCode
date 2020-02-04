/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.PrintTime;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TestGroupCommand extends SequentialCommandGroup {
  /**
   * Creates a new TestGroupCommand.
   */
  BooleanSupplier m_isFinishedCondition;
  public TestGroupCommand(BooleanSupplier isFinishedCondition) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    addCommands(new PrintTime(true), new PrintTime(false));
    m_isFinishedCondition = isFinishedCondition;
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    super.initialize();
  }
  @Override
  public boolean isFinished() {
    // TODO Auto-generated method stub
    return m_isFinishedCondition.getAsBoolean();
  }
}
