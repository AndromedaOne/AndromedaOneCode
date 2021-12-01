/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.subsystems.SubsystemsContainer;

public class SubsystemController {
  private XboxController m_subsystemController;

  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    if (Config4905.getConfig4905().isShowBot()) {
      m_subsystemController = new XboxController(1);
    }
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }

  // direct access to buttons is not allowed. to access a button, create
  // a public method with a name that describes the function being performed
  // this forces all buttons to be mapped in this file and makes it easier
  // to figure out what button is mapped to what function, instead of having
  // to search the code base to figure it out.
  private double getLeftStickForwardBackwardValue() {
    return deadband(-m_subsystemController.getY(GenericHID.Hand.kLeft));
  }

  private double getRightStickForwardBackwardValue() {
    return deadband(-m_subsystemController.getY(GenericHID.Hand.kRight));
  }

  private double getLeftTriggerValue() {
    return deadband(m_subsystemController.getTriggerAxis(Hand.kLeft));
  }

  private double getRightTriggerValue() {
    return deadband(m_subsystemController.getTriggerAxis(Hand.kRight));
  }

  private double getRightStickLeftRightValue() {
    return deadband(-m_subsystemController.getX(GenericHID.Hand.kRight));
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.1) {
      return 0.0;
    } else {
      return stickValue;
    }
  }
}
