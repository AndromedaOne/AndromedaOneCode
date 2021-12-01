// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.lib.ButtonsEnumerated;
import frc.robot.lib.POVDirectionNames;

/** Add your docs here. */
public class ControllerBase {
  private XboxController m_controller;

  protected void setController(XboxController controller) {
    m_controller = controller;
  }

  // direct access to buttons is not allowed. to access a button, create
  // a public method with a name that describes the function being performed
  // this forces all buttons to be mapped in this file and makes it easier
  // to figure out what button is mapped to what function, instead of having
  // to search the code base to figure it out.

  protected JoystickButton getYbutton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.YBUTTON.getValue()));
  }

  protected JoystickButton getBbutton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.BBUTTON.getValue()));
  }

  protected JoystickButton getAbutton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.ABUTTON.getValue()));
  }

  protected JoystickButton getXbutton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.XBUTTON.getValue()));
  }

  protected JoystickButton getBackButton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.BACKBUTTON.getValue()));
  }

  protected JoystickButton getStartButton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.STARTBUTTON.getValue()));
  }

  protected POVButton getPOVnorth() {
    return (new POVButton(m_controller, POVDirectionNames.NORTH.getValue()));
  }

  protected POVButton getPOVsouth() {
    return (new POVButton(m_controller, POVDirectionNames.SOUTH.getValue()));
  }

  protected POVButton getPOVwest() {
    return (new POVButton(m_controller, POVDirectionNames.WEST.getValue()));
  }

  protected POVButton getPOVeast() {
    return (new POVButton(m_controller, POVDirectionNames.EAST.getValue()));
  }

  protected double getLeftStickForwardBackwardValue() {
    return deadband(-m_controller.getY(GenericHID.Hand.kLeft));
  }

  protected double getLeftStickLeftRightValue() {
    return deadband(-m_controller.getX(GenericHID.Hand.kLeft));
  }

  protected double getRightStickForwardBackwardValue() {
    return deadband(-m_controller.getY(GenericHID.Hand.kRight));
  }

  protected double getRightStickLeftRightValue() {
    return deadband(-m_controller.getX(GenericHID.Hand.kRight));
  }

  protected double getLeftTriggerValue() {
    return deadband(m_controller.getTriggerAxis(Hand.kLeft));
  }

  protected double getRightTriggerValue() {
    return deadband(m_controller.getTriggerAxis(Hand.kRight));
  }

  protected boolean getLeftBumperPressed() {
    return m_controller.getBumperPressed(Hand.kLeft);
  }

  protected boolean getLeftBumperReleased() {
    return m_controller.getBumperReleased(Hand.kLeft);
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.1) {
      return 0.0;
    } else {
      return stickValue;
    }
  }
}
