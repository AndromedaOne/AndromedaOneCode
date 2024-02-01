package frc.robot.oi;

import java.util.Optional;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.autonomousCommands.AmpScore;
import frc.robot.commands.groupCommands.autonomousCommands.CentralSpeaker2Scores;
import frc.robot.commands.groupCommands.autonomousCommands.CentralSpeaker3Scores;
import frc.robot.commands.groupCommands.autonomousCommands.DriveStation2Speaker;
import frc.robot.commands.groupCommands.autonomousCommands.DriveStation3SpeakerWithAmp;
import frc.robot.commands.groupCommands.autonomousCommands.EmergencyBackup;
import frc.robot.commands.groupCommands.topGunAutonomousCommands.DoNothingAuto;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {
    Optional<Alliance> currentAlliance = DriverStation.getAlliance();
    Config autonomousConfig;
    if (currentAlliance.isPresent()) {
      if (currentAlliance.get() == Alliance.Red) {
        autonomousConfig = Config4905.getConfig4905().getRedAutonomousConfig();
      } else {
        autonomousConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
      }
    } else {
      autonomousConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    }
    m_autoChooser = autoChooser;

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isSwerveBot()) {
      m_autoChooser.addOption("1: Emergency Backup", new EmergencyBackup(autonomousConfig));
      m_autoChooser.addOption("2: Score Amp, Pick Up Note, Score Amp",
          new AmpScore(autonomousConfig));
      m_autoChooser.addOption(
          "3: Central Speaker Start, Score Speaker, Pick Up Note, Score Speaker",
          new CentralSpeaker2Scores(autonomousConfig));
      m_autoChooser.addOption(
          "4: Cental Speaker Start, Score Speaker, Pick Up Note, Score Speaker, Pick Up Note, Score Speaker",
          new CentralSpeaker3Scores(autonomousConfig));
      m_autoChooser.addOption("5: Drive Station Start, Score Speaker, Pick up Note",
          new DriveStation2Speaker(autonomousConfig));
      m_autoChooser.addOption("6: Drive Station Start, Score Speaker, Pick up Note, Score Speaker",
          new DriveStation3SpeakerWithAmp(autonomousConfig));
    }

    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
