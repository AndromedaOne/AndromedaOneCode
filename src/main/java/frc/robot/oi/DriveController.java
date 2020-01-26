/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Add your docs here.
 */
public class DriveController {
  private Joystick m_driveController = new Joystick(0);

  /*
   * Below are a bunch of getters for the drive controller and its joystick
   * values. getForwardBackwardStick is negative to ensure pushing the stick
   * FORWARD (positive) returns a positive value.
   */

  public Joystick getDriveController() {
    return m_driveController;
  }

  /**
   * Returns the position of the forward/backward stick with FORWARD being a
   * positive value to stick with our conventions.
   * 
   * @return The position of the left drive stick (up and down).
   */
  public double getForwardBackwardStick() {
    return -m_driveController.getY(GenericHID.Hand.kLeft);
  }

  /**
   * Returns the position of the left/right stick with LEFT being a positive value
   * to stick with our conventions.
   * 
   * @return the position of the right drive stick (left to right).
   */
  public double getRotateStick() {
    return -m_driveController.getX(GenericHID.Hand.kRight);
  }

}
