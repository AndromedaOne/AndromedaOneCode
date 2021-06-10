/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Config4905;
import frc.robot.commands.BringWingsUp;
import frc.robot.commands.LetWingsDown;
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.TrackLineAndDriveBackwards;
import frc.robot.commands.TrackLineAndDriveForward;
import frc.robot.commands.climber.Climb;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.commands.romiBallMopper.ToggleMopper;
import frc.robot.lib.ButtonsEnumerated;
import frc.robot.lib.POVDirectionNames;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * Add your docs here.
 */
public class DriveController {
  private XboxController m_driveController = new XboxController(0);
  private JoystickButton turnToNorth;
  private JoystickButton turnToEast;
  private JoystickButton turnToSouth;
  private JoystickButton turnToWest;
  private JoystickButton turnToFace;
  private JoystickButton letOutLeftWinch;
  private JoystickButton letOutRightWinch;
  private POVButton climbLevel;
  private JoystickButton turnOnLimelight;
  private JoystickButton turnOffLimelight;
  private POVButton driveForwardAndTrackLine;
  private POVButton driveBackwardAndTrackLine;

  private SensorsContainer m_sensorsContainer;

  public DriveController(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer) {
    m_sensorsContainer = sensorsContainer;
    if (!Config4905.getConfig4905().isRomi()) {
      turnToNorth = new JoystickButton(m_driveController, ButtonsEnumerated.YBUTTON.getValue());
      turnToNorth.whenPressed(new TurnToCompassHeading(0));
      turnToEast = new JoystickButton(m_driveController, ButtonsEnumerated.BBUTTON.getValue());
      turnToEast.whenPressed(new TurnToCompassHeading(90));
      turnToSouth = new JoystickButton(m_driveController, ButtonsEnumerated.ABUTTON.getValue());
      turnToSouth.whenPressed(new TurnToCompassHeading(180));
      turnToWest = new JoystickButton(m_driveController, ButtonsEnumerated.XBUTTON.getValue());
      turnToWest.whenPressed(new TurnToCompassHeading(270));
    }

    if (sensorsContainer.hasLimeLight()) {
      limeLightButtons();
    }
    if (Config4905.getConfig4905().doesClimberExist()) {
      climberButtons();
    }

    // set up romi buttons has to be last.
    if (Config4905.getConfig4905().isRomi()) {
      setupRomiButtons();
    }
  }

  /**
   * Returns the position of the forward/backward stick with FORWARD being a
   * positive value to stick with our conventions.
   * 
   * @return The position of the left drive stick (up and down).
   */
  public double getForwardBackwardStick() {
    return deadband(-m_driveController.getY(GenericHID.Hand.kLeft));
  }

  /**
   * Returns the position of the left/right stick with LEFT being a positive value
   * to stick with our conventions.
   * 
   * @return the position of the right drive stick (left to right).
   */
  public double getRotateStick() {
    return deadband(-m_driveController.getX(GenericHID.Hand.kRight));
  }

  public double getLeftTriggerValue() {
    return deadband(m_driveController.getTriggerAxis(Hand.kLeft));
  }

  public double getRightTriggerValue() {
    return deadband(m_driveController.getTriggerAxis(Hand.kRight));
  }

  public boolean getLeftBumperPressed() {
    return m_driveController.getBumperPressed(Hand.kLeft);
  }

  public boolean getLeftBumperReleased() {
    return m_driveController.getBumperReleased(Hand.kLeft);
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.01) {
      return 0.0;
    } else {
      return stickValue;
    }
  }

  protected void limeLightButtons() {
    turnToFace = new JoystickButton(m_driveController, ButtonsEnumerated.RIGHTBUMPERBUTTON.getValue());
    turnToFace.whenPressed(new TurnToFaceCommand(m_sensorsContainer.getLimeLight()::horizontalDegreesToTarget));
    turnOnLimelight = new JoystickButton(m_driveController, ButtonsEnumerated.BACKBUTTON.getValue());
    turnOnLimelight.whenPressed(new ToggleLimelightLED(true, m_sensorsContainer));
    turnOffLimelight = new JoystickButton(m_driveController, ButtonsEnumerated.STARTBUTTON.getValue());
    turnOffLimelight.whenPressed(new ToggleLimelightLED(false, m_sensorsContainer));
  }

  // Climber buttons
  protected void climberButtons() {
    climbLevel = new POVButton(m_driveController, POVDirectionNames.NORTH.getValue());
    climbLevel.whileHeld(new Climb());
    letOutLeftWinch = new JoystickButton(m_driveController, ButtonsEnumerated.LEFTSTICKBUTTON.getValue());
    letOutRightWinch = new JoystickButton(m_driveController, ButtonsEnumerated.RIGHTSTICKBUTTON.getValue());
  }

  public JoystickButton getLetOutLeftWinchButton() {
    return letOutLeftWinch;
  }

  public JoystickButton getLetOutRightWinchButton() {
    return letOutRightWinch;
  }

  private void setupRomiButtons() {
    JoystickButton mopperButton = new JoystickButton(m_driveController, ButtonsEnumerated.XBUTTON.getValue());
    mopperButton.whenPressed(new ToggleMopper());
    driveForwardAndTrackLine = new POVButton(m_driveController, POVDirectionNames.NORTH.getValue());
    driveForwardAndTrackLine.whileHeld(new TrackLineAndDriveForward());
    driveBackwardAndTrackLine = new POVButton(m_driveController, POVDirectionNames.SOUTH.getValue());
    driveBackwardAndTrackLine.whileHeld(new TrackLineAndDriveBackwards());
    POVButton letRomiWingsDownButton = new POVButton(m_driveController, POVDirectionNames.WEST.getValue());
    letRomiWingsDownButton.whileHeld(new LetWingsDown());
    POVButton bringRomiWingsUpButton = new POVButton(m_driveController, POVDirectionNames.EAST.getValue());
    bringRomiWingsUpButton.whileHeld(new BringWingsUp());
  }
}
