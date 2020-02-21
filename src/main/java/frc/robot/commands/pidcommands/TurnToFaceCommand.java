package frc.robot.commands.pidcommands;

import java.util.BitSet;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.SensorsContainer;
import frc.robot.telemetries.Trace;

/**
 * Limelight Turn To Face Command.
 */
public class TurnToFaceCommand extends PIDCommand4905 {
  protected DoubleSupplier m_sensor;
  protected boolean m_lastSetpoint = false;
  protected static Config m_conf = Config4905.getConfig4905().getPidConstantsConfig();
  protected Config m_conf2 = Config4905.getConfig4905().getSensorConfig();
  protected int m_nanCounter = 0;
  protected BitSet m_nanBuffer = new BitSet(50);
  SensorsContainer m_sensorsContainer = Robot.getInstance().getSensorsContainer();

  public TurnToFaceCommand(DoubleSupplier sensor) {
    super(
        new PIDController4905("Turn To Face PID", m_conf.getDouble("TurnToFaceCommand.Kp"),
            m_conf.getDouble("TurnToFaceCommand.Ki"), m_conf.getDouble("TurnToFaceCommand.Kd"),
            m_conf.getDouble("TurnToFaceCommand.minOutputToMove")),
        sensor, 0.0, (lambda) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().moveUsingGyro(0.0,
            -lambda, true, true));
    this.getController().setTolerance(0.1);
    this.m_sensor = sensor;
  }

  @Override
  public boolean isFinished() {
    m_nanCounter++;
    m_nanCounter = m_nanCounter % 50;
    m_nanBuffer.set(m_nanCounter, m_sensor.getAsDouble() == Double.NaN);
    if (m_nanBuffer.cardinality() == 50) {
      Trace.getInstance().logCommandInfo("TurnToFaceCommand", "No target found for one second");
      return true;
    }
    if (m_conf2.getDouble("limelight.cameraHeight") == 0.0 || !m_conf2.hasPath("limelight")) {
      Trace.getInstance().logCommandInfo("TurnToFaceCommand", "No Limelight Sensor");
      return true;
    } else {
      double angle = m_sensor.getAsDouble();
      boolean returnValue = this.getController().atSetpoint() && m_lastSetpoint;
      m_lastSetpoint = this.getController().atSetpoint();
      if (returnValue) {
        // TODO: Change the placeholder to the real number.
        Trace.getInstance().logCommandInfo("TurnToFaceCommand", "Target found, angle is currently " + angle
            + " and distance is currently " + m_sensorsContainer.getLimeLight().distanceToTarget(45));
      }
      return returnValue;
    }
  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart("TurnToFaceCommand");
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop("TurnToFaceCommand");
  }
}