// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenClimberCommands;

/** Add your docs here. */
public class BillClimberSingleton {
  private boolean m_isClimberEnabled = false;
  private static BillClimberSingleton m_instance;

  private BillClimberSingleton() {

  }

  public static BillClimberSingleton getInstance() {
    if (m_instance == null) {
      m_instance = new BillClimberSingleton();
    }
    return m_instance;
  }

  public void setClimberEnabled() {
    m_isClimberEnabled = true;
  }

  public void setClimberDisabled() {
    m_isClimberEnabled = false;
  }

  public boolean getClimberEnabled() {
    return m_isClimberEnabled;
  }
}
