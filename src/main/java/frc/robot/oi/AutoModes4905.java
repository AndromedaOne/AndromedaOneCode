package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.autoCommands.CCWFuelRaid;
import frc.robot.commands.autoCommands.CWFuelRaid;
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
        m_autoChooser.addOption("CCWFuelRaid", new CCWFuelRaid());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("CWFuelRaid", new CWFuelRaid());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
