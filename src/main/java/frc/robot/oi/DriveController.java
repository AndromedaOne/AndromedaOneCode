/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.lib.ButtonsEnumerated;

/**
 * Add your docs here.
 */
public class DriveController {
  private XboxController m_driveController = new XboxController(0);

  /**
   * Returns the position of the forward/backward stick with FORWARD being a
   * positive value to stick with our conventions.
   * 
   * @return The position of the left drive stick (up and down).
   */
  public double getForwardBackwardStick() {
    return deadband(-m_driveController.getY(GenericHID.Hand.kLeft));
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.01) {
      return 0.0;
    } else {
      return stickValue;
    }
  }

  /**
   * Returns the position of the left/right stick with LEFT being a positive value
   * to stick with our conventions.
   * 
   * @return the position of the right drive stick (left to right).
   */
  public double getRotateStick() {
    return deadband(-m_driveController.getX(GenericHID.Hand.kRight));
  }

  public JoystickButton getTestButton() {
    return ButtonsEnumerated.ABUTTON.getJoystickButton(m_driveController);
  }
}
