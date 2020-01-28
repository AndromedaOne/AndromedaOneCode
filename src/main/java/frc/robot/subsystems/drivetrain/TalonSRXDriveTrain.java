/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.TalonSRXController;

public class TalonSRXDriveTrain extends DriveTrain {
  private Config drivetrainConfig;

  private final TalonSRXController m_frontLeft;
  private final TalonSRXController m_backLeft;
  private final TalonSRXController m_frontRight;
  private final TalonSRXController m_backRight;

  public TalonSRXDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();

    m_frontLeft = new TalonSRXController(drivetrainConfig, "frontleft");
    m_backLeft = new TalonSRXController(drivetrainConfig, "backleft");
    m_frontRight = new TalonSRXController(drivetrainConfig, "frontright");
    m_backRight = new TalonSRXController(drivetrainConfig, "backright");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput) {
    // TODO Auto-generated method stub

  }

  @Override
  public double getEncoderPositionInches() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getEncoderVelocityInches() {
    // TODO Auto-generated method stub
    return 0;
  }
}
