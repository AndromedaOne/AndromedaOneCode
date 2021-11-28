/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Config4905;
import frc.robot.lib.ButtonsEnumerated;
import frc.robot.subsystems.SubsystemsContainer;

public class SubsystemController {
  private XboxController m_subsystemController;

  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    if (Config4905.getConfig4905().isShowBot()) {
      m_subsystemController = new XboxController(1);
    }
  }

  public JoystickButton getDeployAndRunIntakeButton() {
    return ButtonsEnumerated.LEFTBUMPERBUTTON.getJoystickButton(m_subsystemController);
  }

  public double getLeftStickForwardBackwardValue() {
    return deadband(-m_subsystemController.getY(GenericHID.Hand.kLeft));
  }

  public double getRightStickForwardBackwardValue() {
    return deadband(-m_subsystemController.getY(GenericHID.Hand.kRight));
  }

  public double getLeftTriggerValue() {
    return deadband(m_subsystemController.getTriggerAxis(Hand.kLeft));
  }

  public double getRightTriggerValue() {
    return deadband(m_subsystemController.getTriggerAxis(Hand.kRight));
  }

  public double getRightStickLeftRightValue() {
    return deadband(-m_subsystemController.getX(GenericHID.Hand.kRight));
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.1) {
      return 0.0;
    } else {
      return stickValue;
    }
  }
}
