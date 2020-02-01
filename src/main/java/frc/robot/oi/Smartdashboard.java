/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * Add your docs here.
 */
public class Smartdashboard {
  public Smartdashboard(SubsystemsContainer subsystemsContainer) {

    SmartDashboard.putData("DriveBackward", new DriveBackwardTimed(3, subsystemsContainer.getDrivetrain()));
  }
}
