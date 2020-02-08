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
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * Add your docs here.
 */
public class SmartDashboard4905 {
  SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  public SmartDashboard4905(SubsystemsContainer subsystemsContainer) {

    SmartDashboard.putData("DriveBackward", new DriveBackwardTimed(3, subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("MoveUsingEncoder", new MoveUsingEncoder(subsystemsContainer.getDrivetrain(), 48));
    initializeAutoChooser(subsystemsContainer);
  }

  private void initializeAutoChooser(SubsystemsContainer subsystemsContainer) {
    m_autoChooser.setDefaultOption("DoNothing", new DoNothingAuto());
    m_autoChooser.addOption("DriveBackward", new DriveBackwardTimed(3, subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("autoModes", m_autoChooser);
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }
}
