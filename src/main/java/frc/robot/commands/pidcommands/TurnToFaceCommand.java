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
  protected int m_targetCounter = 0;
  protected BitSet m_targetBuffer = new BitSet(4);
  SensorsContainer m_sensorsContainer = Robot.getInstance().getSensorsContainer();
  LimeLightCameraBase limelight;

  public TurnToFaceCommand(DoubleSupplier sensor) {
    super(
        // The controller that the command will use
        new PIDController4905("TurnToFace", 0.0, 0.0, 0.0, 0.0),
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
    getController().setTolerance(m_conf.getDouble("TurnToFaceCommand.positionTolerance"));
    this.m_sensor = sensor;
  }

  @Override
  public boolean isFinished() {
    m_lostCounter++;
    m_lostCounter = m_lostCounter % 250;

    boolean targetFound = m_sensorsContainer.getLimeLight().targetLock();
    m_lostBuffer.set(m_lostCounter, !targetFound);

    m_targetCounter++;
    m_targetCounter %= 4;
    m_targetBuffer.set(m_targetCounter, this.getController().atSetpoint());

    if (m_lostBuffer.cardinality() == 250) {
      Trace.getInstance().logCommandInfo(this, "No target found for one second");
      return true;
    }
    if (!m_conf2.hasPath("limelight") || m_conf2.getDouble("limelight.cameraHeight") == 0.0) {
      Trace.getInstance().logCommandInfo(this, "No Limelight Sensor");
      return true;
    } else {
      if (m_lostCounter == 1) {
        System.out.println("limelight," + limelight.verticalRadiansToTarget() + " " + limelight.distanceToPowerPort());
      }

      boolean returnValue = m_targetBuffer.cardinality() == 4 && targetFound;
      return returnValue;
    }
  }

  @Override
  public void initialize() {
    super.initialize();
    limelight = m_sensorsContainer.getLimeLight();
    limelight.enableLED();
    System.out.println("Turning on lime light");
    m_lostCounter = 0;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    System.out.println("Turning off lime light");
    limelight.disableLED();
  }
}