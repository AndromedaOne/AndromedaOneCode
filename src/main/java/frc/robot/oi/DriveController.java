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
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.UnstickCargo;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
import frc.robot.commands.showBotCannon.PressurizeCannon;
import frc.robot.commands.showBotCannon.ShootCannon;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * All driveController buttons get mapped here with descriptive names so they
 * are easier to find.
 */
public class DriveController extends ControllerBase {
  private JoystickButton m_turnOnLimelight;
  private JoystickButton m_turnOffLimelight;
  private SensorsContainer m_sensorsContainer;
  private SubsystemsContainer m_subsystemsContainer;

  public DriveController(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer) {
    setController(new XboxController(0));
    m_sensorsContainer = sensorsContainer;
    m_subsystemsContainer = subsystemsContainer;
    if (!Config4905.getConfig4905().getRobotName().equals("4905_Romi4")) {
      getPOVnorth().onTrue(new TurnToCompassHeading(0));
      getPOVeast().onTrue(new TurnToCompassHeading(90));
      getPOVsouth().onTrue(new TurnToCompassHeading(180));
      getPOVwest().onTrue(new TurnToCompassHeading(270));
    }
    if (sensorsContainer.hasLimeLight()) {
      limeLightButtons();
    }
    if (Config4905.getConfig4905().isRomi()) {
      setupRomiButtons();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      setUpShooterButtons();
    }
    if (Config4905.getConfig4905().doesShowBotCannonExist()) {
      setUpCannonButtons();
      }
    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")) {
      setUpParkingBrake();
    }
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

  public double getShowBotElevatorUpTriggerValue() {
    return getLeftTriggerValue();
  }

  public double getShowBotElevatorDownTriggerValue() {
    return getRightTriggerValue();
  }

  private void setUpShooterButtons() {
    getBackButton().whileTrue(new UnstickCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake()));
  }

  protected void limeLightButtons() {
    m_turnOnLimelight = getBackButton();
    m_turnOnLimelight.onTrue(new ToggleLimelightLED(true, m_sensorsContainer));
    m_turnOffLimelight = getStartButton();
    m_turnOffLimelight.onTrue(new ToggleLimelightLED(false, m_sensorsContainer));
  }

  private void setUpParkingBrake() {
    getBackButton().onTrue(new ToggleBrakes(m_subsystemsContainer.getDrivetrain()));
  }
  private void setUpCannonButtons() {
    getAbutton().onTrue(new PressurizeCannon());
    getBbutton().onTrue(new ShootCannon());
  }
}
