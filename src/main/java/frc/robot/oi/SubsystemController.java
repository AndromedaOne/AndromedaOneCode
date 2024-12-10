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
import frc.robot.commands.billthovenArmRotateCommands.ArmRotateCommand;
import frc.robot.commands.billthovenClimberCommands.DisableClimberMode;
import frc.robot.commands.billthovenClimberCommands.EnableClimberMode;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillAmpScore;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillSpeakerScore;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillTrapScore;
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

  public JoystickButton getBillFeederEjectNoteButton() {
    // Runs the feeder backwards
    return getRightBumperButton();
  }

  public JoystickButton getBillTrapShotButton() {
    // Shoots the note into the trap
    return getStartButton();
  }

  public JoystickButton getBillSpeakerCloseScoreButton() {
    // Scores the note into the speaker from close range
    return getXbutton();
  }

  public JoystickButton getBillSpeakerAwayScoreButton() {
    // Scores the note into the speaker from mid Range
    return getYbutton();
  }

  public JoystickButton getBillSpeakerShuttleScoreButton() {
    return getBbutton();
  }

  public JoystickButton getBillAmpScoreButton() {
    // Scores the note into the amp
    return getAbutton();
  }

  public JoystickButton getBillEnableClimberMode() {
    // Enables the climber buttons and disables the speaker and amp commands
    return getBackButton();
  }

  public JoystickButton getBillDisableClimberMode() {
    return getLeftStickButton();
  }

  public double getBillClimberSpeed() {
    return getLeftStickForwardBackwardValue();
  }

  public POVButton getBillArmRotateWhileClimb() {
    return getPOVnorth();
  }

  private void setUpBillEndEffectorButtons() {
    getBillFeederEjectNoteButton()
        .whileTrue(new RunBillFeeder(m_subsystemsContainer.getBillFeeder(), FeederStates.EJECT));
    // getBillFeederButton()
    // .whileTrue(new RunBillFeeder(m_subsystemsContainer.getBillFeeder(),
    // FeederStates.INTAKE));
    getBillTrapShotButton().onTrue(new BillTrapScore(m_subsystemsContainer.getBillArmRotate(),
        m_subsystemsContainer.getBillEffectorPosition(), m_subsystemsContainer.getBillFeeder(),
        m_subsystemsContainer.getBillShooter()));
    getBillFeederIntakeNoteButton().onTrue(new IntakeNote(m_subsystemsContainer.getBillArmRotate(),
        m_subsystemsContainer.getBillEffectorPosition(), m_subsystemsContainer.getBillFeeder(),
        m_subsystemsContainer.getBillShooter(), true));
    getBillSpeakerCloseScoreButton().onTrue(new BillSpeakerScore(
        m_subsystemsContainer.getBillArmRotate(), m_subsystemsContainer.getBillEffectorPosition(),
        m_subsystemsContainer.getBillFeeder(), m_subsystemsContainer.getBillShooter(),
        BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE));
    getBillSpeakerAwayScoreButton().onTrue(new BillSpeakerScore(
        m_subsystemsContainer.getBillArmRotate(), m_subsystemsContainer.getBillEffectorPosition(),
        m_subsystemsContainer.getBillFeeder(), m_subsystemsContainer.getBillShooter(),
        BillSpeakerScore.SpeakerScoreDistanceEnum.AWAY));
    getBillSpeakerShuttleScoreButton().onTrue(new BillSpeakerScore(
        m_subsystemsContainer.getBillArmRotate(), m_subsystemsContainer.getBillEffectorPosition(),
        m_subsystemsContainer.getBillFeeder(), m_subsystemsContainer.getBillShooter(),
        BillSpeakerScore.SpeakerScoreDistanceEnum.SHUTTLE));
    getBillAmpScoreButton().onTrue(new BillAmpScore(m_subsystemsContainer.getBillArmRotate(),
        m_subsystemsContainer.getBillEffectorPosition(), m_subsystemsContainer.getBillFeeder()));
    getBillEnableClimberMode().onTrue(new EnableClimberMode(m_subsystemsContainer.getBillClimber(),
        m_subsystemsContainer.getBillArmRotate()));
    getBillDisableClimberMode().onTrue(new DisableClimberMode(
        m_subsystemsContainer.getBillClimber(), m_subsystemsContainer.getBillArmRotate()));
    getBillArmRotateWhileClimb().onTrue(
        new ArmRotateCommand(m_subsystemsContainer.getBillArmRotate(), true, () -> 285, false));
  }
}
