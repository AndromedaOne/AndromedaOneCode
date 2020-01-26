/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

/**
 * The container that controls all of the classes in package OI.
 */
public class OIContainer {
  private DriveController m_driveController;
  private Smartdashboard m_smartDashboard;
  private SubsystemController m_subsystemController;

  public OIContainer() {
    m_driveController = new DriveController();
    m_smartDashboard = new Smartdashboard();
    m_subsystemController = new SubsystemController();
  }

  public DriveController getDriveController(){
    return m_driveController;
  }

  public Smartdashboard getSmartDashboard(){
    return m_smartDashboard;
  }

  public SubsystemController getSubsystemController(){
    return m_subsystemController;
  }
}
