/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.pidsubsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class PIDDriveTrain extends PIDSubsystem {

  public WPI_TalonSRX driveRight;
  public WPI_TalonSRX driveLeft;
  
  
  /**
   * Creates a new PIDDriveTrain.
   */
  public PIDDriveTrain() {
    super(
        // The PIDController used by the subsystem
        new PIDController(0, 0, 0));
    driveRight = new WPI_TalonSRX(4);
    driveLeft = new WPI_TalonSRX(2);
    
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return 0;
  }
}
