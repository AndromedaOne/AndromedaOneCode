// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import java.util.function.DoubleSupplier;

/** Add your docs here. */
public class ArmRotationExtensionSingleton {
  private static ArmRotationExtensionSingleton m_instance = new ArmRotationExtensionSingleton();
  private double m_angle = 180;
  private double m_position = 0;

  private ArmRotationExtensionSingleton() {
  }

  public static ArmRotationExtensionSingleton getInstance() {
    return m_instance;
  }

  public DoubleSupplier getAngle() {
    return () -> m_angle;
  }

  public DoubleSupplier getPosition() {
    return () -> m_position;
  }

  public void setAngle(double angle) {
    m_angle = angle;
  }

  public void setPosition(double position) {
    if (position < 0) {
      m_position = 0;
    } else {
      m_position = position;
    }
  }
}
