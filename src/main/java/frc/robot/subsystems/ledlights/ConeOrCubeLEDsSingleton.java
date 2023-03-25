// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

/** Add your docs here. */

public class ConeOrCubeLEDsSingleton {
  private static boolean m_buttonheld;
  private LEDStates m_LedStates;
  private static ConeOrCubeLEDsSingleton m_instance = new ConeOrCubeLEDsSingleton();

  public ConeOrCubeLEDsSingleton() {
  }

  public static ConeOrCubeLEDsSingleton getInstance() {
    return m_instance;
  }

  public boolean getButtonHeld() {
    return m_buttonheld;
  }

  public void setButtonHeld(boolean buttonValue) {
    m_buttonheld = buttonValue;
  }

  public LEDStates getLEDStates() {
    return m_LedStates;
  }

  public void setLEDStates(LEDStates LEDValue) {
    m_LedStates = LEDValue;
  }
}
