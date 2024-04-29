/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenArmRotateCommands.DisableMotorBrake;
import frc.robot.commands.billthovenArmRotateCommands.DisablePneumaticArmBrake;
import frc.robot.commands.billthovenArmRotateCommands.EnableMotorBrake;
import frc.robot.commands.billthovenArmRotateCommands.EnablePneumaticArmBrake;
import frc.robot.commands.billthovenClimberCommands.ResetBillClimberOffset;
import frc.robot.commands.billthovenClimberCommands.RunBillClimber;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.commands.billthovenShooterCommands.TuneBillShooterFeedForward;
import frc.robot.commands.driveTrainCommands.DriveBackwardTimed;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.driveTrainCommands.TurnToTargetUsingGyro;
import frc.robot.commands.examplePathCommands.DriveTrainDiagonalPath;
import frc.robot.commands.examplePathCommands.DriveTrainRectangularPath;
import frc.robot.commands.examplePathCommands.SimpleDriveTrainDiagonalPath;
import frc.robot.commands.groupCommands.autonomousCommands.AmpScore;
import frc.robot.commands.groupCommands.autonomousCommands.CentralSpeaker2Scores;
import frc.robot.commands.groupCommands.autonomousCommands.CentralSpeaker3ScoresAmpSide;
import frc.robot.commands.groupCommands.autonomousCommands.CentralSpeaker3ScoresSource;
import frc.robot.commands.groupCommands.autonomousCommands.DriveStation2SpeakerAmpSide;
import frc.robot.commands.groupCommands.autonomousCommands.DriveStation2SpeakerSource;
import frc.robot.commands.groupCommands.autonomousCommands.DriveStation3SpeakerWithAmp;
import frc.robot.commands.groupCommands.autonomousCommands.EmergencyBackup;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsSimple;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
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
      SensorsContainer sensorsContainer) {
    if (Config4905.getConfig4905().isBillthoven()) {
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
    if (Config4905.getConfig4905().doesBillShooterExist()) {
      SmartDashboard.putData("Tune Bill Shooter Feed Forward", new TuneBillShooterFeedForward(
          subsystemsContainer.getBillShooter(), subsystemsContainer.getBillFeeder()));
      SmartDashboard.putData("Run Bill Shooter RPM",
          new RunBillShooterRPM(subsystemsContainer.getBillShooter()));
    }
    if (Config4905.getConfig4905().doesArmRotateExist()) {
      SmartDashboard.putData("Set Bill Arm Rotate For Tuning", new ArmRotate(
          subsystemsContainer.getBillArmRotate(), () -> 300, false, true, false, false));
      // Will need to be changed at some point
      SmartDashboard.putData("Enable Arm Motor Brake Mode",
          new EnableMotorBrake(subsystemsContainer.getBillArmRotate()));
      SmartDashboard.putData("Disable Arm Motor Brake Mode",
          new DisableMotorBrake(subsystemsContainer.getBillArmRotate()));
      SmartDashboard.putData("Enable Arm Brake",
          new EnablePneumaticArmBrake(subsystemsContainer.getBillArmRotate()));
      SmartDashboard.putData("Disable Arm Brake",
          new DisablePneumaticArmBrake(subsystemsContainer.getBillArmRotate()));
    }
    if (Config4905.getConfig4905().doesEndEffectorExist()) {
      SmartDashboard.putData("EndEffector Low Position",
          new MoveEndEffector(subsystemsContainer.getBillEffectorPosition(), () -> false));
      SmartDashboard.putData("EndEffector High Position",
          new MoveEndEffector(subsystemsContainer.getBillEffectorPosition(), () -> true));

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
      if (Config4905.getConfig4905().isBillthoven()) {
        SmartDashboard.putData("AmpScore", new AmpScore());
        SmartDashboard.putData("CentralSpeaker2Scores", new CentralSpeaker2Scores());
        SmartDashboard.putData("CentralSpeaker3ScoresSource", new CentralSpeaker3ScoresSource());
        SmartDashboard.putData("DriveStation2SpeakerSource", new DriveStation2SpeakerSource());
        SmartDashboard.putData("DriveStation3SpeakerWithAmp", new DriveStation3SpeakerWithAmp());
        SmartDashboard.putData("CentralSpeaker3ScoresAmpSide", new CentralSpeaker3ScoresAmpSide());
        SmartDashboard.putData("DriveStation2SpeakerAmpSide", new DriveStation2SpeakerAmpSide());
        SmartDashboard.putData("EmergencyBackup", new EmergencyBackup());
      }
    }

    if (Config4905.getConfig4905().doesShowBotAudioExist()) {
      SmartDashboard.putData("play audio",
          new PlayAudio(subsystemsContainer.getShowBotAudio(), AudioFiles.CrazyTrain));
      SmartDashboard.putData("stop audio", new StopAudio(subsystemsContainer.getShowBotAudio()));
    }
    if (Config4905.getConfig4905().doesBillClimberExist()) {
      SmartDashboard.putData(new RunBillClimber(subsystemsContainer.getBillClimber(), true));
      SmartDashboard.putData(new ResetBillClimberOffset(subsystemsContainer.getBillClimber()));
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
