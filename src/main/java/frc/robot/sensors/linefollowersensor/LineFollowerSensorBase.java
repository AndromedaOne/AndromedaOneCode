package frc.robot.sensors.linefollowersensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public abstract class LineFollowerSensorBase {

  public abstract void getSensorReading(int[] readingBuf);

  private Config lineConf = Robot.getConfig().getConfig("sensors.lineFollowSensor");
  private LineFollowArraySensorReading sensorReading = new LineFollowArraySensorReading();
  private GetSensorData sensorDataThread;
  private double currentDistanceFromCenter = 0;
  private double currentLineFoundSensor = 0;
  // Default Distance to sensor array in centimetres
  protected double distanceToSensor;
  // Distance between sensors in centimetres
  protected double distanceBtSensors;
  protected int detectionThreshold;
  protected int numSensors;
  protected double threadTime = 0.02;
  protected int[] sensorReadingBuffer;
  private boolean sensorsReadLeftToRight = true;

  protected LineFollowerSensorBase(String sensorConfigName) {
    detectionThreshold = lineConf.getInt(sensorConfigName + ".detectionThreshold");
    distanceToSensor = lineConf.getInt(sensorConfigName + ".distanceToSensor");
    distanceBtSensors = lineConf.getDouble(sensorConfigName + ".distanceBtSensors");
    numSensors = lineConf.getInt(sensorConfigName + ".numSensors");
    threadTime = lineConf.getDouble(sensorConfigName + ".threadDelay");
    sensorsReadLeftToRight = lineConf.getBoolean(sensorConfigName + ".sensorsReadLeftToRight");
    sensorDataThread = new GetSensorData();
    sensorDataThread.start();
  }

  private class GetSensorData extends Thread {
    int[] mySensorReadingBuffer = new int[numSensors];
    int[] returnSensorReadingBuffer = new int[numSensors];

    public void run() {
      while (true) {
        getSensorReading(mySensorReadingBuffer);
        updateSensorReadingBuffer();
        SmartDashboard.putNumber("Line Sensor 1", returnSensorReadingBuffer[0]);
        SmartDashboard.putNumber("Line Sensor 2", returnSensorReadingBuffer[1]);
        SmartDashboard.putNumber("Line Sensor 3", returnSensorReadingBuffer[2]);
        SmartDashboard.putNumber("Line Sensor 4", returnSensorReadingBuffer[3]);
        SmartDashboard.putNumber("Line Sensor 5", returnSensorReadingBuffer[4]);
        SmartDashboard.putNumber("Line Sensor 6", returnSensorReadingBuffer[5]);
        SmartDashboard.putNumber("Line Sensor 7", returnSensorReadingBuffer[6]);
        SmartDashboard.putNumber("Line Sensor 8", returnSensorReadingBuffer[7]);
        SmartDashboard.putNumber("Distance From Center", currentDistanceFromCenter);
        SmartDashboard.putBoolean("Found Line", findLine().lineFound);
        SmartDashboard.putNumber("Line Angle", findLine().lineAngle);
        Trace.getInstance().addTrace(true, "LineFollower", new TracePair<>("LS0", returnSensorReadingBuffer[0]),
            new TracePair<>("LS1", returnSensorReadingBuffer[1]), new TracePair<>("LS2", returnSensorReadingBuffer[2]),
            new TracePair<>("LS3", returnSensorReadingBuffer[3]), new TracePair<>("LS4", returnSensorReadingBuffer[4]),
            new TracePair<>("LS5", returnSensorReadingBuffer[5]), new TracePair<>("LS6", returnSensorReadingBuffer[6]),
            new TracePair<>("LS7", returnSensorReadingBuffer[7]),
            new TracePair<>("DistFromCenter", currentDistanceFromCenter),
            new TracePair<>("LineAngle", findLine().lineAngle));
        Timer.delay(threadTime);
      }
    }

    private synchronized void updateSensorReadingBuffer() {
      returnSensorReadingBuffer = mySensorReadingBuffer.clone();
    }

    public synchronized int[] getSensorReadingBuffer() {
      return returnSensorReadingBuffer;
    }
  }

  /**
   * Determines if an integer is even or odd
   */
  private static Boolean isEven(Integer i) {
    return (i % 2) == 0;
  }

  /**
   * Gets the distance from the center of the sensor (assuming 0 is the leftmost
   * sensor and there are an even number of sensors)
   */
  private double calculateDistanceFromCenter(double sensor) {
    double distFromSensor;
    // Get the number of sensors on each side of the center
    if (isEven(numSensors)) {
      int halfNumSensors = numSensors / 2;
      double sensorsOffSet = (halfNumSensors * distanceBtSensors) - (distanceBtSensors / 2);
      distFromSensor = (distanceBtSensors * sensor) - sensorsOffSet;
      distFromSensor = sensorsReadLeftToRight ? distFromSensor : -distFromSensor;
      return distFromSensor;
    } else {
      // There is no case for an odd number of sensors yet
      System.err.println("ERROR: Line Sensors with an odd number of sensors are not supported");
      return 0;
    }
  }

  private double calculateLineAngleFromCenter(double distFromCenter) {
    double angle = Math.atan2(distFromCenter, distanceToSensor);
    return angle;
  }

  /**
   * This function also records which sensor sees a line, which is important in
   * calculating a line angle. So you must call this to get an accurate angle from
   * center
   * 
   * @return This will return a boolean stating wether or not we are detecting a
   *         line
   * @author Owen Salter & Devin C
   */
  private boolean isThereLine() {
    int highestValue = sensorReadingBuffer[0];
    int savedIndex = 0;
    double lineFoundSensor = 0;

    for (int i = 1; i < sensorReadingBuffer.length; i++) {
      if (sensorReadingBuffer[i] > highestValue) {
        highestValue = sensorReadingBuffer[i];
        savedIndex = i;
        lineFoundSensor = savedIndex;
      } else if (sensorReadingBuffer[i] == highestValue) {
        lineFoundSensor = (savedIndex + i) / 2;
      }
    }
    currentDistanceFromCenter = calculateDistanceFromCenter(lineFoundSensor);
    return highestValue >= detectionThreshold;
  }

  /**
   * @return This will return wether or not we found a line and what angle we
   *         found the line at as a type LineFollowArraySensorReading
   */
  public LineFollowArraySensorReading findLine() {
    sensorReadingBuffer = sensorDataThread.getSensorReadingBuffer();
    sensorReading.lineFound = isThereLine();
    sensorReading.lineAngle = calculateLineAngleFromCenter(currentDistanceFromCenter);
    return sensorReading;
  }

}
