// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.oi;

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
  // this forces all buttons to be mapped in the controller file and makes it
  // easier
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

  protected boolean getBackButtonPressed() {
    return m_controller.getBackButtonPressed();
  }

  protected boolean getBackButtonReleased() {
    return m_controller.getBackButtonReleased();
  }

  protected JoystickButton getStartButton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.STARTBUTTON.getValue()));
  }

  protected POVButton getPOVnorth() {
    return (new POVButton(m_controller, POVDirectionNames.NORTH.getValue()));
  }

  protected POVButton getPOVnorthEast() {
    return (new POVButton(m_controller, POVDirectionNames.NORTHEAST.getValue()));
  }

  protected POVButton getPOVeast() {
    return (new POVButton(m_controller, POVDirectionNames.EAST.getValue()));
  }

  protected POVButton getPOVsouthEast() {
    return (new POVButton(m_controller, POVDirectionNames.SOUTHEAST.getValue()));
  }

  protected POVButton getPOVsouth() {
    return (new POVButton(m_controller, POVDirectionNames.SOUTH.getValue()));
  }

  protected POVButton getPOVsouthWest() {
    return (new POVButton(m_controller, POVDirectionNames.SOUTHWEST.getValue()));
  }

  protected POVButton getPOVwest() {
    return (new POVButton(m_controller, POVDirectionNames.WEST.getValue()));
  }

  protected POVButton getPOVnorthWest() {
    return (new POVButton(m_controller, POVDirectionNames.NORTHWEST.getValue()));
  }

  protected double getLeftStickForwardBackwardValue() {
    return deadband(-m_controller.getLeftY());
  }

  protected double getLeftStickLeftRightValue() {
    return deadband(-m_controller.getLeftX());
  }

  protected JoystickButton getLeftStickButton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.LEFTSTICKBUTTON.getValue()));
  }

  protected double getRightStickForwardBackwardValue() {
    return deadband(-m_controller.getRightY());
  }

  protected double getRightStickLeftRightValue() {
    return deadband(-m_controller.getRightX());
  }

  protected JoystickButton getRightStickButton() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.RIGHTSTICKBUTTON.getValue()));
  }

  protected double getLeftTriggerValue() {
    return deadband(m_controller.getLeftTriggerAxis());
  }

  protected boolean getLeftTriggerPressedBoolean() {
    if (deadband(m_controller.getLeftTriggerAxis()) >= 0.1) {
      return true;
    }
    return false;
  }

  protected JoystickButton getLeftTriggerPressed() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.LEFTTRIGGERBUTTON.getValue()));
  }

  protected double getRightTriggerValue() {
    return deadband(m_controller.getRightTriggerAxis());
  }

  protected boolean getRightTriggerPressedBoolean() {
    if (deadband(m_controller.getRightTriggerAxis()) >= 0.1) {
      return true;
    }
    return false;
  }

  protected JoystickButton getRightTriggerPressed() {
    return (new JoystickButton(m_controller, ButtonsEnumerated.RIGHTTRIGGERBUTTON.getValue()));
  }

  protected boolean getLeftBumperPressed() {
    return m_controller.getLeftBumper();
  }

  protected boolean getLeftBumperReleased() {
    return !m_controller.getLeftBumper();
  }

  protected JoystickButton getLeftBumperButton() {
    return new JoystickButton(m_controller, ButtonsEnumerated.LEFTBUMPERBUTTON.getValue());
  }

  protected boolean getRightBumperPressed() {
    return m_controller.getRightBumper();
  }

  protected boolean getRightBumperReleased() {
    return !m_controller.getRightBumper();
  }

  protected JoystickButton getRightBumperButton() {
    return new JoystickButton(m_controller, ButtonsEnumerated.RIGHTBUMPERBUTTON.getValue());
  }

  protected boolean getPOVnorthPressed() {
    return (m_controller.getPOV() == POVDirectionNames.NORTH.getValue());
  }

  protected boolean getPOVnorthEastPressed() {
    return (m_controller.getPOV() == POVDirectionNames.NORTHEAST.getValue());
  }

  protected boolean getPOVeastPressed() {
    return (m_controller.getPOV() == POVDirectionNames.EAST.getValue());
  }

  protected boolean getPOVnsouthEastPressed() {
    return (m_controller.getPOV() == POVDirectionNames.SOUTHEAST.getValue());
  }

  protected boolean getPOVsouthPressed() {
    return (m_controller.getPOV() == POVDirectionNames.SOUTH.getValue());
  }

  protected boolean getPOVsouthWestPressed() {
    return (m_controller.getPOV() == POVDirectionNames.SOUTHWEST.getValue());
  }

  protected boolean getPOVwestPressed() {
    return (m_controller.getPOV() == POVDirectionNames.WEST.getValue());
  }

  protected boolean getPOVnorthWestPressed() {
    return (m_controller.getPOV() == POVDirectionNames.NORTHWEST.getValue());
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.1) {
      return 0.0;
    } else {
      return stickValue;
    }
  }
}
