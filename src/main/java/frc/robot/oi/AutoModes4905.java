package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.autonomousCommands.BumpAutoScoreTwice;
import frc.robot.commands.groupCommands.autonomousCommands.CSAutoLeaveEngage;
import frc.robot.commands.groupCommands.autonomousCommands.CSAutoScoreDirectlyEngage;
import frc.robot.commands.groupCommands.autonomousCommands.CSAutoScoreLeaveEngage;
import frc.robot.commands.groupCommands.autonomousCommands.NoBumpAutoScoreTwice;
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
      m_autoChooser.addOption("2: NO BUMP - Score, Leave Community, Pick, Score",
          new NoBumpAutoScoreTwice());
      m_autoChooser.addOption("3: BUMP -  Score, Leave Community, Pick, Score",
          new BumpAutoScoreTwice());
      m_autoChooser.addOption("4: CS - Leave Community , Engage", new CSAutoLeaveEngage());
      m_autoChooser.addOption("5: CS - Score, Leave Community, Engage",
          new CSAutoScoreLeaveEngage());
      m_autoChooser.addOption("6: CS - Score, Engage", new CSAutoScoreDirectlyEngage());

      // not tested

      // m_autoChooser.addOption("7: Safety (Week 0 Style) - Score, Leave, Engage",
      // new SafetyAutoCS());
      //
      SmartDashboard.putData("autoModes", m_autoChooser);
    }
  }
}