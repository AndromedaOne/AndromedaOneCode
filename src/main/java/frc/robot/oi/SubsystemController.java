/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootFenderHigh;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootLaunchPadHigh;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootTarmacHigh;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootWallHigh;
import frc.robot.commands.groupCommands.shooterFeederCommands.StopShooterFeeder;
import frc.robot.commands.intakeCommands.DeployAndRunIntake;
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
    m_subsystemsContainer = subsystemsContainer;
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    }
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }

  private void setUpIntakeButtons() {
    getRightBumperButton()
        .whenPressed(new DeployAndRunIntake(m_subsystemsContainer.getIntake(), false));
    getBackButton().whenPressed(new DeployAndRunIntake(m_subsystemsContainer.getIntake(), true));
  }

  private void setupShooterButtons() {
    // Y = fender, X = launchpad, A = wall, B = tarmac
    getYbutton().whileHeld(new ShootFenderHigh(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getXbutton().whileHeld(new ShootLaunchPadHigh(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getAbutton().whileHeld(new ShootWallHigh(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getBbutton().whileHeld(new ShootTarmacHigh(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getYbutton().whenReleased(new StopShooterFeeder(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel()));
    getXbutton().whenReleased(new StopShooterFeeder(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel()));
    getAbutton().whenReleased(new StopShooterFeeder(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel()));
    getBbutton().whenReleased(new StopShooterFeeder(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel()));
  }

  public boolean getRunIntakeButtonReleased() {
    return getRightBumperReleased();
  }

  public boolean getRunIntakeinReverseButtonReleased() {
    return getBackButtonReleased();
  }
}
