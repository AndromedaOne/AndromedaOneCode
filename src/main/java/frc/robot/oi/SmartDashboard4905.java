/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.driveTrainCommands.DriveBackwardTimed;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.driveTrainCommands.TurnToTargetUsingGyro;
import frc.robot.commands.examplePathCommands.DriveTrainDiagonalPath;
import frc.robot.commands.examplePathCommands.DriveTrainRectangularPath;
import frc.robot.commands.examplePathCommands.OttoOneTest;
import frc.robot.commands.examplePathCommands.SimpleDriveTrainDiagonalPath;
import frc.robot.commands.examplePathCommands.Spinner;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPath;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPathReturn;
import frc.robot.commands.examplePathCommands.ThisIsJustASimplePathToReefStationD;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsSimple;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
import frc.robot.commands.photonVisionCommands.SetPoseUsingSmartDashboard;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.commands.sbsdArmCommands.Rotate;
import frc.robot.commands.sbsdArmCommands.RotateEndEffector;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdArmCommands.SetBreakMode;
import frc.robot.commands.sbsdAutoCommands.auto1;
import frc.robot.commands.sbsdAutoCommands.auto2;
import frc.robot.commands.sbsdAutoCommands.auto6;
import frc.robot.commands.sbsdAutoCommands.auto7;
import frc.robot.commands.sbsdTeleOpCommands.sbsdMoveArmAndEndEffector;
import frc.robot.commands.showBotAudio.PlayAudio;
import frc.robot.commands.showBotAudio.StopAudio;
import frc.robot.commands.showBotCannon.PressurizeCannon;
import frc.robot.commands.showBotCannon.ShootCannon;
import frc.robot.commands.topGunShooterCommands.MoveShooterAlignment;
import frc.robot.commands.topGunShooterCommands.RunShooterRPM;
import frc.robot.commands.topGunShooterCommands.TuneShooterFeedForward;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.showBotAudio.AudioFiles;

/**
 * This class is for adding SmartDashboard Buttons, putData, (clickable buttons
 * to run commands). DO NOT include putNumber or putString as these need to be
 * in periodic methods to get called over and over. this class gets instantiated
 * once when the robot is turned on...
 */
public class SmartDashboard4905 {
  SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  public SmartDashboard4905(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer) throws FileVersionException, IOException, ParseException {
    if (Config4905.getConfig4905().isSwerveBot()) {
      AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);
    }
    SmartDashboard.putNumber("Auto Delay", 0);
    SmartDashboard.putData("Reload Config", new ConfigReload());
    SmartDashboard.putData("Calibrate Gyro",
        new CalibrateGyro(sensorsContainer.getGyro(), subsystemsContainer.getDriveTrain()));
    SmartDashboard.putData("Simple Diagonal Path Gen",
        new SimpleDriveTrainDiagonalPath(subsystemsContainer.getDriveTrain()));
    if (Robot.getInstance().getSensorsContainer().getLimeLight().doesLimeLightExist()) {
      SmartDashboard.putData("Enable Limelight LEDs",
          new ToggleLimelightLED(true, sensorsContainer));
      SmartDashboard.putData("Disable Limelight LEDs",
          new ToggleLimelightLED(false, sensorsContainer));
    }
    if (Config4905.getConfig4905().doesShowBotCannonExist()) {
      SmartDashboard.putData("PressurizeCannon", new PressurizeCannon());
      SmartDashboard.putData("Shoot Cannon", new ShootCannon());
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      SmartDashboard.putData("Tune Shooter Feed Forward",
          new TuneShooterFeedForward(subsystemsContainer.getTopShooterWheel(),
              subsystemsContainer.getBottomShooterWheel(), subsystemsContainer.getFeeder()));
      SmartDashboard.putNumber("Set Shooter RPM", 1000);
      SmartDashboard.putData("Run Shooter RPM", new RunShooterRPM(
          subsystemsContainer.getTopShooterWheel(), subsystemsContainer.getBottomShooterWheel()));
      SmartDashboard.putData("Tune Shooter Angle", new MoveShooterAlignment(
          subsystemsContainer.getShooterAlignment(), () -> 57, true, 0.1, 0.1, 0.5));
    }
    if (Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
      SmartDashboard.putNumber("Set Pose X", 0);
      SmartDashboard.putNumber("Set Pose Y", 0);
      SmartDashboard.putNumber("Set Pose Angle", 0);
      SmartDashboard.putData("Set Pose",
          new SetPoseUsingSmartDashboard(subsystemsContainer.getDriveTrain()));
    }

    if (Config4905.getConfig4905().isRomi()) {
      romiCommands(subsystemsContainer);
    }

    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")) {
      SmartDashboard.putData("Toggle Brakes",
          new ToggleBrakes(subsystemsContainer.getDriveTrain()));
    }
    if (Config4905.getConfig4905().doesDrivetrainExist()) {
      SmartDashboard.putData("DriveBackward",
          new DriveBackwardTimed(1, subsystemsContainer.getDriveTrain()));
      SmartDashboard.putNumber("MoveUsingEncoderTester Distance To Move", 24);
      SmartDashboard.putNumber("MoveUsingEncoderTester Angle To Move", 0);
      SmartDashboard.putData("MoveUsingEncoderTester",
          new MoveUsingEncoderTester(subsystemsContainer.getDriveTrain()));
      SmartDashboard.putData("DriveTrainRectangularPathExample",
          new DriveTrainRectangularPath(subsystemsContainer.getDriveTrain()));
      SmartDashboard.putData("DriveTrainDiagonalPathExample",
          new DriveTrainDiagonalPath(subsystemsContainer.getDriveTrain()));
      if (Robot.getInstance().getSensorsContainer().getPhotonVision().doesPhotonVisionExist()) {
        SmartDashboard.putData("Turn to target",
            new TurnToTargetUsingGyro(subsystemsContainer.getDriveTrain(), () -> 4, () -> 0, true,
                sensorsContainer.getPhotonVision()));
      }

    }
    if (Config4905.getConfig4905().isSwerveBot()) {
      SmartDashboard.putData("SwervePathPlanningPath", new SwervePathPlanningPath());
    }

    if (Config4905.getConfig4905().isSwerveBot()) {
      SmartDashboard.putData("ThisIsJustASimplePathToReefStationD",
          new ThisIsJustASimplePathToReefStationD());
    }

    if (Config4905.getConfig4905().isSwerveBot()) {
      SmartDashboard.putData("SwervePathPlanningPathReturn", new SwervePathPlanningPathReturn());
    }

    if (Config4905.getConfig4905().isSwerveBot()) {
      SmartDashboard.putData("OttoOneTest", new OttoOneTest());
    }

    if (Config4905.getConfig4905().isSwerveBot()) {
      SmartDashboard.putData("SpinTest", new Spinner());
    }

    if (Config4905.getConfig4905().doesShowBotAudioExist()) {
      SmartDashboard.putData("play audio",
          new PlayAudio(subsystemsContainer.getShowBotAudio(), AudioFiles.CrazyTrain));
      SmartDashboard.putData("stop audio", new StopAudio(subsystemsContainer.getShowBotAudio()));
    }

    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isSBSD()) {
      SmartDashboard.putData("Auto #1 - West Side Scory", new auto1());
      SmartDashboard.putData("Auto #2 - East Side Scory", new auto2());
      SmartDashboard.putData("Auto #6 - 1 North Score And Seven Years Ago", new auto6());
      SmartDashboard.putData("Auto #7 - Drive Backwards", new auto7());
    }

    if (Config4905.getConfig4905().doesSBSDArmExist()) {
      SmartDashboard.putData("SBSD Arm Brake On", new SetBreakMode(true));
      SmartDashboard.putData("SBSD Arm Brake Off", new SetBreakMode(false));
      SmartDashboard.putData("SBSD Rotate Arm", new Rotate());
      SmartDashboard.putData("SBSD Arm Set Goal", new ArmControlCommand(true, false));
      SmartDashboard.putData("SBSD Arm Level 1",
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_1, false));
      SmartDashboard.putData("SBSD Arm Level 2",
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_2, false));
      SmartDashboard.putData("SBSD Arm Level 3",
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_3, false));
      SmartDashboard.putData("SBSD Arm Level 4",
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_4, false));
      SmartDashboard.putData("SBSD Arm Coral Load",
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, false));
    }

    if (Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      SmartDashboard.putData("SBSD End Effector Rotate", new RotateEndEffector());
      SmartDashboard.putData("SBSD End Effector Control Command",
          new EndEffectorControlCommand(true, false));
      SmartDashboard.putData("SBSD End Effector Level 1",
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_1, false));
      SmartDashboard.putData("SBSD End Effector Level 2",
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_2, false));
      SmartDashboard.putData("SBSD End Effector Level 3",
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_3, false));
      SmartDashboard.putData("SBSD End Effector Level 4",
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_4, false));
      SmartDashboard.putData("SBSD End Effector Coral Load",
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, false));
    }

    if (Config4905.getConfig4905().doesSBSDArmExist()
        && Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      SmartDashboard.putData("SBSD Arm and End Effector Level 1",
          new sbsdMoveArmAndEndEffector(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_1));
      SmartDashboard.putData("SBSD Arm and End Effector Level 2",
          new sbsdMoveArmAndEndEffector(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_2));
      SmartDashboard.putData("SBSD Arm and End Effector Level 3",
          new sbsdMoveArmAndEndEffector(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_3));
      SmartDashboard.putData("SBSD Arm and End Effector Level 4",
          new sbsdMoveArmAndEndEffector(() -> SBSDArmSetpoints.ArmSetpoints.LEVEL_4));
    }
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

  private void romiCommands(SubsystemsContainer subsystemsContainer) {
    SmartDashboard.putData("AllianceAnticsSimple",
        new AllianceAnticsSimple(subsystemsContainer.getDriveTrain()));
  }

}
