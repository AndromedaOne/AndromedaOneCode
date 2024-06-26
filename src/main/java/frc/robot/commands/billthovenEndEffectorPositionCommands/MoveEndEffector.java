// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenEndEffectorPositionCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.billthovenClimberCommands.BillClimberSingleton;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;

public class MoveEndEffector extends Command {
  BillEndEffectorPositionBase m_endEffectorPositionBase;
  BooleanSupplier m_toHighPosition;
  private boolean m_needToEnd;

  // toHighPosition: if true moves effector to hight position, low if false
  public MoveEndEffector(BillEndEffectorPositionBase endEffectorPositionBase,
      BooleanSupplier toHighPosition, boolean needToEnd) {
    addRequirements(endEffectorPositionBase.getSubsystemBase());
    m_endEffectorPositionBase = endEffectorPositionBase;
    m_toHighPosition = toHighPosition;
    m_needToEnd = needToEnd;
  }

  public MoveEndEffector(BillEndEffectorPositionBase endEffectorPositionBase,
      BooleanSupplier toHighPosition) {
    this(endEffectorPositionBase, toHighPosition, true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (BillClimberSingleton.getInstance().getClimberEnabled()) {
      return;
    }
    if (m_toHighPosition.getAsBoolean()) {
      m_endEffectorPositionBase.moveHighEndEffector();
    } else {
      m_endEffectorPositionBase.moveLowEndEffector();
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
    return m_needToEnd;
  }
}
