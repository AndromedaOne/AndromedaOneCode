package frc.robot.oi;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.DoNothingAuto;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer,
      SendableChooser<Command> autoChooser) {
    m_autoChooser = autoChooser;
    Config driveTrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    LimeLightCameraBase limelight = sensorsContainer.getLimeLight();

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    SmartDashboard.putData("autoModes", m_autoChooser);
  }

}