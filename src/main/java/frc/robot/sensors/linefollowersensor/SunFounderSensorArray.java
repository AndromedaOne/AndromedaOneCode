package frc.robot.sensors.linefollowersensor;

import edu.wpi.first.wpilibj.I2C;

public class SunFounderSensorArray extends LineFollowerSensorBase {

  private LineFollowerSensorBase lineSensor;
  private I2C I2cBus;
  // - None of these values are properly set needs to be researched -
  private static final int NUM_SENSORS = 8;
  // Distance between sensors in centimeters
  private static final double DIST_BT_SENORS = 0;
  // Distance to sensors from center of rotation
  private static final double DIST_TO_SENSORS = 0;
  private static final int DETECTION_THRESHOLD = 0;
  private static final int THREAD_TIME = 50;

  public SunFounderSensorArray() {
    // TODO Add this to the config
    super("sunFounder");
  }

  /**
   * @return An array of booleans where true means that a line was detected and
   * false means that a line wasn't detected
   * @author Owen Salter
   */
  private boolean once = true;

  /**
   * Doesn't do anything right now.
   * 
   * @return structure containing a boolean of whether the line is found, and the
   * angle in radians
   */
  public void getSensorReading(int[] readingBuf) {
    /*
     * Need to: - figure out adj from DistanceToSensor - get hyp from adj and op -
     * use hyp to calculate angle - return angle
     * 
     * boolean[] boolBuf = new boolean[(this.numSensors / 2) + 1]; int senseCount =
     * 0; double adj1 = 0;
     * 
     * boolBuf = isThereLine(); // Get the adjacent for the angles for (int i = 0; i
     * < boolBuf.length; i++) { if (boolBuf[i] == true) { adj1 = getAdjacent(i);
     * senseCount++; } }
     *
     * 
     * if (senseCount != 0) { sensorReading.lineFound = true; } else {
     * sensorReading.lineFound = false; }
     */

  }
}