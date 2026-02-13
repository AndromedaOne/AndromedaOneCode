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
import frc.robot.commands.climberCommands.ClimberExtensionCommand;
import frc.robot.commands.climberCommands.ClimberRotationCommand;
import frc.robot.commands.driveTrainCommands.MoveUsingDistanceSensorTester;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoderTester;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetWheelsToAngle;
import frc.robot.commands.driveTrainCommands.ToggleBrakes;
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
      SmartDashboard.putNumber("MoveUsingEncoderTester Distance To Move", 24);
      SmartDashboard.putNumber("MoveUsingEncoderTester Angle To Move", 0);
      SmartDashboard.putData("MoveUsingEncoderTester",
          new MoveUsingEncoderTester(subsystemsContainer.getDriveTrain()));

      SmartDashboard.putData("SwervePathPlanningPath", new SwervePathPlanningPath());
      SmartDashboard.putData("SwervePathPlanningPathReturn", new SwervePathPlanningPathReturn());
      SmartDashboard.putData("MoveUsingDistanceSensor",
          new MoveUsingDistanceSensorTester(subsystemsContainer.getDriveTrain(),
              sensorsContainer.getTof0().getDistanceInchesAsSupplier(),
              sensorsContainer.getTof0().getFacingAngle()));
      SmartDashboard.putNumber("MoveUsingDistanceSensorTester Distance To Move", 24);
      // SmartDashboard.putNumber("MoveUsingDistanceSensorTester angle", 0);
      SmartDashboard.putData("SpinTest", new Spinner());
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
      SmartDashboard.putData("Short climber extend",
          new ClimberExtensionCommand("ShortClimberExtend"));
      SmartDashboard.putData("Long climber extend",
          new ClimberExtensionCommand("LongClimberExtend"));
      SmartDashboard.putData("Climber retract", new ClimberExtensionCommand("ClimberRetract"));
      SmartDashboard.putData("Climb up", new ClimberRotationCommand("ClimbUp"));
      SmartDashboard.putData("Climb down", new ClimberRotationCommand("ClimbDown"));
    }

  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

}
