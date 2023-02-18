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
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.LowScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
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
      System.out.println("Gripper is being run");
      setupGripperButtons();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    }
    if (Config4905.getConfig4905().doesSamArmRotateExist()) {
      setUpArmButtons();
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
    getXbutton().onTrue(new LowScorePosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
    getYbutton().onTrue(new MiddleScorePosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
//    getBbutton().onTrue(new TopScorePosition(m_subsystemsContainer.getArmRotateBase(),
//        m_subsystemsContainer.getArmExtRetBase()));
//    getAbutton().onTrue(new SubstationPickupPosition(m_subsystemsContainer.getArmRotateBase(),
//        m_subsystemsContainer.getArmExtRetBase()));
    getLeftBumperButton().onTrue(new StowPosition(m_subsystemsContainer.getArmRotateBase(),
        m_subsystemsContainer.getArmExtRetBase()));
  }

  private void setupGripperButtons() {
    getRightBumperButton().onTrue(new OpenCloseGripper(m_subsystemsContainer.getGripper()));
  }

  public boolean getPauseFeederButtonPressed() {
    if (getLeftTriggerValue() > 0.3) {
      return true;
    } else {
      return false;
    }
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

  public boolean getGrabForwardButton() {
    if (getLeftTriggerValue() > 0.3) {
      return true;
    }
    return false;
  }

}
