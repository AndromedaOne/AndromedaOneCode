package frc.robot.sensors.magencodersensor;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.sensors.SensorBase;

public abstract class MagEncoderSensor extends SensorBase implements PIDSource {

  /**
   * @return the distance in ticks that the MagEncoder has traveled
   */
  public abstract double getDistanceTicks();

  /**
   * @return the velocity of the MagEncoder in ticks per second
   */
  public abstract double getVelocity();

  @Override
  /**
   * Gets the distance in ticks to pass to a PID controller
   */
  public double pidGet() {
    return getDistanceTicks();
  }

  @Override
  /**
   * Does not do anything
   */
  public void setPIDSourceType(PIDSourceType pidSource) {

  }

  @Override
  /**
   * @return kDisplacement because that is what we use for all of our PID
   * Controllers
   */
  public PIDSourceType getPIDSourceType() {
    return PIDSourceType.kDisplacement;
  }

  /**
   * sets the initial value to the current value
   */
  public abstract void reset();

  /**
   * sets the initial value to the value specified
   */
  public abstract void resetTo(double value);

}