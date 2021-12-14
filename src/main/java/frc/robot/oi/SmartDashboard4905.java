/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.ExamplePathCommands.DriveTrainDiagonalPath;
import frc.robot.commands.ExamplePathCommands.DriveTrainRectangularPath;
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.pidcommands.MoveUsingEncoderTester;
import frc.robot.commands.romiCommands.romiBallMopper.MopBallMopper;
import frc.robot.commands.romiCommands.romiBallMopper.ResetBallMopper;
import frc.robot.commands.showBotCannon.PressurizeCannon;
import frc.robot.commands.showBotCannon.ShootCannon;
import frc.robot.groupcommands.RomiCommands.AllianceAnticsScoring;
import frc.robot.groupcommands.RomiCommands.AllianceAnticsSimple;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * Add your docs here.
 */
public class SmartDashboard4905 {
  SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  public SmartDashboard4905(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer) {
    AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);
    SmartDashboard.putData("DriveBackward", new DriveBackwardTimed(1, subsystemsContainer.getDrivetrain()));
    SmartDashboard.putNumber("MoveUsingEncoderTester Distance To Move", 24);
    SmartDashboard.putData("MoveUsingEncoderTester", new MoveUsingEncoderTester(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("DriveTrainRectangularPathExample",
        new DriveTrainRectangularPath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("DriveTrainDiagonalPathExample",
        new DriveTrainDiagonalPath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putNumber("Auto Delay", 0);
    SmartDashboard.putData("Reload Config", new ConfigReload());

    if (Robot.getInstance().getSensorsContainer().getLimeLight().doesLimeLightExist()) {
      SmartDashboard.putData("Enable Limelight LEDs", new ToggleLimelightLED(true, sensorsContainer));
      SmartDashboard.putData("Disable Limelight LEDs", new ToggleLimelightLED(false, sensorsContainer));
    }
    if (Config4905.getConfig4905().doesCannonExist()) {
      SmartDashboard.putData("PressurizeCannon", new PressurizeCannon());
      SmartDashboard.putData("Shoot Cannon", new ShootCannon());
    }

    if (Config4905.getConfig4905().isRomi()) {
      romiCommands(subsystemsContainer);
    }
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }

  private void romiCommands(SubsystemsContainer subsystemsContainer) {
    if (Config4905.getConfig4905().doesRomiBallMopperExist()) {
      SmartDashboard.putData("Mop Ball", new MopBallMopper());
      SmartDashboard.putData("Reset ball Mopper", new ResetBallMopper());
    }
    SmartDashboard.putData("AllianceAnticsSimple", new AllianceAnticsSimple(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("AllianceAnticsScoring", new AllianceAnticsScoring(subsystemsContainer.getDrivetrain()));
  }
}
