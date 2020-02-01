/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * Add your docs here.
 */
public class SubsystemController {
  private Joystick m_subsystemController;
  private JoystickButton m_shootFromTrechFront;
  private JoystickButton m_shootFromTrechBack;
  private JoystickButton m_shootFromTrechInitiationLine;
  private JoystickButton m_shootWithLimeLight;

  public SubsystemController() {
    m_subsystemController = new Joystick(1);

    m_shootFromTrechFront = new JoystickButton(m_subsystemController, 1);
  }

}
