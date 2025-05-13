package frc.robot.commands.driveTrainCommands;

import java.util.function.BooleanSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.math.MathUtil;
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

public class TeleOpCommand extends Command {

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

// use this constructor for SwerveDrive
  public TeleOpCommand(BooleanSupplier robotCentricSup) {
    if (Config4905.getConfig4905().doesTankDrivetrainExist()) {
      m_isStrafe = false;
      m_drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    }

    addRequirements(m_driveTrain.getSubsystemBase());

    m_kDelay = m_drivetrainConfig.getInt("teleop.kdelay");
    m_kProportion = m_drivetrainConfig.getDouble("teleop.kproportion");
    m_robotCentricSup = robotCentricSup;
  }

  // use this constructor for TankDrive
  public TeleOpCommand() {
    this(() -> true);
  }

  @Override
  public void initialize() {
    m_currentDelay = 0;
    m_savedRobotAngle = m_gyro.getZAngle();
  }

  public static double getExponential(final double input, final double exponent,
      final double weight, final double deadband) {
    if (Math.abs(input) < deadband) {
      return 0;
    }
    double sign = Math.signum(input);
    double v = Math.abs(input);

    double a = weight * Math.pow(v, exponent) + (1 - weight) * v;
    double b = weight * Math.pow(deadband, exponent) + (1 - weight) * deadband;
    v = (a - 1 * b) / (1 - b);

    v *= sign;
    return v;
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
      strafeStickValue *= m_drivetrainConfig.getDouble("teleop.slowmodefowardbackscale");
      m_driveTrain.setDriveTrainMode(DriveTrainModeEnum.SLOW);
    } else if ((m_slowMidFastMode == SlowMidFastModeStates.MIDMODEBUTTONPRESSED)
        || (m_slowMidFastMode == SlowMidFastModeStates.MIDMODEBUTTONRELEASED)) {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.midmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.midmoderotatescale");
      strafeStickValue *= m_drivetrainConfig.getDouble("teleop.midmodefowardbackscale");
      m_driveTrain.setDriveTrainMode(DriveTrainModeEnum.MID);
    } else {
      forwardBackwardStickValue *= m_drivetrainConfig.getDouble("teleop.fastmodefowardbackscale");
      rotateStickValue *= m_drivetrainConfig.getDouble("teleop.fastmoderotatescale");
      strafeStickValue *= m_drivetrainConfig.getDouble("teleop.fastmodefowardbackscale");
      m_driveTrain.setDriveTrainMode(DriveTrainModeEnum.FAST);
    }
    SmartDashboard.putString("Teleop drive mode", m_driveTrain.getDriveTrainMode().toString());
    Trace.getInstance().addTrace(true, "TeleopDrive", new TracePair("Gyro", m_gyro.getZAngle()),
        new TracePair("savedAngle", m_savedRobotAngle),
        new TracePair("rotateStick", rotateStickValue));
    double exponent = (m_isStrafe ? 1 : 3);
    forwardBackwardStickValue = Math.pow(forwardBackwardStickValue, exponent);
    // removed strafe for more fine control
    // strafe should be in there, it's not being exponented at the moment and it
    // needs to be

    double magnitude = MathUtil.clamp(
        getExponential(Math.hypot(strafeStickValue, forwardBackwardStickValue), 3.6, 0.75, .1), -1,
        1) * 1;
    double angle = Math.atan2(forwardBackwardStickValue, strafeStickValue);
    strafeStickValue = Math.cos(angle) * magnitude;
    forwardBackwardStickValue = Math.sin(angle) * magnitude;

    // if (forwardBackwardStickValue == 0) {
    // strafeStickValue = Math.pow(strafeStickValue, exponent);
    // }
    // it's here now
    // when the strafe is exponented while the forwardback isn't zero, it screws up
    // it can't be zero but it needs to be low
    rotateStickValue = Math.pow(rotateStickValue, exponent);
    // do not use moveWithGyro here as we're providing the drive straight correction
    if (m_isStrafe) {
      m_driveTrain.move(forwardBackwardStickValue, strafeStickValue,
      rotateStickValue,
      !m_robotCentricSup.getAsBoolean(), true);
      } else {
      m_driveTrain.move(forwardBackwardStickValue, -rotateStickValue, false);
    }

  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interupted) {
  }

  private void calculateSlowMidFastMode() {
    switch (m_slowMidFastMode) {
    case FASTMODEBUTTONRELEASED:
      if (m_driveController.getDownShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.MIDMODEBUTTONPRESSED;
        Trace.getInstance().logCommandInfo(this, "DownShiftPressed, Entering MidMode");
      }
      break;

    case FASTMODEBUTTONPRESSED:
      if (m_driveController.getUpShiftReleased() && m_driveController.getDownShiftReleased()) {
        m_slowMidFastMode = SlowMidFastModeStates.FASTMODEBUTTONRELEASED;
        Trace.getInstance().logCommandInfo(this, "UpShiftReleased, Entering FastMode");
      }
      break;

    case MIDMODEBUTTONRELEASED:
      if (m_driveController.getDownShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.SLOWMODEBUTTONPRESSED;
        Trace.getInstance().logCommandInfo(this, "DownShiftPressed, Entering SlowMode");
      } else if (m_driveController.getUpShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.FASTMODEBUTTONPRESSED;
        Trace.getInstance().logCommandInfo(this, "UpShiftPressed, Entering FastMode");
      }
      break;

    case MIDMODEBUTTONPRESSED:
      if ((m_driveController.getDownShiftReleased() && m_driveController.getUpShiftReleased())) {
        m_slowMidFastMode = SlowMidFastModeStates.MIDMODEBUTTONRELEASED;
        Trace.getInstance().logCommandInfo(this, "DownShiftReleased, Entering MidMode");
      }
      break;

    case SLOWMODEBUTTONRELEASED:
      if (m_driveController.getUpShiftPressed()) {
        m_slowMidFastMode = SlowMidFastModeStates.MIDMODEBUTTONPRESSED;
        Trace.getInstance().logCommandInfo(this, "UpShiftPressed, Entering MidMode");
      }
      break;

    case SLOWMODEBUTTONPRESSED:
      if (m_driveController.getDownShiftReleased() && m_driveController.getUpShiftReleased()) {
        m_slowMidFastMode = SlowMidFastModeStates.SLOWMODEBUTTONRELEASED;
        Trace.getInstance().logCommandInfo(this, "DownShiftPressed, Entering SlowMode");
      }
      break;

    default:
      Trace.getInstance().logCommandInfo(this,
          "WARNING: unknown state detected: " + m_slowMidFastMode.toString());
    }
  }
}
