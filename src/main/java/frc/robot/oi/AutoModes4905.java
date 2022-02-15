package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
import frc.robot.commands.groupCommands.autonomousCommands.DoNothingAuto;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsScoring;
import frc.robot.commands.groupCommands.romiCommands.AllianceAnticsSimple;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SensorsContainer sensorsContainer, SendableChooser<Command> autoChooser) {
    m_autoChooser = autoChooser;
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();

    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());

    if (Config4905.getConfig4905().isRomi()) {
      m_autoChooser.addOption("1: Simple Park", new AllianceAnticsSimple(driveTrain));
      m_autoChooser.addOption("2: Scoring And Park", new AllianceAnticsScoring(driveTrain));
    }
    if (Config4905.getConfig4905().isTopGun()) {
      m_autoChooser.addOption("1: Taxi",
          new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, (4 * 12), 0, 6)));
      // ?? Check heading angle, output power
      // m_autoChooser.addOption("2: High Hub 2", new FakeCommand());
      // m_autoChooser.addOption("3: Low Hub 2", new FakeCommand());

    }

    SmartDashboard.putData("autoModes", m_autoChooser);
  }

}