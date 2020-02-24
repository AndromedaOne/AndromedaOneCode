/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.controlpanelmanipulator;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ControlPanelManipulatorBase extends SubsystemBase {
  /**
   * Creates a new ControlPanelBase.
   */
  public ControlPanelManipulatorBase() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public abstract void extendSystem();

  public abstract void retractSystem();

  public abstract void rotateOneTime();

  public abstract void rotateToColor(Color targetColor);

  public abstract void rotateThreeTimes();
}
