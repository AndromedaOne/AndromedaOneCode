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
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.sbsdClimberCommands.SBSDClimb;
import frc.robot.commands.sbsdTeleOpCommands.GetInClimberMode;
import frc.robot.commands.sbsdTeleOpCommands.NotInUnsafeZone;
import frc.robot.commands.sbsdTeleOpCommands.sbsdCoralLoadArmEndEffectorPositon;
import frc.robot.commands.sbsdTeleOpCommands.teleOpCoralScoring;
import frc.robot.commands.sbsdTeleOpCommands.teleOpDriverCoralPickup;
import frc.robot.commands.sbsdTeleOpCommands.teleOpWallCoralPickup;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
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
    if (Config4905.getConfig4905().getRobotName().equals("SBSD")
        || Config4905.getConfig4905().getRobotName().equals("SwerveBot")) {
      setupSBSDTeleOpButtons();
    } else {
      getPOVnorth().onTrue(new TurnToCompassHeading(() -> 0));
      getPOVeast().onTrue(new TurnToCompassHeading(() -> 90));
      getPOVsouth().onTrue(new TurnToCompassHeading(() -> 180));
      getPOVwest().onTrue(new TurnToCompassHeading(() -> 270));
    }
    getLeftStickButton().onTrue(new PauseRobot(1, m_subsystemsContainer.getDriveTrain()));
    getStartButton().onTrue(
        new CalibrateGyro(m_sensorsContainer.getGyro(), m_subsystemsContainer.getDriveTrain()));
    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")
        || Config4905.getConfig4905().getSwerveDrivetrainConfig().hasPath("parkingbrake")) {
      setUpParkingBrake();
    }
    if (Config4905.getConfig4905().getSensorConfig().hasPath("photonvision")) {
      // setUpPhotonVision();
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

  private void setupSBSDTeleOpButtons() {
    getAbutton().onTrue(new SequentialCommandGroup4905(new NotInUnsafeZone(),
        new sbsdCoralLoadArmEndEffectorPositon()));
    getXbutton().whileTrue(new SequentialCommandGroup4905(new NotInUnsafeZone(),
        new teleOpCoralScoring(m_subsystemsContainer.getDriveTrain())));
    getPOVeast().whileTrue(new SequentialCommandGroup4905(new NotInUnsafeZone(),
        new teleOpDriverCoralPickup(m_subsystemsContainer.getDriveTrain())));
    getPOVwest().whileTrue(new SequentialCommandGroup4905(new NotInUnsafeZone(),
        new teleOpWallCoralPickup(m_subsystemsContainer.getDriveTrain())));
    getBbutton().onTrue(new GetInClimberMode());
    getPOVsouth().whileTrue(new SBSDClimb(false));
    getPOVnorth().whileTrue(new SBSDClimb(true));
  }
}
