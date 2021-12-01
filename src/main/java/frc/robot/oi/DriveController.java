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
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.commands.romiCommands.BringWingsUp;
import frc.robot.commands.romiCommands.LetWingsDown;
import frc.robot.commands.romiCommands.ToggleConveyor;
import frc.robot.commands.romiCommands.TrackLineAndDriveBackwards;
import frc.robot.commands.romiCommands.TrackLineAndDriveForward;
import frc.robot.commands.romiCommands.romiBallMopper.ToggleMopper;
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
  private JoystickButton turnOnLimelight;
  private JoystickButton turnOffLimelight;
  private POVButton driveForwardAndTrackLine;
  private POVButton driveBackwardAndTrackLine;
  private JoystickButton toggleConveyor;
  private SensorsContainer m_sensorsContainer;
  private SubsystemsContainer m_subsystemsContainer;

  public DriveController(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer) {
    m_sensorsContainer = sensorsContainer;
    m_subsystemsContainer = subsystemsContainer;
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
    if (Config4905.getConfig4905().isRomi()) {
      setupRomiButtons();
    }
  }

  public double getDriveTrainForwardBackwardStick() {
    return getForwardBackwardStick();
  }

  public double getDriveTrainRotateStick() {
    return getRotateStick();
  }

  public boolean getSlowModeBumperPressed() {
    return getLeftBumperPressed();
  }

  protected void limeLightButtons() {
    turnOnLimelight = new JoystickButton(m_driveController, ButtonsEnumerated.BACKBUTTON.getValue());
    turnOnLimelight.whenPressed(new ToggleLimelightLED(true, m_sensorsContainer));
    turnOffLimelight = new JoystickButton(m_driveController, ButtonsEnumerated.STARTBUTTON.getValue());
    turnOffLimelight.whenPressed(new ToggleLimelightLED(false, m_sensorsContainer));
  }

  private void setupRomiButtons() {
    if (Config4905.getConfig4905().getRobotName().equals("4905_Romi")) {
      driveForwardAndTrackLine = new POVButton(m_driveController, POVDirectionNames.NORTH.getValue());
      driveForwardAndTrackLine.whileHeld(new TrackLineAndDriveForward());
      driveBackwardAndTrackLine = new POVButton(m_driveController, POVDirectionNames.SOUTH.getValue());
      driveBackwardAndTrackLine.whileHeld(new TrackLineAndDriveBackwards());
      POVButton letRomiWingsDownButton = new POVButton(m_driveController, POVDirectionNames.WEST.getValue());
      letRomiWingsDownButton.whileHeld(new LetWingsDown());
      POVButton bringRomiWingsUpButton = new POVButton(m_driveController, POVDirectionNames.EAST.getValue());
      bringRomiWingsUpButton.whileHeld(new BringWingsUp());
      JoystickButton mopperButton = new JoystickButton(m_driveController, ButtonsEnumerated.XBUTTON.getValue());
      mopperButton.whenPressed(new ToggleMopper());
    }
    if (Config4905.getConfig4905().getRobotName().equals("4905_Romi2")) {
      toggleConveyor = new JoystickButton(m_driveController, ButtonsEnumerated.XBUTTON.getValue());
      toggleConveyor.whenPressed(new ToggleConveyor(m_subsystemsContainer.getConveyor(), 1));
    }
  }

  // direct access to buttons is not allowed. to access a button, create
  // a public method with a name that describes the function being performed
  // this forces all buttons to be mapped in this file and makes it easier
  // to figure out what button is mapped to what function, instead of having
  // to search the code base to figure it out.

  /**
   * Returns the position of the forward/backward stick with FORWARD being a
   * positive value to stick with our conventions.
   * 
   * @return The position of the left drive stick (up and down).
   */
  private double getForwardBackwardStick() {
    return deadband(-m_driveController.getY(GenericHID.Hand.kLeft));
  }

  /**
   * Returns the position of the left/right stick with LEFT being a positive value
   * to stick with our conventions.
   *
   * @return the position of the right drive stick (left to right).
   */
  private double getRotateStick() {
    return deadband(-m_driveController.getX(GenericHID.Hand.kRight));
  }

  private double getLeftTriggerValue() {
    return deadband(m_driveController.getTriggerAxis(Hand.kLeft));
  }

  private double getRightTriggerValue() {
    return deadband(m_driveController.getTriggerAxis(Hand.kRight));
  }

  private boolean getLeftBumperPressed() {
    return m_driveController.getBumperPressed(Hand.kLeft);
  }

  private boolean getLeftBumperReleased() {
    return m_driveController.getBumperReleased(Hand.kLeft);
  }

  private double deadband(double stickValue) {
    if (Math.abs(stickValue) < 0.01) {
      return 0.0;
    } else {
      return stickValue;
    }
  }
}
