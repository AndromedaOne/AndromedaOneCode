package frc.robot.commands.pidcommands;

import java.util.BitSet;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;

/**
 * Limelight Turn To Face Command.
 */
public class TurnToFaceCommand extends PIDCommand4905 {
  DoubleSupplier sensor;
  boolean lastSetpoint = false;
  static Config conf = Config4905.getConfig4905().getPidConstantsConfig();
  Config conf2 = Config4905.getConfig4905().getSensorConfig();
  int nanCounter = 0;
  BitSet nanBuffer = new BitSet(50);

  public TurnToFaceCommand(DoubleSupplier sensor) {
    super(
        new PIDController4905("Turn To Face PID", conf.getDouble("TurnToFaceCommand.Kp"),
            conf.getDouble("TurnToFaceCommand.Ki"), conf.getDouble("TurnToFaceCommand.Kd"),
            conf.getDouble("TurnToFaceCommand.minOutputToMove")),
        sensor, 0.0, (lambda) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().moveUsingGyro(0.0,
            -lambda, true, true));
    this.getController().setTolerance(0.1);
    this.sensor = sensor;
  }

  @Override
  public boolean isFinished() {
    nanCounter++;
    nanCounter = nanCounter % 50;
    nanBuffer.set(nanCounter, sensor.getAsDouble() == Double.NaN);
    if (nanBuffer.cardinality() == 50) {
      return true;
    }
    if (conf2.getDouble("limelight.cameraHeight") == 0.0) {
      return true;
    } else {
      double angle = sensor.getAsDouble();
      boolean returnValue = this.getController().atSetpoint() && lastSetpoint;
      lastSetpoint = this.getController().atSetpoint();
      return returnValue;
    }
  }
}