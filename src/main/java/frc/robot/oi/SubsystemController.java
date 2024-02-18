/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Config4905;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All subsystemController buttons get mapped here with descriptive names so
 * they are easier to find.
 */
public class SubsystemController extends ControllerBase {
  private SubsystemsContainer m_subsystemsContainer;

  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    m_subsystemsContainer = subsystemsContainer;
    setController(new XboxController(1));

    if (Config4905.getConfig4905().doesRightLEDExist()
        || Config4905.getConfig4905().doesLeftLEDExist()) {
    }
    if (Config4905.getConfig4905().isBillthoven()) {
      setUpBillEndEffectorButtons();
    }
  }

  public JoystickButton getBillFeederButton() {
    // Runs the feeder without moving the arm
    return getRightStickButton();
  }

  public JoystickButton getBillFeederEjectNoteButton() {
    // Runs the feeder backwards
    return getRightBumperButton();
  }

  public JoystickButton getBillFeederIntakeNoteButton() {
    // Will move the arm down and run the feeder
    return getLeftTriggerPressed();
  }

  public JoystickButton getBillFeederTrapShotButton() {
    // Shoots the note into the trap
    return getStartButton();
  }

  private void setUpBillEndEffectorButtons() {
    getBillFeederEjectNoteButton()
        .whileTrue(new RunBillFeeder(m_subsystemsContainer.getBillFeeder(), FeederStates.EJECT));
    getBillFeederButton()
        .whileTrue(new RunBillFeeder(m_subsystemsContainer.getBillFeeder(), FeederStates.INTAKE));
    getBillFeederTrapShotButton().whileTrue(
        new RunBillFeeder(m_subsystemsContainer.getBillFeeder(), FeederStates.TRAPSHOOTING));
  }
}
