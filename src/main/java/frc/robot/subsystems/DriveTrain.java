/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase {
  public WPI_TalonSRX driveLeftMaster;
  public WPI_TalonSRX driveRightMaster;
  public WPI_TalonSRX driveRightSlave;
  public WPI_TalonSRX driveLeftSlave;
  public DifferentialDrive diffDrive;

  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    driveLeftMaster = initTalon(2);
    driveLeftSlave = initTalon(1);
    driveRightMaster = initTalon(4);
    driveRightSlave = initTalon(3);

    driveRightMaster.setInverted(true);
    diffDrive = new DifferentialDrive(driveLeftMaster, driveRightMaster);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  private WPI_TalonSRX initTalon(int deviceId) {
    WPI_TalonSRX _talon = new WPI_TalonSRX(deviceId);

    _talon.configFactoryDefault();

    _talon.setNeutralMode(NeutralMode.Brake);


    _talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    _talon.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.QuadEncoder, 0);

    _talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

    // Ensure no stale data
    _talon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20);
    
    _talon.config_kP(Constants.kSlot_Velocit, Constants.kGains_Velocit.kP, Constants.kTimeoutMs);
		_talon.config_kI(Constants.kSlot_Velocit, Constants.kGains_Velocit.kI, Constants.kTimeoutMs);
		_talon.config_kD(Constants.kSlot_Velocit, Constants.kGains_Velocit.kD, Constants.kTimeoutMs);
		_talon.config_kF(Constants.kSlot_Velocit, Constants.kGains_Velocit.kF, Constants.kTimeoutMs);

    int closedLoopTimeMs = 1;
    _talon.configClosedLoopPeriod(0, closedLoopTimeMs, Constants.kTimeoutMs);
    _talon.configClosedLoopPeriod(1, closedLoopTimeMs, Constants.kTimeoutMs);
    return _talon;
  }
}
