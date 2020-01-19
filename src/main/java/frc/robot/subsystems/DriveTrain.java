/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
  private static WPI_TalonSRX driveTrainLeftMaster;
  private static WPI_TalonSRX driveTrainRightMaster;
  private static WPI_TalonSRX driveTrainLeftSlave;
  private static WPI_TalonSRX driveTrainRightSlave;
  private static DifferentialDrive differentialDrive;

  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    driveTrainLeftMaster = new WPI_TalonSRX(3);
    driveTrainRightMaster = new WPI_TalonSRX(1);
    driveTrainLeftSlave = new WPI_TalonSRX(4);
    driveTrainLeftSlave.configFactoryDefault();
    driveTrainLeftSlave.follow(driveTrainLeftMaster);
    driveTrainLeftSlave.setInverted(true);
    driveTrainRightSlave = new WPI_TalonSRX(2);
    driveTrainRightSlave.configFactoryDefault();
    driveTrainRightSlave.follow(driveTrainRightMaster);
    driveTrainRightSlave.setInverted(false);

    differentialDrive = new DifferentialDrive(driveTrainLeftMaster, driveTrainRightMaster);

    driveTrainLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
    driveTrainLeftMaster.setSensorPhase(true);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void move(double forwardBackSpeed, double rotateAmount) {
    differentialDrive.arcadeDrive(forwardBackSpeed, rotateAmount);
  }

  public double getPosition() {
    return (driveTrainLeftMaster.getSelectedSensorPosition());
  }

  public void resetEncoder() {
    driveTrainLeftMaster.setSelectedSensorPosition(0);
  }
}
