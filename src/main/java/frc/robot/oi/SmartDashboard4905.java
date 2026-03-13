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
import frc.robot.commands.FuelRaiderCommands.RunShooterRPM;
import frc.robot.commands.FuelRaiderCommands.SetAHIPID;
import frc.robot.commands.FuelRaiderCommands.SmartDashboardExtend;
import frc.robot.commands.FuelRaiderCommands.SmartDashboardRetract;
import frc.robot.commands.FuelRaiderCommands.TuneShooterFeedForward;
import frc.robot.commands.autoCommands.CCWFuelRaid;
import frc.robot.commands.autoCommands.CWFuelRaid;
import frc.robot.commands.autoCommands.CenterToLeftSideDrop;
import frc.robot.commands.autoCommands.CenterToRightSideDrop;
import frc.robot.commands.autoCommands.GoToCenterLeft;
import frc.robot.commands.autoCommands.GoToCenterRight;
import frc.robot.commands.autoCommands.LeftToCenterAfterDrop;
import frc.robot.commands.autoCommands.LeftToRightPickup;
import frc.robot.commands.autoCommands.LeftTrench;
import frc.robot.commands.autoCommands.RightToCenterAfterDrop;
import frc.robot.commands.autoCommands.RightToLeftPickup;
import frc.robot.commands.autoCommands.RightTrench;
import frc.robot.commands.autoCommands.TrenchLeftToCenter;
import frc.robot.commands.driveTrainCommands.MoveUsingDistanceSensorTester;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.SetMidSpeed;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetWheelsToAngle;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
import frc.robot.commands.driveTrainCommands.TurnToFieldElement;
import frc.robot.commands.ejectBeltCommands.EjectBeltLeft;
import frc.robot.commands.ejectBeltCommands.EjectBeltRight;
import frc.robot.commands.examplePathCommands.Spinner;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPath;
import frc.robot.commands.examplePathCommands.SwervePathPlanningPathReturn;
import frc.robot.commands.intakeCommands.IntakeRollerEjectCommand;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
import frc.robot.commands.photonVisionCommands.SetPoseUsingSmartDashboard;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.shooter.ShooterBase;

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
    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isFuelRaider()) {
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
      SmartDashboard.putNumber("MoveUsingEncoderTester Distance To Move", 24);
      SmartDashboard.putNumber("MoveUsingEncoderTester Angle To Move", 0);
      SmartDashboard.putData("MoveUsingEncoderTester",
          new MoveUsingEncoderTester(subsystemsContainer.getDriveTrain()));

      SmartDashboard.putData("SwervePathPlanningPath", new SwervePathPlanningPath());
      SmartDashboard.putData("SwervePathPlanningPathReturn", new SwervePathPlanningPathReturn());
      SmartDashboard.putData("MoveUsingDistanceSensor",
          new MoveUsingDistanceSensorTester(subsystemsContainer.getDriveTrain(),
              sensorsContainer.getTof1().getFacingAngle(), sensorsContainer.getTof1()));
      SmartDashboard.putNumber("MoveUsingDistanceSensorTester Distance To Move", 24);
      SmartDashboard.putNumber("SensorDifferenceTesterAngle", 0);
      SmartDashboard.putData("SpinTest", new Spinner());
      SmartDashboard.putData("Turn to hub",
          new TurnToFieldElement(Robot.getInstance().getFieldConstants().getHubPose()));
      SmartDashboard.putNumber("MidMode/Mid mode value to set", 0.7);
      SmartDashboard.putData("MidMode/set mid mode value", new SetMidSpeed());
    }
    if (Config4905.getConfig4905().doesEjectBeltExist()) {
      SmartDashboard.putData("Run eject belt left", new EjectBeltLeft());
      SmartDashboard.putData("Run eject belt right", new EjectBeltRight());
    }
    if (Config4905.getConfig4905().doesIntakeRollersExist()) {
      SmartDashboard.putData("Intake roller intake", new IntakeRollerIntakeCommand());
      SmartDashboard.putData("Intake roller eject", new IntakeRollerEjectCommand());
    }

    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isFuelRaider()) {
      String pathName = "pathcommands/";
      String autoName = "autocommands/";
      String ccw = "ccw/";
      String cw = "cw/";
      SmartDashboard.putData(autoName + "CCW fuel raid", new CCWFuelRaid());
      SmartDashboard.putData(autoName + "CW fuel raid", new CWFuelRaid());
      SmartDashboard.putData(autoName + "Left Trench", new LeftTrench());
      SmartDashboard.putData(autoName + "Right Trench", new RightTrench());

      SmartDashboard.putData(pathName + ccw + "Go To Center Right", new GoToCenterRight());
      SmartDashboard.putData(pathName + ccw + "Right to left pickup", new RightToLeftPickup());
      SmartDashboard.putData(pathName + ccw + "Center to left side drop",
          new CenterToLeftSideDrop());
      SmartDashboard.putData(pathName + ccw + "Left to center after drop",
          new LeftToCenterAfterDrop());

      SmartDashboard.putData(pathName + cw + "Go To Center Left", new GoToCenterLeft());
      SmartDashboard.putData(pathName + cw + "Left to right pickup", new LeftToRightPickup());
      SmartDashboard.putData(pathName + cw + "Center to right side drop",
          new CenterToRightSideDrop());
      SmartDashboard.putData(pathName + cw + "Right to center after drop",
          new RightToCenterAfterDrop());
      SmartDashboard.putData(pathName + "TrenchLeftToCenter", new TrenchLeftToCenter());
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
    if (Config4905.getConfig4905().doesShooterExist()) {
      String name = ShooterBase.getSmartDashboardShooterString() + "commands/";
      SmartDashboard.putData(name + "Run shooter RPM",
          new RunShooterRPM(Robot.getInstance().getSubsystemsContainer().getShooter()));
      SmartDashboard.putData(name + "Tune shooter feed forward",
          new TuneShooterFeedForward(Robot.getInstance().getSubsystemsContainer().getShooter()));
    }

  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

}
