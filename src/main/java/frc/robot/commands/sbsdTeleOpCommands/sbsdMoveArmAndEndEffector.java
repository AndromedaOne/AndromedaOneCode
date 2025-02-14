// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import java.util.function.IntSupplier;

import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class sbsdMoveArmAndEndEffector extends ParallelCommandGroup4905 {
  /** Creates a new sbsdMoveArmAndEndEffector. */
  boolean m_moved;
  sbsdMoveArm m_moveArm;
  sbsdMoveEndEffector m_moveEndEffector;

  public sbsdMoveArmAndEndEffector(IntSupplier level) {
    m_moveArm = new sbsdMoveArm(level);
    m_moveEndEffector = new sbsdMoveEndEffector(level);
    addCommands(m_moveArm, m_moveEndEffector);
  }

  @Override
  public void additionalInitialize() {

  }

  @Override
  public void additionalEnd(boolean moved) {
    moved = true;
    m_moved = moved;
    System.out.println("moved: " + moved);
  }
}