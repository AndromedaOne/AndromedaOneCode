package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.groupCommands.autonomousCommands.AHighHub2;
import frc.robot.commands.groupCommands.autonomousCommands.B4Cargo;
import frc.robot.commands.groupCommands.autonomousCommands.BHighHub2;
import frc.robot.commands.groupCommands.autonomousCommands.CHighHub2;
import frc.robot.commands.groupCommands.autonomousCommands.DoNothingAuto;
import frc.robot.commands.groupCommands.autonomousCommands.TaxiAuto;
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
    if (Config4905.getConfig4905().isTopGun() || Config4905.getConfig4905().isShowBot()) {
      m_autoChooser.addOption("1: Taxi", new TaxiAuto());
      m_autoChooser.addOption("2: AHighHub2", new AHighHub2(180));
      m_autoChooser.addOption("3: BHighHub2", new BHighHub2());
      m_autoChooser.addOption("4: CHighHub2", new CHighHub2());
      m_autoChooser.addOption("5: B4Cargo", new B4Cargo());
    }

    SmartDashboard.putData("autoModes", m_autoChooser);
  }

}