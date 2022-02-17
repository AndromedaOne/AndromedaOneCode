/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.intakeCommands.DeployAndRunIntake;
import frc.robot.commands.FakeCommand;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All subsystemController buttons get mapped here with descriptive names so
 * they are easier to find.
 */
public class SubsystemController extends ControllerBase {
  private SubsystemsContainer m_subsystemsContainer;

  private SubsystemsContainer m_subsystemsContainer;

  public SubsystemController(SubsystemsContainer subsystemsContainer) {
    m_subsystemsContainer = subsystemsContainer;
    setController(new XboxController(1));
    m_subsystemsContainer = subsystemsContainer;
    if (Config4905.getConfig4905().doesShooterExist()) {
      setupShooterButtons();
    if (Config4905.getConfig4905().doesIntakeExist()) {
      setUpIntakeButtons();
    }
    }
  }

  public double getElevatorAdjustElevationStick() {
    return (getLeftStickForwardBackwardValue());
  }

  private void setupShooterButtons() {
    getYbutton().whileHeld(new FakeCommand());
    getXbutton().whileHeld(new FakeCommand());
    getAbutton().whileHeld(new FakeCommand());
    getBbutton().whileHeld(new FakeCommand());

  private void setUpIntakeButtons() {
    getRightBumperButton()
        .whenPressed(new DeployAndRunIntake(m_subsystemsContainer.getIntake(), false));
    getBackButton().whenPressed(new DeployAndRunIntake(m_subsystemsContainer.getIntake(), true));
  }

  public boolean getRunIntakeButtonReleased() {
    return getRightBumperReleased();
  }

  public boolean getRunIntakeinReverseButtonReleased() {
    return getBackButtonReleased();
  }
}
