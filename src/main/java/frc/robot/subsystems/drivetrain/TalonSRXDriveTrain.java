/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Config4905;
import frc.robot.actuators.TalonSRXController;

public class TalonSRXDriveTrain extends RealDriveTrain {
  private Config drivetrainConfig;

  private final TalonSRXController m_frontLeft;
  private final TalonSRXController m_backLeft;
  private final TalonSRXController m_frontRight;
  private final TalonSRXController m_backRight;

  // motors on the Left side of the drive
  private SpeedControllerGroup m_leftmotors;

  // motors on the right side of the drive
  private SpeedControllerGroup m_rightmotors;

  private int ticksPerInch;

  public TalonSRXDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();

    m_frontLeft = new TalonSRXController(drivetrainConfig, "frontleft");
    m_backLeft = new TalonSRXController(drivetrainConfig, "backleft");
    m_frontRight = new TalonSRXController(drivetrainConfig, "frontright");
    m_backRight = new TalonSRXController(drivetrainConfig, "backright");

    // motors on the left side of the drive
    m_leftmotors = new SpeedControllerGroup(m_frontLeft, m_backLeft);

    // motors on the right side of the drive.
    m_rightmotors = new SpeedControllerGroup(m_frontRight, m_backRight);

    
    ticksPerInch = drivetrainConfig.getInt("ticksPerInch");
    System.out.println("Done configuring TalonSRXDriveTrain");
  }

  @Override
  public double getRobotPositionInches() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getRobotVelocityInches() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected SpeedControllerGroup getLeftSpeedControllerGroup() {
    return m_leftmotors;
  }

  @Override
  protected SpeedControllerGroup getRightSpeedControllerGroup() {
    return m_rightmotors;
  }
}
