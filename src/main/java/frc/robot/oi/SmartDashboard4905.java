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
import frc.robot.commands.SAMgripperCommands.CloseGripper;
import frc.robot.commands.SAMgripperCommands.OpenCloseGripper;
import frc.robot.commands.SAMgripperCommands.OpenGripper;
import frc.robot.commands.driveTrainCommands.BalanceRobot;
import frc.robot.commands.driveTrainCommands.DriveBackwardTimed;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.MoveWithoutPID;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.examplePathCommands.DriveTrainDiagonalPath;
import frc.robot.commands.examplePathCommands.DriveTrainRectangularPath;
import frc.robot.commands.groupCommands.autonomousCommands.EngageAutoDock;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsSimple;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.OffFloorPickupPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.SubstationPickupPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.TopScorePosition;
import frc.robot.commands.limeLightCommands.ToggleLimelightLED;
import frc.robot.commands.samArmExtendRetractCommands.DisengageExtendRetractBrake;
import frc.robot.commands.samArmExtendRetractCommands.EnableExtendRetractBrake;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetract;
import frc.robot.commands.samArmRotateCommands.EnableArmBrake;
import frc.robot.commands.samArmRotateCommands.RotateArm;
import frc.robot.commands.showBotCannon.PressurizeCannon;
import frc.robot.commands.showBotCannon.ShootCannon;
import frc.robot.commands.topGunShooterCommands.MoveShooterAlignment;
import frc.robot.commands.topGunShooterCommands.RunShooterRPM;
import frc.robot.commands.topGunShooterCommands.TuneShooterFeedForward;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

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
    if (Config4905.getConfig4905().doesCannonExist()) {
      SmartDashboard.putData("PressurizeCannon", new PressurizeCannon());
      SmartDashboard.putData("Shoot Cannon", new ShootCannon());
    }
    if (Config4905.getConfig4905().doesGripperExist()) {
      SmartDashboard.putData("Toggle Gripper",
          new OpenCloseGripper(subsystemsContainer.getGripper()));
      SmartDashboard.putData("Open Gripper", new OpenGripper(subsystemsContainer.getGripper()));
      SmartDashboard.putData("Close Gripper", new CloseGripper(subsystemsContainer.getGripper()));
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

    if (Config4905.getConfig4905().doesSamArmRotateExist()) {
      SmartDashboard.putData("Low Score Position", new OffFloorPickupPosition(
          subsystemsContainer.getArmRotateBase(), subsystemsContainer.getArmExtRetBase()));
      SmartDashboard.putData("Mid Score Position", new MiddleScorePosition(
          subsystemsContainer.getArmRotateBase(), subsystemsContainer.getArmExtRetBase()));
      SmartDashboard.putData("Top Score Position", new TopScorePosition(
          subsystemsContainer.getArmRotateBase(), subsystemsContainer.getArmExtRetBase()));
      SmartDashboard.putData("Substation Pickup Position", new SubstationPickupPosition(
          subsystemsContainer.getArmRotateBase(), subsystemsContainer.getArmExtRetBase()));
      SmartDashboard.putData("Stow Position", new StowPosition(
          subsystemsContainer.getArmRotateBase(), subsystemsContainer.getArmExtRetBase()));
      SmartDashboard.putData("Enable Arm Rotation Brake",
          new EnableArmBrake(subsystemsContainer.getArmRotateBase()));
      SmartDashboard.putData("Extend Arms Tuner",
          new ExtendRetract(subsystemsContainer.getArmExtRetBase(), true));
      SmartDashboard.putData("Arm Angle Tuner",
          new RotateArm(subsystemsContainer.getArmRotateBase(), () -> 0, false, true));
      SmartDashboard.putData("Engage Arm Ext Brake",
          new EnableExtendRetractBrake(subsystemsContainer.getArmExtRetBase()));
      SmartDashboard.putData("Disengage Arm Ext Brake",
          new DisengageExtendRetractBrake(subsystemsContainer.getArmExtRetBase()));
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
      SmartDashboard.putData("BalanceRobotBackwards",
          new SequentialCommandGroup4905(
              new MoveWithoutPID(subsystemsContainer.getDrivetrain(), -70, 0.4, 180),
              new BalanceRobot(subsystemsContainer.getDrivetrain(), 0.6, 180)));
      SmartDashboard.putData("BalanceRobotForward",
          new SequentialCommandGroup4905(
              new MoveWithoutPID(subsystemsContainer.getDrivetrain(), 70, 0.4, 180),
              new BalanceRobot(subsystemsContainer.getDrivetrain(), 0.6, 180)));
      SmartDashboard.putData("Engage Auto Dock", new EngageAutoDock());
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
