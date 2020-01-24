package frc.robot.sensors.ultrasonicsensor;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.sensors.SensorBase;

public abstract class UltrasonicSensor extends SensorBase implements PIDSource {

  /**
   * @return the distance in inches from whatever is in front of the Ultrasonic
   */
  public abstract double getDistanceInches();

  public double getMinDistanceInches() {
    return getDistanceInches();
  }

  @Override
  /**
   * Gets the distance in inches returned by the ultrasonic and passes it to a PID
   * Controller
   */
  public double pidGet() {
    return getDistanceInches();
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

}