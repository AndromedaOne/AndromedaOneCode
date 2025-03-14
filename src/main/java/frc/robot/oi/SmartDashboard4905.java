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
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetWheelsToAngle;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.examplePathCommands.FinishPathTest;
import frc.robot.commands.examplePathCommands.OnTheFlyPathTest;
import frc.robot.commands.examplePathCommands.OttoOneTest;
import frc.robot.commands.examplePathCommands.Spinner;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPath;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPathReturn;
import frc.robot.commands.examplePathCommands.ThisIsJustASimplePathToReefStationD;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsSimple;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
import frc.robot.commands.photonVisionCommands.SetPoseUsingSmartDashboard;
import frc.robot.commands.sbsdAlgaeManipulatorCommands.AlgaeManipulatorIntake;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.commands.sbsdArmCommands.Rotate;
import frc.robot.commands.sbsdArmCommands.RotateEndEffector;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdArmCommands.SetBreakMode;
import frc.robot.commands.sbsdAutoCommands.auto1;
import frc.robot.commands.sbsdAutoCommands.auto2;
import frc.robot.commands.sbsdAutoCommands.auto6;
import frc.robot.commands.sbsdAutoCommands.auto8;
import frc.robot.commands.sbsdClimberCommands.SBSDClimb;
import frc.robot.commands.sbsdTeleOpCommands.GetInClimberMode;
import frc.robot.commands.sbsdTeleOpCommands.sbsdMoveArmAndEndEffector;
import frc.robot.commands.showBotAudio.PlayAudio;
import frc.robot.commands.showBotAudio.StopAudio;
import frc.robot.commands.showBotCannon.PressurizeCannon;
import frc.robot.commands.showBotCannon.ShootCannon;
import frc.robot.commands.teleOpPathCommands.FinishC;
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
    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isSBSD()) {
      AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);
    }
    SmartDashboard.putNumber("Auto Delay", 0);
    SmartDashboard.putData("Reload Config", new ConfigReload());
    SmartDashboard.putData("Calibrate Gyro",
        new CalibrateGyro(sensorsContainer.getGyro(), subsystemsContainer.getDriveTrain()));
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
      SmartDashboard.putNumber("Set swerve drive angle for test", 0);
      SmartDashboard.putData("Run swerve drive angle set for test",
          new SwerveDriveSetWheelsToAngle(subsystemsContainer.getDriveTrain(), 0, true));
    }

    if (Config4905.getConfig4905().isRomi()) {
      romiCommands(subsystemsContainer);
    }

    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")) {
      SmartDashboard.putData("Toggle Brakes",
          new ToggleBrakes(subsystemsContainer.getDriveTrain()));
    }
    if (Config4905.getConfig4905().doesDrivetrainExist()) {
      SmartDashboard.putNumber("MoveUsingEncoderTester Distance To Move", 24);
      SmartDashboard.putNumber("MoveUsingEncoderTester Angle To Move", 0);
      SmartDashboard.putData("MoveUsingEncoderTester",
          new MoveUsingEncoderTester(subsystemsContainer.getDriveTrain()));
    }
    if (Config4905.getConfig4905().isSwerveBot()) {
      SmartDashboard.putData("SwervePathPlanningPath", new SwervePathPlanningPath());
      SmartDashboard.putData("ThisIsJustASimplePathToReefStationD",
          new ThisIsJustASimplePathToReefStationD());
      SmartDashboard.putData("SwervePathPlanningPathReturn", new SwervePathPlanningPathReturn());
      SmartDashboard.putData("OttoOneTest", new OttoOneTest());
      SmartDashboard.putData("SpinTest", new Spinner());
      SmartDashboard.putData("On the fly path test", new OnTheFlyPathTest().andThen(new FinishC()));
    }

    if (Config4905.getConfig4905().doesShowBotAudioExist()) {
      SmartDashboard.putData("play audio",
          new PlayAudio(subsystemsContainer.getShowBotAudio(), AudioFiles.CrazyTrain));
      SmartDashboard.putData("stop audio", new StopAudio(subsystemsContainer.getShowBotAudio()));
    }

    if ((Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isSBSD())
        && Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
      SmartDashboard.putData("Auto #1 - West Side Scory", new auto1());
      SmartDashboard.putData("Auto #2 - East Side Scory", new auto2());
      SmartDashboard.putData("Auto #6 - 1 North Score And Seven Years Ago", new auto6());
      SmartDashboard.putData("Auto #7 - Drive Backwards", new auto8());
      SmartDashboard.putNumber("Camera index to use", 0);
      SmartDashboard.putNumber("April tag to use", 0);
      SmartDashboard.putBoolean("Use left for camera", false);
      SmartDashboard.putData("Finish Path Test Using Move Left",
          new FinishPathTest(subsystemsContainer.getDriveTrain(), true, true));
      SmartDashboard.putData("Finish Path Test Using Move Right",
          new FinishPathTest(subsystemsContainer.getDriveTrain(), true, false));
      SmartDashboard.putData("Finish Path Test Without Move",
          new FinishPathTest(subsystemsContainer.getDriveTrain(), false, false));
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
      SmartDashboard.putData("Run Climber Winch", new SBSDClimb(true, false));
      SmartDashboard.putData("Run Reverse Climb", new SBSDClimb(true, true));
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

    if (Config4905.getConfig4905().doesSBSDAlgaeManipulatorExist()) {
      SmartDashboard.putData("SBSD Move Algae Manipulator", new AlgaeManipulatorIntake());
    }
    if (Config4905.getConfig4905().doesSBSDClimberExist()
        && Config4905.getConfig4905().doesSBSDArmExist()
        && Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      SmartDashboard.putData("Climber mode", new GetInClimberMode());
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
