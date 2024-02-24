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
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillAmpScore;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillSpeakerScore;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.IntakeNote;
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

  public JoystickButton getBillFeederIntakeNoteButton() {
    // Runs the feeder without moving the arm
    return getRightStickButton();
  }

  public boolean getBillFireTrigger() {
    // Moves note into shooter motors
    return getRightTriggerPressedBoolean();
  }

  public JoystickButton getBillShootingPositionButton() {
    // Changes the shooting posistion from low to high
    return getLeftBumperButton();
  }

  public double getBillClimberStick() {
    // Moves the climber up and down
    return getLeftStickForwardBackwardValue();
  }

  public JoystickButton getBillFeederEjectNoteButton() {
    // Runs the feeder backwards
    return getRightBumperButton();
  }

  public JoystickButton getBillFeederTrapShotButton() {
    // Shoots the note into the trap
    return getStartButton();
  }

  public JoystickButton getBillSpeakerCloseScoreButton() {
    // Scores the note into the speaker from close range
    return getXbutton();
  }

  public JoystickButton getBillSpeakerMidScoreButton() {
    // Scores the note into the speaker from Mid Range
    return getBbutton();
  }

  public JoystickButton getBillSpeakerFarScoreButton() {
    // Scores the note into the speaker from far range
    return getYbutton();
  }

  public JoystickButton getBillAmpScoreButton() {
    // Scores the note into the amp
    return getAbutton();
  }

  private void setUpBillEndEffectorButtons() {
    getBillFeederEjectNoteButton()
        .whileTrue(new RunBillFeeder(m_subsystemsContainer.getBillFeeder(), FeederStates.EJECT));
    // getBillFeederButton()
    // .whileTrue(new RunBillFeeder(m_subsystemsContainer.getBillFeeder(),
    // FeederStates.INTAKE));
    getBillFeederTrapShotButton().onTrue(
        new RunBillFeeder(m_subsystemsContainer.getBillFeeder(), FeederStates.TRAPSHOOTING));
    getBillFeederIntakeNoteButton().onTrue(new IntakeNote(m_subsystemsContainer.getBillArmRotate(),
        m_subsystemsContainer.getBillEffectorPosition(), m_subsystemsContainer.getBillFeeder()));
    getBillSpeakerCloseScoreButton().onTrue(new BillSpeakerScore(
        m_subsystemsContainer.getBillArmRotate(), m_subsystemsContainer.getBillEffectorPosition(),
        m_subsystemsContainer.getBillFeeder(), m_subsystemsContainer.getBillShooter(),
        BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE));
    getBillSpeakerMidScoreButton().onTrue(new BillSpeakerScore(
        m_subsystemsContainer.getBillArmRotate(), m_subsystemsContainer.getBillEffectorPosition(),
        m_subsystemsContainer.getBillFeeder(), m_subsystemsContainer.getBillShooter(),
        BillSpeakerScore.SpeakerScoreDistanceEnum.MID));
    getBillSpeakerFarScoreButton().onTrue(new BillSpeakerScore(
        m_subsystemsContainer.getBillArmRotate(), m_subsystemsContainer.getBillEffectorPosition(),
        m_subsystemsContainer.getBillFeeder(), m_subsystemsContainer.getBillShooter(),
        BillSpeakerScore.SpeakerScoreDistanceEnum.FAR));
    getBillAmpScoreButton().onTrue(new BillAmpScore(m_subsystemsContainer.getBillArmRotate(),
        m_subsystemsContainer.getBillEffectorPosition(), m_subsystemsContainer.getBillFeeder()));

  }
}
