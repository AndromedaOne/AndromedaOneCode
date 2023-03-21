package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.autonomousCommands.EngageAutoDock;
import frc.robot.commands.groupCommands.autonomousCommands.LeftAutoPlaceAndLeave;
import frc.robot.commands.groupCommands.autonomousCommands.PlaceDirectlyEngage;
import frc.robot.commands.groupCommands.autonomousCommands.PlaceEngageAutoDock;
import frc.robot.commands.groupCommands.autonomousCommands.RightAutoPlaceAndLeave;
import frc.robot.commands.groupCommands.autonomousCommands.SafetyAutoCS;
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

    // This line of code will need to be changed to check for the S.A.M. Robot
    if (Config4905.getConfig4905().isSAM()) {
      m_autoChooser.addOption("1: Taxi", new TaxiAuto());
      m_autoChooser.addOption("2: Blue Left Score, Leave, Pick, Score",
          new LeftAutoPlaceAndLeave());
      m_autoChooser.addOption("3: Blue CS Leave, Engage", new EngageAutoDock());
      m_autoChooser.addOption("4: Blue Right Score, Leave, Pick, Score",
          new RightAutoPlaceAndLeave());
      m_autoChooser.addOption("5: Blue CS Score, Leave, Engage", new PlaceEngageAutoDock());

      // new not tested
      // place leave straight to the charging station then balance

      // m_autoChooser.addOption("6: Blue CS Score, Leave, Pick, Engage",
      // new PlacePickEngageAutoDock());
      m_autoChooser.addOption("7: Red Left Score, Leave, Pick, Score",
          new RightAutoPlaceAndLeave());
      m_autoChooser.addOption("8: Red CS Leave, Engage", new EngageAutoDock());
      m_autoChooser.addOption("9: Red Right Score, Leave, Pick, Score",
          new LeftAutoPlaceAndLeave());
      m_autoChooser.addOption("10: Red CS Score, Leave, Engage", new PlaceEngageAutoDock());
      // m_autoChooser.addOption("11: Red CS Score, Leave, Pick, Engage",
      // new PlacePickEngageAutoDock());
      m_autoChooser.addOption("12: Safety (Week 0 Style): Score, Leave, Engage",
          new SafetyAutoCS());
      m_autoChooser.addOption("13. CS Place And Straight Into Engage", new PlaceDirectlyEngage());
    }
    SmartDashboard.putData("autoModes", m_autoChooser);
  }

}