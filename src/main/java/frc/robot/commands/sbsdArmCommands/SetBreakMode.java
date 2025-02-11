// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SetBreakMode extends Command {
  private SBSDArmBase m_sbsdArmBase;
  private CoralEndEffectorRotateBase m_endEffector;
  private boolean m_brakeOn = true;

  public SetBreakMode(boolean brakeOn) {
    m_sbsdArmBase = Robot.getInstance().getSubsystemsContainer().getSBSDArmBase();
    m_endEffector = Robot.getInstance().getSubsystemsContainer().getSBSDCoralEndEffectorBase();
    addRequirements(m_sbsdArmBase.getSubsystemBase(), m_endEffector.getSubsystemBase());
    m_brakeOn = brakeOn;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_brakeOn) {
      m_sbsdArmBase.setBrakeMode();
      m_endEffector.setBrakeMode();
    } else {
      m_sbsdArmBase.setCoastMode();
      m_endEffector.setCoastMode();
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
