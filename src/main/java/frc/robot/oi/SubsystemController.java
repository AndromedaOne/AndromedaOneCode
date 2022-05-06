/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.shooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootFender;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootLaunchPad;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootTarmac;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootTerminal;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootWall;
import frc.robot.commands.shooterCommands.EndgameRotateAndExtendArms;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
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
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    }
  }

  private void setUpIntakeButtons() {
    getRightBumperButton().whileHeld(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), false));
    getBackButton().whileHeld(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), true));
  }

  private void setupShooterButtons() {

    // Y = fender, X = launchpad, A = wall, B = tarmac, POV East = terminal
    getYbutton().whileHeld(new ShootFender(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getXbutton().whileHeld(new ShootLaunchPad(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getAbutton().whileHeld(new ShootWall(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getBbutton().whileHeld(new ShootTarmac(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getPOVeast().whileHeld(new ShootTerminal(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getStartButton()
        .whenPressed(new EndgameRotateAndExtendArms(m_subsystemsContainer.getShooterAlignment()));
  }

  public void addEndGameButtons() {
    getLeftStickButton().whenPressed(
        new MoveShooterAlignment(m_subsystemsContainer.getShooterAlignment(), () -> 59));
  }

  public boolean getShootBackwardButtonPressed() {
    if (getLeftBumperPressed()) {
      return true;
    } else {
      return false;
    }
  }

  public boolean getShootLowHubButtonPressed() {
    if (getRightTriggerValue() > 0.3) {
      return true;
    } else {
      return false;
    }
  }

  public boolean getPauseFeederButtonPressed() {
    if (getLeftTriggerValue() > 0.3) {
      return true;
    } else {
      return false;
    }
  }

  public double getEndgameShooterAlignmentStick() {
    return getRightStickForwardBackwardValue();
  }
}
