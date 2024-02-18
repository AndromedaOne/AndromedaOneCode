// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

/** Add your docs here. */

public class LEDRobotInformation {
  private static boolean m_buttonheld;
  private static LEDRobotInformation m_instance = new LEDRobotInformation();
  private static boolean m_cannonIsPressurized = false;

  private LEDRobotInformation() {
  }

  public static LEDRobotInformation getInstance() {
    return m_instance;
  }

  public boolean getButtonHeld() {
    return m_buttonheld;
  }

  public void setButtonHeld(boolean buttonValue) {
    m_buttonheld = buttonValue;
  }

  public void setCannonIsPressurized(boolean value) {
    m_cannonIsPressurized = value;
  }

  public boolean getCannonIsPressurized() {
    return m_cannonIsPressurized;
  }
}
