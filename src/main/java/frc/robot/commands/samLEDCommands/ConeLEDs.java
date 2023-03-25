// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samLEDCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ledlights.ConeOrCubeLEDsSingleton;
import frc.robot.subsystems.ledlights.LEDStates;
import frc.robot.subsystems.ledlights.RealLEDs;

public class ConeLEDs extends CommandBase {
  protected RealLEDs m_coneLEDs;
  protected ConeOrCubeLEDsSingleton m_ConeOrCubeLEDsSingleton;
  public static int m_ledState;

  /** Creates a new ConeLEDs. */
  public ConeLEDs() {
    m_ConeOrCubeLEDsSingleton = ConeOrCubeLEDsSingleton.getInstance();
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_ConeOrCubeLEDsSingleton.setButtonHeld(true);
    m_ConeOrCubeLEDsSingleton.setLEDStates(LEDStates.CONE);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_ConeOrCubeLEDsSingleton.setButtonHeld(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
