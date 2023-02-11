/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.SAMgripperCommands.OpenCloseGripper;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.ShootThreePointer;
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
    if (Config4905.getConfig4905().doesIntakeExist()) {
      setUpIntakeButtons();
    }
    if (Config4905.getConfig4905().doesGripperExist()) {
      setupGripperButtons();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    }
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }

  private void setUpIntakeButtons() {
    getRightBumperButton().whileTrue(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), false));
    getBackButton().whileTrue(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), true));
  }

  private void setupShooterButtons() {

    // Y = fender, X = launchpad, A = wall, B = tarmac, POV East = terminal
    getXbutton().whileTrue(new ShootThreePointer(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
  }

  private void setupGripperButtons() {
    int currentGripperState = m_subsystemsContainer.getGripper().getState();
    if (currentGripperState == 0) {
      currentGripperState = 1;
    } else {
      currentGripperState = 0;
    }
    getRightBumperButton()
        .whileTrue(new OpenCloseGripper(m_subsystemsContainer.getGripper(), currentGripperState));
  }

  public boolean getPauseFeederButtonPressed() {
    if (getLeftTriggerValue() > 0.3) {
      return true;
    } else {
      return false;
    }
  }

  public boolean getXButton() {
    return getXButton();
  }

  public boolean getEjectCargoButton() {
    return getBackButton().getAsBoolean();
  }
}
