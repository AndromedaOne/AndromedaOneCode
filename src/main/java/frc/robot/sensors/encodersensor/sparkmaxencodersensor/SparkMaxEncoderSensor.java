package frc.robot.sensors.encodersensor.sparkmaxencodersensor;

public abstract class SparkMaxEncoderSensor {
    // TODO Update ticks per inch, it's 1 for now so we don't divide by 0
    int ticksPerInch = 1;

    /**
     * @return the distance in ticks that the Spark Max encoder has travelled
     */
    public abstract double getDistanceTicks();

    /**
     * @return the distance in inches that the Spark Max encoder has travelled
     */
    public abstract double getDistanceInches();

    /**
     * @return the rate of the Spark Max encoder in ticks per second
     */
    public abstract double getVelocity();

}