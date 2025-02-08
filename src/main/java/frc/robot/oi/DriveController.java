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
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.ShootLongShot;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.ShootShortShot;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.UnstickCargo;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
import frc.robot.commands.sbsdTeleOpCommands.teleOpCoralScoring;
import frc.robot.commands.showBotAudio.PlayAudio;
import frc.robot.commands.showBotAudio.PlayNextAudioFile;
import frc.robot.commands.showBotAudio.StopAudio;
import frc.robot.commands.showBotCannon.PressurizeCannon;
import frc.robot.commands.showBotCannon.ShootCannon;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.showBotAudio.AudioFiles;

/**
 * All driveController buttons get mapped here with descriptive names so they
 * are easier to find.
 */
public class DriveController extends ControllerBase {
  private JoystickButton m_turnOffLimelight;
  private SensorsContainer m_sensorsContainer;
  private SubsystemsContainer m_subsystemsContainer;

  public DriveController(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer) {
    setController(new XboxController(0));
    m_sensorsContainer = sensorsContainer;
    m_subsystemsContainer = subsystemsContainer;
    if (!Config4905.getConfig4905().getRobotName().equals("4905_Romi4")) {
      getPOVnorth().onTrue(new TurnToCompassHeading(() -> 0));
      getPOVeast().onTrue(new TurnToCompassHeading(() -> 90));
      getPOVsouth().onTrue(new TurnToCompassHeading(() -> 180));
      getPOVwest().onTrue(new TurnToCompassHeading(() -> 270));
    }
    getLeftStickButton().onTrue(new PauseRobot(1, m_subsystemsContainer.getDriveTrain()));
    getStartButton().onTrue(
        new CalibrateGyro(m_sensorsContainer.getGyro(), m_subsystemsContainer.getDriveTrain()));
    if (sensorsContainer.hasLimeLight()) {
      limeLightButtons();
    }
    if (Config4905.getConfig4905().isRomi()) {
      setupRomiButtons();
    }
    if (Config4905.getConfig4905().isTopGun()) {
      if (Config4905.getConfig4905().doesShooterExist()) {
        setUpShooterButtons();
      }
      if (Config4905.getConfig4905().doesIntakeExist()) {
        setUpIntakeButtons();
      }

    }
    if (Config4905.getConfig4905().doesShowBotCannonExist()) {
      setUpCannonButtons();
    }
    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")
        || Config4905.getConfig4905().getSwerveDrivetrainConfig().hasPath("parkingbrake")) {
      setUpParkingBrake();
    }

    if (Config4905.getConfig4905().doesShowBotAudioExist()) {
      setupShowBotAudioButtons();
    }

    if (Config4905.getConfig4905().getSensorConfig().hasPath("photonvision")) {
      // setUpPhotonVision();
    }
    if (Config4905.getConfig4905().isSwerveBot()) {
      setupSBSDTeleOpButtons();
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
    getXbutton().whileTrue(new ShootLongShot(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
    getYbutton().whileTrue(new ShootShortShot(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment()));
  }

  private void setUpIntakeButtons() {
    // A button = pick up cargo
    getAbutton().whileTrue(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), false));
    // B button = eject cargo
    getBbutton().whileTrue(new PickUpCargo(m_subsystemsContainer.getFeeder(),
        m_subsystemsContainer.getTopShooterWheel(), m_subsystemsContainer.getBottomShooterWheel(),
        m_subsystemsContainer.getShooterAlignment(), m_subsystemsContainer.getIntake(), true));
  }

  public boolean getTopGunEjectCargoButton() {
    return getBbutton().getAsBoolean();
  }

  protected void limeLightButtons() {
    m_turnOffLimelight = getStartButton();
    m_turnOffLimelight.onTrue(new ToggleLimelightLED(false, m_sensorsContainer));
  }

  private void setUpParkingBrake() {
    getBackButton().onTrue(new ToggleBrakes(m_subsystemsContainer.getDriveTrain()));
  }

  private void setUpCannonButtons() {
    getAbutton().onTrue(new PressurizeCannon());
    // the driver must hold down the shoot button while the count down is played. if
    // the driver
    // releases the button before the count down finishes, the cannon will not
    // shoot.
    getBbutton().whileTrue(new ShootCannon());
  }

  private void setupShowBotAudioButtons() {
    getBackButton()
        .onTrue(new PlayAudio(m_subsystemsContainer.getShowBotAudio(), AudioFiles.MeepMeep));
    getStartButton()
        .onTrue(new PlayAudio(m_subsystemsContainer.getShowBotAudio(), AudioFiles.TruckHorn));
    getXbutton().onTrue(new PlayNextAudioFile(m_subsystemsContainer.getShowBotAudio()));
    getYbutton().onTrue(new StopAudio(m_subsystemsContainer.getShowBotAudio()));
  }

  private void setupRomiButtons() {
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

  private void setupSBSDTeleOpButtons() {
    getAbutton()
        .whileTrue(new teleOpCoralScoring(m_subsystemsContainer.getDriveTrain()));
  }

  /*
   * private void setUpPhotonVision() { getAbutton().onTrue(new TurnToTarget(() ->
   * -1, () -> 0)); }
   */
}
