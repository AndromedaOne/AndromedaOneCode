/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Config4905;
import frc.robot.commands.sbsdArmCommands.ManualModeCoralScore;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All subsystemController buttons get mapped here with descriptive names so
 * they are easier to find.
 */
public class SubsystemController extends ControllerBase {
  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    setController(new XboxController(1));

    if (Config4905.getConfig4905().doesRightLEDExist()
        || Config4905.getConfig4905().doesLeftLEDExist()) {
    }
    if (Config4905.getConfig4905().isSBSD()) {
      setUpSBSDButtons();

    }
  }

  public void setUpSBSDButtons() {
    getLeftStickButton().whileTrue(new ManualModeCoralScore());

  }

  public JoystickButton getScoreLevelOne() {
    return getAbutton();
  }

  public JoystickButton getScoreLevelTwo() {
    return getXbutton();
  }

  public JoystickButton getScoreLevelThree() {
    return getBbutton();
  }

  public JoystickButton getScoreLevelFour() {
    return getYbutton();
  }

  public JoystickButton getScoreLeft() {
    return getLeftBumperButton();
  }

  public JoystickButton getScoreRight() {
    return getRightBumperButton();
  }

  public POVButton getPickupButtonAlgae() {
    return getPOVsouth();
  }

  public POVButton getScoreButtonAlgae() {
    return getPOVnorth();
  }
}
