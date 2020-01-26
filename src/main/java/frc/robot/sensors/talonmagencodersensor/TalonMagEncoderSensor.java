package frc.robot.sensors.talonmagencodersensor;

public abstract class TalonMagEncoderSensor {

  /**
   * @return the distance in ticks that the MagEncoder has traveled
   */
  public abstract double getDistanceTicks();

  /**
   * @return the velocity of the MagEncoder in ticks per second
   */
  public abstract double getVelocity();

  /**
   * sets the initial value to the current value
   */
  public abstract void reset();

  /**
   * sets the initial value to the value specified
   */
  public abstract void resetTo(double value);

}