package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.autoCommands.BackupLeft;
import frc.robot.commands.autoCommands.BackupRight;
import frc.robot.commands.autoCommands.HubStartDepot;
import frc.robot.commands.autoCommands.LeftTrench;
import frc.robot.commands.autoCommands.LeftTrenchDoubleLoop;
import frc.robot.commands.autoCommands.LeftTrenchLoop;
import frc.robot.commands.autoCommands.RightTrench;
import frc.robot.commands.autoCommands.RightTrenchDoubleLoop;
import frc.robot.commands.autoCommands.RightTrenchLoop;
import frc.robot.commands.driveTrainCommands.DoNothingAuto;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {

    m_autoChooser = autoChooser;

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isFuelRaider()) {
      try {
        m_autoChooser.addOption("LeftTrenchLoop", new LeftTrenchLoop());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("RightTrenchLoop", new RightTrenchLoop());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Half LeftFuelRaid Trench", new LeftTrench());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Half RightFuelRaid Trench", new RightTrench());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("HubStartDepot", new HubStartDepot());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("LeftTrenchDoubleLoop", new LeftTrenchDoubleLoop());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("RightTrenchDoubleLoop", new RightTrenchDoubleLoop());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("BackupLeft", new BackupLeft());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("BackupRight", new BackupRight());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
