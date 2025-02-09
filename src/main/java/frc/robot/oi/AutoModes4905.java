package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.topGunAutonomousCommands.DoNothingAuto;
import frc.robot.commands.sbsdAutoCommands.auto1;
import frc.robot.commands.sbsdAutoCommands.auto2;
import frc.robot.commands.sbsdAutoCommands.auto6;
import frc.robot.commands.sbsdAutoCommands.auto7;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {

    m_autoChooser = autoChooser;

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isSwerveBot()) {
      try {
        m_autoChooser.addOption("Auto #1 - West Side Scory", new auto1());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Auto #2 - East Side Scory", new auto2());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Auto #6 - 1 North Score And Seven Years Ago", new auto6());
      } catch (Exception e) {
        e.printStackTrace();
      }
      m_autoChooser.addOption("Auto #7 - Drive Backwards", new auto7());
    }

    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
