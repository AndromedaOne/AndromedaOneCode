package frc.robot.commands.driveTrainCommands;

import java.util.function.BooleanSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class SwerveTeleOpCommand extends Command {

  private DriveController m_driveController = Robot.getInstance().getOIContainer()
      .getDriveController();
  private DriveTrainBase m_driveTrain = Robot.getInstance().getSubsystemsContainer()
      .getDriveTrain();
  private Config m_drivetrainConfig = Config4905.getConfig4905().getSwerveDrivetrainConfig();
  private Gyro4905 m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
  private int m_currentDelay = 0;
  private int m_kDelay = 0;
  private double m_savedRobotAngle = 0.0;
  private double m_kProportion = 0.0;
  private boolean m_isStrafe = true;

  private enum SlowMidFastModeStates {
    FASTMODEBUTTONRELEASED, FASTMODEBUTTONPRESSED, MIDMODEBUTTONRELEASED, MIDMODEBUTTONPRESSED,
    SLOWMODEBUTTONRELEASED, SLOWMODEBUTTONPRESSED,
  }

  private SlowMidFastModeStates m_slowMidFastMode = SlowMidFastModeStates.FASTMODEBUTTONRELEASED;

  private BooleanSupplier m_robotCentricSup;
  private double m_kp = 1;

// private DriveTrain m_driveTrain =
// Robot.getInstance().getSubsystemContainer().getDriveTrain();

  public SwerveTeleOpCommand(BooleanSupplier robotCentricSup) {
    if (Config4905.getConfig4905().doesTankDrivetrainExist()) {
      m_isStrafe = false;
      m_drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    }

    addRequirements(m_driveTrain.getSubsystemBase());

    m_kDelay = m_drivetrainConfig.getInt("teleop.kdelay");
    m_kProportion = m_drivetrainConfig.getDouble("teleop.kproportion");
    if (Config4905.getConfig4905().isShowBot() || Config4905.getConfig4905().isTopGun()) {
      m_slowMidFastMode = SlowMidFastModeStates.SLOWMODEBUTTONRELEASED;
    }
    m_robotCentricSup = robotCentricSup;
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_currentDelay = 0;
    m_savedRobotAngle = m_gyro.getZAngle();
  }

  @Override
  public void execute() {
    double forwardBackwardStickValue = m_driveController.getDriveTrainForwardBackwardStick();
    double strafeStickValue = 0;
    if (m_isStrafe) {
      strafeStickValue = m_driveController.getSwerveDriveTrainStrafeAxis();
    }
    double rotateStickValue = m_driveController.getDriveTrainRotateStick();
    calculateSlowMidFastMode();
    // if the robot is not rotating, want to gyro correct to drive straight. but
    // if this correction kicks in right after the driver is turning, this will
    // cause
    // the robot to oscillate, so wait some kDelay before starting to correct if
    // driving straight
    if ((rotateStickValue == 0.0) && (m_currentDelay > m_kDelay)
        && (forwardBackwardStickValue != 0.0)) {
      rotateStickValue = -(m_savedRobotAngle - m_gyro.getZAngle()) * m_kProportion;
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
      m_driveTrain.setDriveTrainMode(DriveTrainModeEnum.SLOW);
    } else if ((m_slowMidFastMode == SlowMidFastModeStates.MIDMODEBUTTONPRESSED)
        || (m_slowMidFastMode == SlowMidFastModeStates.MIDMODEBUTTONRELEASED)) {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.midmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.midmoderotatescale");
      m_driveTrain.setDriveTrainMode(DriveTrainModeEnum.MID);
    } else {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.fastmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.fastmoderotatescale");
      m_driveTrain.setDriveTrainMode(DriveTrainModeEnum.FAST);
    }
    SmartDashboard.putString("Teleop drive mode", m_driveTrain.getDriveTrainMode().toString());
    Trace.getInstance().addTrace(true, "TeleopDrive", new TracePair("Gyro", m_gyro.getZAngle()),
        new TracePair("savedAngle", m_savedRobotAngle),
        new TracePair("rotateStick", rotateStickValue));
    // do not use moveWithGyro here as we're providing the drive straight correction
    if (m_isStrafe) {
      m_driveTrain.move(forwardBackwardStickValue, strafeStickValue, rotateStickValue,
          !m_robotCentricSup.getAsBoolean(), true);
    } else {
      m_driveTrain.move(forwardBackwardStickValue, -rotateStickValue, true);
    }

  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interupted) {
    Trace.getInstance().logCommandStop(this);
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
