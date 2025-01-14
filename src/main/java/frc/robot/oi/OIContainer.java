/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.util.FileVersionException;
import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * The container that controls all of the classes in package OI.
 */
public class OIContainer {
  private DriveController m_driveController;
  private SmartDashboard4905 m_smartDashboard;
  private SubsystemController m_subsystemController;

  public OIContainer(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer)
      throws FileVersionException, IOException, ParseException {
    Config controllersConfig = Config4905.getConfig4905().getControllersConfig();
    if (controllersConfig.hasPath("driveController")) {
      System.out.println("Creating drive controller");
      m_driveController = new DriveController(subsystemsContainer, sensorsContainer);
    } else {
      System.out.println("No drive controller");
    }
    if (controllersConfig.hasPath("subsystemController")) {
      System.out.println("Creating subsystem controller");
      m_subsystemController = new SubsystemController(subsystemsContainer);
    } else {
      System.out.println("No subsystem controller");
    }
    m_smartDashboard = new SmartDashboard4905(subsystemsContainer, sensorsContainer);
  }

  public DriveController getDriveController() {
    return m_driveController;
  }

  public SmartDashboard4905 getSmartDashboard() {
    return m_smartDashboard;
  }

  public SubsystemController getSubsystemController() {
    return m_subsystemController;
  }
}