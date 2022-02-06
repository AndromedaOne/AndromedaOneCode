/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.SubsystemsContainer;

public class SubsystemController extends ControllerBase {

  private JoystickButton m_DeployAndRunIntakeReverse;
  private JoystickButton m_DeployAndRunIntake;
  private JoystickButton m_RetractAndStopIntake;

  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    setController(new XboxController(1));
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }
}
