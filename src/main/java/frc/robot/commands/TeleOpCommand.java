/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.sensors.colorSensor.ColorSensor;
import frc.robot.subsystems.drivetrain.*;
import frc.robot.telemetries.Trace;

/**
 * Allows you to drive the robot using the drive controller.
 */
public class TeleOpCommand extends CommandBase {

  // Make the controllers a little easier to get to.
  private DriveController m_driveController = Robot.getInstance().getOIContainer().getDriveController();
  private DriveTrain m_driveTrain;
  private Config m_drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
  private boolean m_slowMode = false;
  private SlowModeStates m_slowModeState = SlowModeStates.NOTSLOWRELEASED;

<<<<<<< HEAD
  private ColorSensor m_frontColorSensor;
  private ColorSensor m_backColorSensor;
  public static final double DESIRED_COLOR_VALUE = 3.85;
  private double p_value = 1.0; 
=======
  private ColorSensor m_colorSensor;
  public static final double DESIRED_COLOR_VALUE = 3.70;
  private double m_lineFollowingP = 1.0;
  private double m_lineFollowingI = 0.0;
  private double m_lineFollowingD = 0.0;
>>>>>>> LineFollowerPID
  private boolean trackingLine = true;

  private LineFollowerCommand lineFollowerCommand;

  private enum SlowModeStates {
    NOTSLOWPRESSED, NOTSLOWRELEASED, SLOWPRESSED, SLOWRELEASED
  }

  /**
   * Takes inputs from the two joysticks on the drive controller.
   */
  public TeleOpCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();
<<<<<<< HEAD
    m_frontColorSensor = Robot.getInstance().getFrontColorSensor();
    m_backColorSensor = Robot.getInstance().getBackColorSensor();
=======
    m_colorSensor = Robot.getInstance().getColorSensor();
    lineFollowerCommand = new LineFollowerCommand(new PIDController4905SampleStop(
      "LineFollowing",
      m_lineFollowingP,
      m_lineFollowingI,
      m_lineFollowingD,
      0
    ),this);
    CommandScheduler.getInstance().schedule(lineFollowerCommand);
>>>>>>> LineFollowerPID
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double forwardBackwardStickValue = m_driveController.getForwardBackwardStick();
    double rotateStickValue = m_driveController.getRotateStick();

    switch (m_slowModeState) {
      case NOTSLOWRELEASED:
        if (m_driveController.getLeftBumperPressed()) {
          m_slowMode = true;
          Robot.getInstance().getSubsystemsContainer().getLEDs("LEDStringOne").setYellow(1.0);
          m_slowModeState = SlowModeStates.SLOWPRESSED;
          System.out.println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
        }
        break;
      case NOTSLOWPRESSED:
        if (m_driveController.getLeftBumperReleased()) {
          m_slowModeState = SlowModeStates.NOTSLOWRELEASED;
          System.out.println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
        }
        break;
      case SLOWRELEASED:
        if (m_driveController.getLeftBumperPressed()) {
          m_slowMode = false;
          Robot.getInstance().getSubsystemsContainer().getLEDs("LEDStringOne").setPurple(1.0);
          ;
          m_slowModeState = SlowModeStates.NOTSLOWPRESSED;
          System.out.println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
        }
        break;
      case SLOWPRESSED:
        if (m_driveController.getLeftBumperReleased()) {
          m_slowModeState = SlowModeStates.SLOWRELEASED;
          System.out.println("Slowmode state: " + m_slowModeState.toString() + "  SlowMode: " + m_slowMode);
        }
        break;
      default:
        System.err.println("WARN: Unknown slowmode state: " + m_slowModeState.toString());
        break;
    }

    if (m_slowMode) {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.forwardbackslowscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.rotateslowscale");
    }
    if (trackingLine) {
<<<<<<< HEAD
      boolean drivingForward = drivingForward(forwardBackwardStickValue);
      if(drivingForward) {
        rotateStickValue = (DESIRED_COLOR_VALUE - m_frontColorSensor.getValue());
      }else {
        rotateStickValue = (m_backColorSensor.getValue() - DESIRED_COLOR_VALUE);
      }
     
      rotateStickValue *= p_value * Math.abs(forwardBackwardStickValue);
      
=======
      rotateStickValue = m_turningValuePID * forwardBackwardStickValue;
>>>>>>> LineFollowerPID
    }


    m_driveTrain.move(forwardBackwardStickValue, -rotateStickValue, false);
  }

  
  private boolean drivingForward(double forwardBackwardStickValue) {
    return forwardBackwardStickValue > 0;
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

  private double m_turningValuePID;

  private class LineFollowerCommand extends PIDCommand4905 {

    public LineFollowerCommand(PIDController4905 controller, TeleOpCommand teleOpCommand) {
      super(controller, m_colorSensor::getValue, () -> DESIRED_COLOR_VALUE, (output) -> teleOpCommand.setTurningValuePID(output));
      
    }

    public void initialize() {
      Trace.getInstance().logCommandStart(this);
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      super.initialize();
      Trace.getInstance().logCommandStop(this);
    }
    
  }

  private void setTurningValuePID(double output) {
    m_turningValuePID = output;
  }

  
}
