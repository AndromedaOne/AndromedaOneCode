/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.driveTrainCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.*;
import frc.robot.subsystems.drivetrain.DriveTrain.DriveTrainMode;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

/**
 * Allows you to drive the robot using the drive controller.
 */
public class TeleOpCommand extends CommandBase {

  // Make the controllers a little easier to get to.
  private DriveController m_driveController = Robot.getInstance().getOIContainer()
      .getDriveController();
  private DriveTrain m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();
  private Config m_drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
  private Gyro4905 m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
  private int m_currentDelay = 0;
  private int kDelay = 0;
  private double m_savedRobotAngle = 0.0;
  private double kProportion = 0.0;

  private enum SlowMidFastModeStates {
    FASTMODEBUTTONRELEASED, FASTMODEBUTTONPRESSED, MIDMODEBUTTONRELEASED, MIDMODEBUTTONPRESSED,
    SLOWMODEBUTTONRELEASED, SLOWMODEBUTTONPRESSED,
  }

  private SlowMidFastModeStates m_slowMidFastMode = SlowMidFastModeStates.FASTMODEBUTTONRELEASED;

  /**
   * Takes inputs from the two joysticks on the drive controller.
   */
  public TeleOpCommand() {
    addRequirements(m_driveTrain);
    kDelay = m_drivetrainConfig.getInt("teleop.kdelay");
    kProportion = m_drivetrainConfig.getDouble("teleop.kproportion");
    if (Config4905.getConfig4905().isShowBot()) {
      m_slowMidFastMode = SlowMidFastModeStates.SLOWMODEBUTTONRELEASED;
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_currentDelay = 0;
    m_savedRobotAngle = m_gyro.getZAngle();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double forwardBackwardStickValue = m_driveController.getDriveTrainForwardBackwardStick();
    double rotateStickValue = m_driveController.getDriveTrainRotateStick();
    calculateSlowMidFastMode();
    // if the robot is not rotating, want to gyro correct to drive straight. but
    // if this correction kicks in right after the driver is turning, this will
    // cause
    // the robot to oscillate, so wait some kDelay before starting to correct if
    // driving straight
    if ((rotateStickValue == 0.0) && (m_currentDelay > kDelay)
        && (forwardBackwardStickValue != 0.0)) {
      rotateStickValue = -(m_savedRobotAngle - m_gyro.getZAngle()) * kProportion;
    } else if (rotateStickValue != 0.0) {
      m_savedRobotAngle = m_gyro.getZAngle();
      m_currentDelay = 0;
    } else if ((rotateStickValue == 0.0) && (forwardBackwardStickValue != 0)) {
      ++m_currentDelay;
      m_savedRobotAngle = m_gyro.getZAngle();
    } else if ((forwardBackwardStickValue == 0) && (rotateStickValue == 0)) {
      m_savedRobotAngle = m_gyro.getZAngle();
      m_currentDelay = 0;
    }
    if ((m_slowMidFastMode == SlowMidFastModeStates.SLOWMODEBUTTONPRESSED)
        || (m_slowMidFastMode == SlowMidFastModeStates.SLOWMODEBUTTONRELEASED)) {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.slowmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.slowmoderotatescale");
      m_driveTrain.setDriveTrainMode(DriveTrainMode.SLOW);
    } else if ((m_slowMidFastMode == SlowMidFastModeStates.MIDMODEBUTTONPRESSED)
        || (m_slowMidFastMode == SlowMidFastModeStates.MIDMODEBUTTONRELEASED)) {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.midmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.midmoderotatescale");
      m_driveTrain.setDriveTrainMode(DriveTrainMode.MID);
    } else {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.fastmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.fastmoderotatescale");
      m_driveTrain.setDriveTrainMode(DriveTrainMode.FAST);
    }
    SmartDashboard.putString("Teleop drive mode", m_driveTrain.getDriveTrainMode().toString());
        new TracePair("savedAngle", m_savedRobotAngle),
        new TracePair("rotateStick", rotateStickValue));
    // do not use moveWithGyro here as we're providing the drive straight correction
    m_driveTrain.move(forwardBackwardStickValue, -rotateStickValue, true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  private void calculateSlowMidFastMode() {
    switch (m_slowMidFastMode) {
    case FASTMODEBUTTONRELEASED:
      if (m_driveController.getDownShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.MIDMODEBUTTONPRESSED;
        System.out.println("DownShiftPressed, Entering MidMode");
      }
      break;

    case FASTMODEBUTTONPRESSED:
      if (m_driveController.getUpShiftReleased() && m_driveController.getDownShiftReleased()) {
        m_slowMidFastMode = SlowMidFastModeStates.FASTMODEBUTTONRELEASED;
        System.out.println("UpShiftReleased, Entering FastMode");
      }
      break;

    case MIDMODEBUTTONRELEASED:
      if (m_driveController.getDownShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.SLOWMODEBUTTONPRESSED;
        System.out.println("DownShiftPressed, Entering SlowMode");
      } else if (m_driveController.getUpShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.FASTMODEBUTTONPRESSED;
        System.out.println("UpShiftPressed, Entering FastMode");
      }
      break;

    case MIDMODEBUTTONPRESSED:
      if ((m_driveController.getDownShiftReleased() && m_driveController.getUpShiftReleased())) {
        m_slowMidFastMode = SlowMidFastModeStates.MIDMODEBUTTONRELEASED;
        System.out.println("DownShiftReleased, Entering MidMode");
      }
      break;

    case SLOWMODEBUTTONRELEASED:
      if (m_driveController.getUpShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.MIDMODEBUTTONPRESSED;
        System.out.println("UpShiftPressed, Entering MidMode");
      }
      break;

    case SLOWMODEBUTTONPRESSED:
      if (m_driveController.getDownShiftReleased() && m_driveController.getUpShiftReleased()) {
        m_slowMidFastMode = SlowMidFastModeStates.SLOWMODEBUTTONRELEASED;
        System.out.println("DownShiftPressed, Entering SlowMode");
      }
      break;

    default:
      System.out.println("WARNING: unknown state detected: " + m_slowMidFastMode.toString());
    }
  }

}
