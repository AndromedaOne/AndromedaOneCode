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
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.BottomScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.OffFloorPickupPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.SubstationPickupPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.TopScorePosition;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.ShootThreePointer;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetract;
import frc.robot.commands.samLEDCommands.ConeLEDs;
import frc.robot.commands.samLEDCommands.CubeLEDs;
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
      System.out.println("Gripper is being run");
      setupGripperButtons();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    }
    if (Config4905.getConfig4905().doesSamArmRotateExist()) {
      setUpArmButtons();
    }
    if (Config4905.getConfig4905().doesRightLEDExist()
        || Config4905.getConfig4905().doesLeftLEDExist()) {
      setupLEDButtons();
    }
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }

  private void setUpIntakeButtons() {
    getLeftBumperButton().whileTrue(new PickUpCargo(m_subsystemsContainer.getFeeder(),
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

  private void setUpArmButtons() {
    getAbutton().onTrue(new OffFloorPickupPosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getYbutton().onTrue(new MiddleScorePosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getBbutton().onTrue(new TopScorePosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getXbutton().onTrue(new BottomScorePosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getPOVnorth().onTrue(new SubstationPickupPosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getLeftBumperButton().onTrue(new StowPosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getRightStickButton()
        .whileTrue(new ExtendRetract(m_subsystemsContainer.getArmExtRetBase(), false, false));
  }

  private void setupGripperButtons() {
    getRightBumperButton().onTrue(new OpenCloseGripper(m_subsystemsContainer.getGripper()));
  }

  private void setupLEDButtons() {
    getBackButton().whileTrue(new ConeLEDs());
    getStartButton().whileTrue(new CubeLEDs());
  }

  public boolean getGripperButtonPressed() {
    if (getRightBumperPressed() == true) {
      return true;
    } else {
      return false;
    }
  }

  public boolean getEjectCargoButton() {
    return getBackButton().getAsBoolean();
  }

  public boolean getGrabBackwardButton() {
    if (getLeftTriggerValue() > 0.3) {
      return true;
    }
    return false;
  }

  public boolean getConeButton() {
    if (getRightTriggerValue() > 0.3) {
      return true;
    }
    return false;
  }

  public double getArmExtendRetractJoystickValue() {
    return getRightStickForwardBackwardValue();
  }
}
