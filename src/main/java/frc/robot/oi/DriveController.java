/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Config4905;
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All driveController buttons get mapped here with descriptive names so they
 * are easier to find.
 */
public class DriveController extends ControllerBase {
  private SensorsContainer m_sensorsContainer;
  private SubsystemsContainer m_subsystemsContainer;

  public DriveController(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer) {
    setController(new XboxController(0));
    m_sensorsContainer = sensorsContainer;
    m_subsystemsContainer = subsystemsContainer;
    getLeftStickButton().onTrue(new PauseRobot(1, m_subsystemsContainer.getDriveTrain()));
    getStartButton().onTrue(
        new CalibrateGyro(m_sensorsContainer.getGyro(), m_subsystemsContainer.getDriveTrain()));
    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")
        || Config4905.getConfig4905().getSwerveDrivetrainConfig().hasPath("parkingbrake")) {
      setUpParkingBrake();
      // setUpFRButtons();
    }
    if (Config4905.getConfig4905().getSensorConfig().hasPath("photonvision")) {
      // setUpPhotonVision();
    }
    if (Config4905.getConfig4905().isFuelRaider()) {
      // setUpFRButtons();
    }
  }

  public boolean getAButtonPressed() {
    return getAbutton().getAsBoolean();
  }

  public boolean getBButtonPressed() {
    return getBbutton().getAsBoolean();
  }

  public boolean getUpArrowPressed() {
    return getPOVnorthPressed();
  }

  public boolean getLeftArrowPressed() {
    return getPOVwestPressed();
  }

  public boolean getDownArrowPressed() {
    return getPOVsouthPressed();
  }

  public boolean getRightArrowPressed() {
    return getPOVeastPressed();
  }

  public double getDriveTrainForwardBackwardStick() {
    return getLeftStickForwardBackwardValue();
  }

  public double getDriveTrainRotateStick() {
    return getRightStickLeftRightValue();
  }

  public boolean getDownShiftPressed() {
    return getLeftBumperPressed();
  }

  public boolean getUpShiftPressed() {
    return getRightBumperPressed();
  }

  public boolean getDownShiftReleased() {
    return getLeftBumperReleased();
  }

  public boolean getUpShiftReleased() {
    return getRightBumperReleased();
  }

  public void rumbleOn(double value) {
    setRumble(value);
  }

  public void rumbleOff() {
    setRumble(0);
  }

  private void setUpParkingBrake() {
    getBackButton().onTrue(new ToggleBrakes(m_subsystemsContainer.getDriveTrain()));
  }

  public double getSwerveDriveTrainTranslationAxis() {
    // XboxController.Axis.kLeftY.value;
    return getLeftStickForwardBackwardValue();
  }

  public double getSwerveDriveTrainStrafeAxis() {
    // XboxController.Axis.kLeftX.value;
    return getLeftStickLeftRightValue();
  }

  public double getSwerveDriveTrainRotationAxis() {
    // XboxController.Axis.kRightX.value;
    return getRightStickLeftRightValue();
  }

  public boolean getProtectedMode() {
    return getXbutton().getAsBoolean();
  }

  public boolean getCoralScoring() {
    return getXbutton().getAsBoolean();
  }

  public boolean getCoralLoadDriver() {
    return getPOVeast().getAsBoolean();
  }

  public boolean getCoralLoadWall() {
    return getPOVwest().getAsBoolean();
  }

  private void setUpFRButtons() {

  }
}
