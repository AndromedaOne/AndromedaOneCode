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
import frc.robot.commands.climberCommands.contractArmsCommand;
import frc.robot.commands.climberCommands.extendArmsCommand;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
import frc.robot.commands.romiCommands.BringWingsUp;
import frc.robot.commands.romiCommands.LetWingsDown;
import frc.robot.commands.romiCommands.ToggleConveyor;
import frc.robot.commands.romiCommands.TrackLineAndDriveBackwards;
import frc.robot.commands.romiCommands.TrackLineAndDriveForward;
import frc.robot.commands.romiCommands.romiBallMopper.ToggleMopper;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All driveController buttons get mapped here with descriptive names so they
 * are easier to find.
 */
public class DriveController extends ControllerBase {
  private JoystickButton m_turnOnLimelight;
  private JoystickButton m_turnOffLimelight;
  private POVButton m_driveForwardAndTrackLine;
  private POVButton m_driveBackwardAndTrackLine;
  private JoystickButton m_toggleConveyor;
  private SensorsContainer m_sensorsContainer;
  private SubsystemsContainer m_subsystemsContainer;

  public DriveController(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer) {
    setController(new XboxController(0));
    m_sensorsContainer = sensorsContainer;
    m_subsystemsContainer = subsystemsContainer;
    if (!Config4905.getConfig4905().getRobotName().equals("4905_Romi4")) {
      getPOVnorth().whenPressed(new TurnToCompassHeading(0));
      getPOVeast().whenPressed(new TurnToCompassHeading(90));
      getPOVsouth().whenPressed(new TurnToCompassHeading(180));
      getPOVwest().whenPressed(new TurnToCompassHeading(270));
    }
    if (sensorsContainer.hasLimeLight()) {
      limeLightButtons();
    }
    if (Config4905.getConfig4905().isRomi()) {
      setupRomiButtons();
    }
    if (Config4905.getConfig4905().doesClimberExist()) {
      setUpClimberButtons();
    }

  }

  public double getDriveTrainForwardBackwardStick() {
    return getLeftStickForwardBackwardValue();
  }

  public double getDriveTrainRotateStick() {
    return getRightStickLeftRightValue();
  }

  public boolean getSlowModeBumperPressed() {
    return getLeftBumperPressed();
  }

  public boolean getSlowModeBumperReleased() {
    return getLeftBumperReleased();
  }

  private void setUpClimberButtons() {
    getBbutton().whenPressed(new extendArmsCommand());
    getAbutton().whenPressed(new contractArmsCommand());
  }

  protected void limeLightButtons() {
    m_turnOnLimelight = getBackButton();
    m_turnOnLimelight.whenPressed(new ToggleLimelightLED(true, m_sensorsContainer));
    m_turnOffLimelight = getStartButton();
    m_turnOffLimelight.whenPressed(new ToggleLimelightLED(false, m_sensorsContainer));
  }

  private void setupRomiButtons() {
    if (Config4905.getConfig4905().getRobotName().equals("4905_Romi4")) {
      m_driveForwardAndTrackLine = getPOVnorth();
      m_driveForwardAndTrackLine.whileHeld(new TrackLineAndDriveForward());
      m_driveBackwardAndTrackLine = getPOVsouth();
      m_driveBackwardAndTrackLine.whileHeld(new TrackLineAndDriveBackwards());
      POVButton letRomiWingsDownButton = getPOVwest();
      letRomiWingsDownButton.whileHeld(new LetWingsDown());
      POVButton bringRomiWingsUpButton = getPOVeast();
      bringRomiWingsUpButton.whileHeld(new BringWingsUp());
      JoystickButton mopperButton = getXbutton();
      mopperButton.whenPressed(new ToggleMopper());
    }
    if (Config4905.getConfig4905().getRobotName().equals("4905_Romi2")) {
      m_toggleConveyor = getYbutton();
      m_toggleConveyor.whenPressed(new ToggleConveyor(m_subsystemsContainer.getConveyor(), 1));
    }
  }
}
