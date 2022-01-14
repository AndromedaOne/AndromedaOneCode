/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.*;
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
  private boolean m_slowMode = false;
  private SlowModeStates m_slowModeState = SlowModeStates.NOTSLOWRELEASED;
  private int m_currentDelay = 0;
  private int kDelay = 0;
  private double m_savedRobotAngle = 0.0;
  private double kProportion = 0.0;

  private enum SlowModeStates {
    NOTSLOWPRESSED, NOTSLOWRELEASED, SLOWPRESSED, SLOWRELEASED
  }

  /**
   * Takes inputs from the two joysticks on the drive controller.
   */
  public TeleOpCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveTrain);
    kDelay = m_drivetrainConfig.getInt("teleop.kdelay");
    kProportion = m_drivetrainConfig.getDouble("teleop.kproportion");
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

    // calc slow mode must come first
    calculateSlowMode();
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
    if (m_slowMode) {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.forwardbackslowscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.rotateslowscale");
    }
    Trace.getInstance().addTrace(true, "TeleopDrive",
        new TracePair<Double>("Gyro", m_gyro.getZAngle()),
        new TracePair<>("savedAngle", m_savedRobotAngle),
        new TracePair<>("rotateStick", rotateStickValue));
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

  private void calculateSlowMode() {
    switch (m_slowModeState) {
    case NOTSLOWRELEASED:
      if (m_driveController.getSlowModeBumperPressed()) {
        m_slowMode = true;
        Robot.getInstance().getSubsystemsContainer().getLEDs("LEDStringOne").setYellow(1.0);
        m_slowModeState = SlowModeStates.SLOWPRESSED;
        System.out
            .println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
      }
      break;
    case NOTSLOWPRESSED:
      if (m_driveController.getSlowModeBumperReleased()) {
        m_slowModeState = SlowModeStates.NOTSLOWRELEASED;
        System.out
            .println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
      }
      break;
    case SLOWRELEASED:
      if (m_driveController.getSlowModeBumperPressed()) {
        m_slowMode = false;
        Robot.getInstance().getSubsystemsContainer().getLEDs("LEDStringOne").setPurple(1.0);
        m_slowModeState = SlowModeStates.NOTSLOWPRESSED;
        System.out
            .println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
      }
      break;
    case SLOWPRESSED:
      if (m_driveController.getSlowModeBumperReleased()) {
        m_slowModeState = SlowModeStates.SLOWRELEASED;
        System.out
            .println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
      }
      break;
    default:
      System.err.println("WARN: Unknown slowmode state: " + m_slowModeState.toString());
      break;
    }
  }
}
