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
import frc.robot.commands.ConfigReload;
import frc.robot.commands.driveTrainCommands.DriveBackwardTimed;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.examplePathCommands.DriveTrainDiagonalPath;
import frc.robot.commands.examplePathCommands.DriveTrainRectangularPath;
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
    AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);
    SmartDashboard.putNumber("Auto Delay", 0);
    SmartDashboard.putData("Reload Config", new ConfigReload());
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
    if (Config4905.getConfig4905().isRomi()) {
      romiCommands(subsystemsContainer);
    }

    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")) {
      SmartDashboard.putData("Toggle Brakes",
          new ToggleBrakes(subsystemsContainer.getDrivetrain()));
    }
    if (Config4905.getConfig4905().doesDrivetrainExist()) {
      SmartDashboard.putData("DriveBackward",
          new DriveBackwardTimed(1, subsystemsContainer.getDrivetrain()));
      SmartDashboard.putNumber("MoveUsingEncoderTester Distance To Move", 24);
      SmartDashboard.putData("MoveUsingEncoderTester",
          new MoveUsingEncoderTester(subsystemsContainer.getDrivetrain()));
      SmartDashboard.putData("DriveTrainRectangularPathExample",
          new DriveTrainRectangularPath(subsystemsContainer.getDrivetrain()));
      SmartDashboard.putData("DriveTrainDiagonalPathExample",
          new DriveTrainDiagonalPath(subsystemsContainer.getDrivetrain()));
    }

    if (Config4905.getConfig4905().doesShowBotAudioExist()) {
      SmartDashboard.putData("play audio",
          new PlayAudio(subsystemsContainer.getShowBotAudio(), AudioFiles.CrazyTrain));
      SmartDashboard.putData("stop audio", new StopAudio(subsystemsContainer.getShowBotAudio()));
    }
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

  private void romiCommands(SubsystemsContainer subsystemsContainer) {
    SmartDashboard.putData("AllianceAnticsSimple",
        new AllianceAnticsSimple(subsystemsContainer.getDrivetrain()));
  }
}
