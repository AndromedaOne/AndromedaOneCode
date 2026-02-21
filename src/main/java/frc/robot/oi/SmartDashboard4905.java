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
import frc.robot.commands.CalibrateGyro;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.FuelRaiderCommands.SetAHIPID;
import frc.robot.commands.FuelRaiderCommands.SmartDashboardExtend;
import frc.robot.commands.FuelRaiderCommands.SmartDashboardRetract;
import frc.robot.commands.autoCommands.LeftBump;
import frc.robot.commands.autoCommands.LeftHub;
import frc.robot.commands.autoCommands.LeftHubPath;
import frc.robot.commands.autoCommands.LeftHubSetPoseManually;
import frc.robot.commands.autoCommands.RightBump;
import frc.robot.commands.autoCommands.RightHub;
import frc.robot.commands.autoCommands.RightHubSetPoseManually;
import frc.robot.commands.climberCommands.ClimberExtensionCommand;
import frc.robot.commands.climberCommands.ClimberRotationCommand;
import frc.robot.commands.driveTrainCommands.MoveUsingDistanceSensorDifferenceTester;
import frc.robot.commands.driveTrainCommands.MoveUsingDistanceSensorTester;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetWheelsToAngle;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.driveTrainCommands.TowerAlignment;
import frc.robot.commands.driveTrainCommands.VibrateRobot;
import frc.robot.commands.ejectBeltCommands.EjectBeltLeft;
import frc.robot.commands.ejectBeltCommands.EjectBeltRight;
import frc.robot.commands.examplePathCommands.Spinner;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPath;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPathReturn;
import frc.robot.commands.groupCommands.MoveAndAlignTower;
import frc.robot.commands.intakeCommands.IntakeRollerEjectCommand;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
import frc.robot.commands.photonVisionCommands.SetPoseUsingSmartDashboard;
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
      SensorsContainer sensorsContainer) throws FileVersionException, IOException, ParseException {
    if (Config4905.getConfig4905().isSwerveBot()) {
      AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);
    }
    SmartDashboard.putNumber("Auto Delay", 0);
    SmartDashboard.putData("Reload Config", new ConfigReload());
    SmartDashboard.putData("Calibrate Gyro",
        new CalibrateGyro(sensorsContainer.getGyro(), subsystemsContainer.getDriveTrain()));
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

    if (Config4905.getConfig4905().getDrivetrainConfig().hasPath("parkingbrake")) {
      SmartDashboard.putData("Toggle Brakes",
          new ToggleBrakes(subsystemsContainer.getDriveTrain()));
    }
    if (Config4905.getConfig4905().doesDrivetrainExist()) {
      String name = "Drive train commands/";
      SmartDashboard.putNumber(name +"MoveUsingEncoderTester Distance To Move", 24);
      SmartDashboard.putNumber(name +"MoveUsingEncoderTester Angle To Move", 0);
      SmartDashboard.putData(name +"MoveUsingEncoderTester",
          new MoveUsingEncoderTester(subsystemsContainer.getDriveTrain()));

      SmartDashboard.putData(name +"SwervePathPlanningPath", new SwervePathPlanningPath());
      SmartDashboard.putData(name +"SwervePathPlanningPathReturn", new SwervePathPlanningPathReturn());
      SmartDashboard.putData(name +"MoveUsingDistanceSensor",
          new MoveUsingDistanceSensorTester(subsystemsContainer.getDriveTrain(),
              sensorsContainer.getTof1().getFacingAngle(), sensorsContainer.getTof1()));
      SmartDashboard.putNumber(name +"MoveUsingDistanceSensorTester Distance To Move", 24);
      SmartDashboard.putNumber(name +"SensorDifferenceTesterAngle", 0);
      SmartDashboard.putData(name +"MoveUsingDistanceSensorDifferenceTester",
          new MoveUsingDistanceSensorDifferenceTester(subsystemsContainer.getDriveTrain()));
      SmartDashboard.putData(name +"Tower Alignment",
          new TowerAlignment(subsystemsContainer.getDriveTrain(), 0.3,
              sensorsContainer.getTof1().getFacingAngle(), sensorsContainer.getTof1()));
      SmartDashboard.putData(name +"Move and align tower",
          new MoveAndAlignTower(subsystemsContainer.getDriveTrain(), 0.3,
              sensorsContainer.getTof1().getFacingAngle(), sensorsContainer.getTof1()));
      SmartDashboard.putData(name +"SpinTest", new Spinner());
      SmartDashboard.putData(name +"Vibrate Robot", new VibrateRobot(subsystemsContainer.getDriveTrain()));
    }
    if (Config4905.getConfig4905().doesEjectBeltExist()) {
      SmartDashboard.putData("Run eject belt left", new EjectBeltLeft());
      SmartDashboard.putData("Run eject belt right", new EjectBeltRight());
    }
    if (Config4905.getConfig4905().doesIntakeRollersExist()) {
      SmartDashboard.putData("Intake roller intake", new IntakeRollerIntakeCommand());
      SmartDashboard.putData("Intake roller eject", new IntakeRollerEjectCommand());
    }

    if (Config4905.getConfig4905().doesClimberExist()) {
      SmartDashboard.putData("climberCommand/Short climber extend",
          new ClimberExtensionCommand("ShortClimberExtend"));
      SmartDashboard.putData("climberCommand/Long climber extend",
          new ClimberExtensionCommand("LongClimberExtend"));
      SmartDashboard.putData("climberCommand/Climber retract",
          new ClimberExtensionCommand("ClimberRetract"));
      SmartDashboard.putData("climberCommand/Climb up", new ClimberRotationCommand("ClimbUp"));
      SmartDashboard.putData("Climb up auto position",
          new ClimberRotationCommand("ClimbUpAutoPosition"));
      SmartDashboard.putData("climberCommand/Climb down", new ClimberRotationCommand("ClimbDown"));
    }
    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isFuelRaider()) {
      SmartDashboard.putData("Right Bump", new RightBump());
      SmartDashboard.putData("Left Bump", new LeftBump());
      SmartDashboard.putData("Right Hub", new RightHub());
      SmartDashboard.putData("Left Hub", new LeftHub());
      SmartDashboard.putData("Set Left Pose", new LeftHubSetPoseManually());
      SmartDashboard.putData("Set Right Pose", new RightHubSetPoseManually());
      SmartDashboard.putData("left hub path", new LeftHubPath());
    }
    if (Config4905.getConfig4905().doesAHIExist()) {
      String name = "ahicommands/";
      SmartDashboard.putData(name + "Retract AHI", new SmartDashboardRetract());
      SmartDashboard.putData(name + "Extend AHI", new SmartDashboardExtend());
      SmartDashboard.putNumber(name + "AHI P value", 0);
      SmartDashboard.putNumber(name + "AHI I value", 0);
      SmartDashboard.putNumber(name + "AHI D value", 0);
      SmartDashboard.putData(name + "Set AHI PID values", new SetAHIPID());
    }

  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

}
