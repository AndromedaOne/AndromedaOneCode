/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.FuelRaiderCommands.RunShooterRPM;
import frc.robot.commands.ejectBeltCommands.EjectBeltLeft;
import frc.robot.commands.ejectBeltCommands.EjectBeltRight;
import frc.robot.commands.groupCommands.ShootBasedOnDistance;
import frc.robot.commands.groupCommands.TurnToHubAndRunEjectBelts;
import frc.robot.commands.intakeCommands.IntakeRollerEjectCommand;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
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
    if (Config4905.getConfig4905().isFuelRaider()) {
      setUpFuelRaiderButtons();
    }
  }

  private void setUpFuelRaiderButtons() {
    // back is LEFT, start is RIGHT
    // do note AHI gets its buttons in the command directly
    // intake roller buttons
    getBbutton().whileTrue(new IntakeRollerIntakeCommand());
    getYbutton().whileTrue(new IntakeRollerEjectCommand());
    // shooter buttons
    // in order to shoot fuel, POV west (eject belt left) must be held
    getXbutton().whileTrue(new ShootBasedOnDistance());
    // eject belt buttons
    getPOVwest().whileTrue(new TurnToHubAndRunEjectBelts());
    getPOVeast().whileTrue(new EjectBeltRight());
    // shuttle shot buttons
    getLeftBumperButton().whileTrue(new EjectBeltLeft());
    // arbitrary RPM :)
    getRightBumperButton().whileTrue(new RunShooterRPM(m_subsystemsContainer.getBottomShooter(),
        () -> Config4905.getConfig4905().getShooterConfig().getDouble("bottomShooter.shuttleRPM"),
        m_subsystemsContainer.getTopShooter(),
        () -> Config4905.getConfig4905().getShooterConfig().getDouble("topShooter.shuttleRPM")));
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
