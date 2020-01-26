/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import frc.robot.Config4905;

public class RealDriveTrain extends DriveTrain {
  /**
   * Creates a new VBusControl.
   */
  public RealDriveTrain() {
    Config4905 conf = Config4905.getConfig4905();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput) {

  }
}