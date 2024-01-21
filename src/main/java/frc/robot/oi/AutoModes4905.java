package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.autonomousCommands.BlueAmpScore;
import frc.robot.commands.groupCommands.autonomousCommands.BlueCentralSpeaker2Scores;
import frc.robot.commands.groupCommands.autonomousCommands.BlueCentralSpeaker3Scores;
import frc.robot.commands.groupCommands.autonomousCommands.BlueDriveStation2Speaker;
import frc.robot.commands.groupCommands.autonomousCommands.BlueDriveStation3SpeakerWithAmp;
import frc.robot.commands.groupCommands.autonomousCommands.EmergencyBackup;
import frc.robot.commands.groupCommands.autonomousCommands.RedAmpScore;
import frc.robot.commands.groupCommands.autonomousCommands.RedCentralSpeaker2Scores;
import frc.robot.commands.groupCommands.autonomousCommands.RedCentralSpeaker3Scores;
import frc.robot.commands.groupCommands.autonomousCommands.RedDriveStation2Speaker;
import frc.robot.commands.groupCommands.autonomousCommands.RedDriveStation3SpeakerWithAmp;
import frc.robot.commands.groupCommands.autonomousCommands.TaxiAuto;
import frc.robot.commands.groupCommands.topGunAutonomousCommands.DoNothingAuto;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {
    m_autoChooser = autoChooser;

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().m_isSwerveBot()) {
      m_autoChooser.addOption("1: Taxi", new TaxiAuto());
      m_autoChooser.addOption("2: Red, Score Amp, Pick Up Note, Score Amp", new RedAmpScore());
      m_autoChooser.addOption(
          "3: Red Central Speaker Start, Score Speaker, Pick Up Note, Score Speaker",
          new RedCentralSpeaker2Scores());
      m_autoChooser.addOption(
          "4: Red Cental Speaker Start, Score Speaker, Pick Up Note, Score Speaker, Pick Up Note, Score Speaker",
          new RedCentralSpeaker3Scores());
      m_autoChooser.addOption("5: Red Drive Station Start, Score Speaker, Pick up Note",
          new RedDriveStation2Speaker());
      m_autoChooser.addOption(
          "6: Red Drive Station Start, Score Speaker, Pick up Note, Score Speaker",
          new RedDriveStation3SpeakerWithAmp());
      m_autoChooser.addOption("7: Blue, Score Amp, Pick Up Note, Score Amp", new BlueAmpScore());
      m_autoChooser.addOption(
          "8: Blue Central Speaker Start, Score Speaker, Pick Up Note, Score Speaker",
          new BlueCentralSpeaker2Scores());
      m_autoChooser.addOption(
          "9: Blue Cental Speaker Start, Score Speaker, Pick Up Note, Score Speaker, Pick Up Note, Score Speaker",
          new BlueCentralSpeaker3Scores());
      m_autoChooser.addOption("10: Blue Drive Station Start, Score Speaker, Pick up Note",
          new BlueDriveStation2Speaker());
      m_autoChooser.addOption(
          "11: Blue Drive Station Start, Score Speaker, Pick up Note, Score Speaker",
          new BlueDriveStation3SpeakerWithAmp());
      m_autoChooser.addOption("12: EmergencyBackup", new EmergencyBackup());
    }

    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
