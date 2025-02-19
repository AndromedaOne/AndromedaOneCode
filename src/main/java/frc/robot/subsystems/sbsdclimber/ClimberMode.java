// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdclimber;

/** Add your docs here. */
public class ClimberMode {
  private static ClimberMode m_climberMode = new ClimberMode();
  private boolean m_armInClimberMode = false;
  private boolean m_endEffectorInClimberMode = false;

  private ClimberMode() {

  }

  public static ClimberMode getInstance() {
    return m_climberMode;
  }

  public boolean getInClimberMode() {
    return m_armInClimberMode && m_endEffectorInClimberMode;
  }

  public void setArmInClimberMode() {
    m_armInClimberMode = true;
  }

  public void setEndEffectorInClimberMode() {
    m_endEffectorInClimberMode = true;
  }
}
