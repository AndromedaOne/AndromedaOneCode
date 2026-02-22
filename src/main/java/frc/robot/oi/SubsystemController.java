/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.ejectBeltCommands.EjectBeltLeft;
import frc.robot.commands.ejectBeltCommands.EjectBeltRight;
import frc.robot.commands.intakeCommands.IntakeRollerEjectCommand;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All subsystemController buttons get mapped here with descriptive names so
 * they are easier to find.
 */
public class SubsystemController extends ControllerBase {
  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    setController(new XboxController(1));
    if (Config4905.getConfig4905().isFuelRaider()) {
      setUpFuelRaiderButtons();
    }
  }

  public void setUpFuelRaiderButtons() {
    // back is LEFT, start is RIGHT
    // do note AHI gets its buttons in the command directly
    // intake roller buttons
    getBbutton().whileTrue(new IntakeRollerIntakeCommand());
    getYbutton().whileTrue(new IntakeRollerEjectCommand());
    // climber buttons
    /*
     * getXbutton().whileTrue(new ClimberExtensionCommand("ClimberRetract", true));
     * getStartButton().whileTrue(new ClimberExtensionCommand("ShortClimberExtend",
     * true)); getBackButton().whileTrue(new
     * ClimberExtensionCommand("LongClimberExtend", true));
     * getLeftBumperButton().whileTrue(new ClimberRotationCommand("ClimbDown"));
     * getRightBumperButton().whileTrue(new ClimberRotationCommand("ClimbUp"));
     */

    // eject belt buttons
    getPOVwest().whileTrue(new EjectBeltLeft());
    getPOVeast().whileTrue(new EjectBeltRight());
  }

  public void rumbleOn(double value) {
    setRumble(value);
  }

  public void rumbleOff() {
    setRumble(0);
  }

  public boolean getAButtonPressed() {
    return getAbutton().getAsBoolean();
  }
}
