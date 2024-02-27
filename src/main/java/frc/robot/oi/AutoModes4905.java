package frc.robot.oi;

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

    m_autoChooser = autoChooser;

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isBillthoven()) {
      m_autoChooser.addOption("1: Emergency Backup", new EmergencyBackup());
      m_autoChooser.addOption("2: AmpScore: Score Amp, Pick Up Note, Score Amp", new AmpScore());
      m_autoChooser.addOption(
          "3: CentralSpeaker2Scores: Central Speaker Start, Score Speaker, Pick Up Note, Score Speaker",
          new CentralSpeaker2Scores());
      m_autoChooser.addOption(
          "4: CentralSpeaker3Scores: Cental Speaker Start, Score Speaker, Pick Up Note, Score Speaker, Pick Up Note, Score Speaker",
          new CentralSpeaker3Scores());
      m_autoChooser.addOption(
          "5: DriveStation2Speaker: Drive Station 2 Start, Score Speaker, Pick up Note, Score Speaker",
          new DriveStation2Speaker());
      m_autoChooser.addOption(
          "6: DriveStation3SpeakerWithAmp: Drive Station Start, Score Speaker, Pick up Note, Score Amp",
          new DriveStation3SpeakerWithAmp());
    }

    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
