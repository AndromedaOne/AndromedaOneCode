package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.autoCommands.LeftBump;
import frc.robot.commands.autoCommands.LeftHub;
import frc.robot.commands.autoCommands.RightBump;
import frc.robot.commands.autoCommands.RightHub;
import frc.robot.commands.driveTrainCommands.DoNothingAuto;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {

    m_autoChooser = autoChooser;

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isSwerveBot() || Config4905.getConfig4905().isFuelRadar()) {
      try {
        m_autoChooser.addOption("Right Bump", new RightBump());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Left Bump", new LeftBump());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Right Hub", new RightHub());
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        m_autoChooser.addOption("Left Hub", new LeftHub());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    SmartDashboard.putData("autoModes", m_autoChooser);
  }
}
