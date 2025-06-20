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
import frc.robot.commands.photonVisionCommands.SetPoseUsingSmartDashboard;
import frc.robot.commands.sbsdAlgaeManipulatorCommands.AlgaeManipulatorIntake;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.commands.sbsdArmCommands.SetBreakMode;
import frc.robot.commands.sbsdAutoCommands.AprilTagSnapshot;
import frc.robot.commands.sbsdClimberCommands.SBSDClimb;
import frc.robot.commands.sbsdTeleOpCommands.GetInClimberMode;
import frc.robot.commands.teleOpPathCommands.FinishC;
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
    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isSBSD()) {
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
      SmartDashboard.putData("AprilTagSnapshot", new AprilTagSnapshot());
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

    if ((Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isSBSD())
        && Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
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
      SmartDashboard.putData("SBSD Arm Set Goal", new ArmControlCommand(true, false));
    }

    if (Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      SmartDashboard.putData("SBSD End Effector Control Command",
          new EndEffectorControlCommand(true, false));
    }

    if (Config4905.getConfig4905().doesSBSDArmExist()
        && Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
    }

    if (Config4905.getConfig4905().doesSBSDAlgaeManipulatorExist()) {
      SmartDashboard.putData("SBSD Move Algae Manipulator", new AlgaeManipulatorIntake());
    }
    if (Config4905.getConfig4905().doesSBSDClimberExist()
        && Config4905.getConfig4905().doesSBSDArmExist()
        && Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      SmartDashboard.putData("Climber mode", new GetInClimberMode());
      SmartDashboard.putData("Run Climber Winch", new SBSDClimb(true, false));
      SmartDashboard.putData("Run Reverse Climb", new SBSDClimb(true, true));
    }
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

}
