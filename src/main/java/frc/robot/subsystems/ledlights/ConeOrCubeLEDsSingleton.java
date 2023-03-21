// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

/** Add your docs here. */
public class ConeOrCubeLEDsSingleton {
  private static ConeOrCubeLEDsSingleton m_instance = new ConeOrCubeLEDsSingleton();

  private ConeOrCubeLEDsSingleton() {
  }

  public static ConeOrCubeLEDsSingleton getInstance() {
    return m_instance;
  }

}
