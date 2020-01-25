package frc.robot.sensors.encodersensor;

public interface EncoderBase {

  /**
   * @return the distance in ticks that the encoder has travelled
   */
  public abstract double getDistanceTicks();

  /**
   * @return the rate of the encoder in ticks per second
   */
  public abstract double getRate();

  /**
   * Resets the encoder
   */
  public abstract void reset();
}