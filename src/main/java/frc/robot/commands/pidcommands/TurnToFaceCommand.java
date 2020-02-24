package frc.robot.commands.pidcommands;

import java.util.BitSet;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.telemetries.Trace;

/**
 * Limelight Turn To Face Command.
 */
public class TurnToFaceCommand extends PIDCommand4905 {
  protected DoubleSupplier m_sensor;
  protected static Config m_conf = Config4905.getConfig4905().getPidConstantsConfig();
  protected Config m_conf2 = Config4905.getConfig4905().getSensorConfig();
  protected int m_lostCounter = 0;
  protected BitSet m_lostBuffer = new BitSet(50);
  SensorsContainer m_sensorsContainer = Robot.getInstance().getSensorsContainer();

  public TurnToFaceCommand(DoubleSupplier sensor) {
    super(
        // The controller that the command will use
        new PIDController4905("TurnToFace", 0, 0, 0, 0),
        // This should return the measurement
        sensor,
        // This should return the setpoint (can also be a constant)
        0.0,
        // This uses the output
        (lambda) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().moveUsingGyro(0.0, -lambda, false,
            false));

    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());

    getController().setP(m_conf.getDouble("TurnToFaceCommand.Kp"));
    getController().setI(m_conf.getDouble("TurnToFaceCommand.Ki"));
    getController().setD(m_conf.getDouble("TurnToFaceCommand.Kd"));
    getController().setMinOutputToMove(m_conf.getDouble("TurnToFaceCommand.minOutputToMove"));
    getController().setTolerance(m_conf.getDouble("TurnToFaceCommand.positionTolerance"),
        m_conf.getDouble("TurnToFaceCommand.velocityTolerance"));
    this.m_sensor = sensor;
  }

  @Override
  public boolean isFinished() {
    m_lostCounter++;
    m_lostCounter = m_lostCounter % 50;
    boolean targetFound = m_sensorsContainer.getLimeLight().targetLock();

    m_lostBuffer.set(m_lostCounter, !targetFound);
    if (m_lostBuffer.cardinality() == 50) {
      Trace.getInstance().logCommandInfo("TurnToFaceCommand", "No target found for one second");
      return true;
    }
    if (!m_conf2.hasPath("limelight") || m_conf2.getDouble("limelight.cameraHeight") == 0.0) {
      Trace.getInstance().logCommandInfo("TurnToFaceCommand", "No Limelight Sensor");
      return true;
    } else {
      double angle = m_sensor.getAsDouble();

      if (m_lostCounter == 1) {
        LimeLightCameraBase limelight = m_sensorsContainer.getLimeLight();
        System.out.println("limelight," + limelight.verticalRadiansToTarget() + " " + limelight.distanceToPowerPort());
      }

      boolean returnValue = this.getController().atSetpoint() && targetFound;
      return returnValue;
    }
  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
  }
}