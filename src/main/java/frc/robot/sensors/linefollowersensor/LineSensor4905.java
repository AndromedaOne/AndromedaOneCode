/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors.linefollowersensor;

import java.time.Duration;
import java.time.Instant;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import frc.robot.Robot;

/**
 * This line sensor is the line sensor that currently uses 8 Pololu sensors
 * Which is read by an Ardruino, then the values are sent to the robo rio via
 * I2C
 */
public class LineSensor4905 extends LineFollowerSensorBase {

  private static I2C i2c = new I2C(I2C.Port.kOnboard, 2);
  private Config lineConf = Robot.getConfig().getConfig("sensors.lineFollowSensor.lineFollowSensor4905");
  private final int NUM_SENSORS = lineConf.getInt("numSensors");
  private static long averageTime = 0;
  private static int timesAveraged = 50;
  private static long timeAccumulated = 0;
  private byte[] buffer = new byte[2 * NUM_SENSORS];
  private int averageCounter = 0;

  public LineSensor4905() {
    super("lineFollowSensor4905");
    System.out.println(" - Creating 4905 Line Sensor! - ");
  }

  /**
   * This will return the average time for reading the I2C This will default to
   * averaging 50 reads, but can be changed
   * 
   * @return returns the averged time in miliseconds for reads
   */
  public long getAverageReadTime() {
    return averageTime;
  }

  /**
   * Allows you to changed how many samples are taken before averaging the amount
   * of time it takes to read the I2C
   * 
   * @param times the amount of samples we are averaging
   */
  public void setTimesAveraged(int times) {
    timesAveraged = times;
  }

  public int getTimesAverged() {
    return timesAveraged;
  }

  @Override
  public void getSensorReading(int[] readingBuf) {
    Instant startTime = Instant.now();
    i2c.readOnly(buffer, 2 * NUM_SENSORS);

    // This calculates the time it takes the to read the sensor
    // This is average for each second
    // This is mainly test code
    Instant endTime = Instant.now();
    Duration timeElapsed = Duration.between(startTime, endTime);
    timeAccumulated += timeElapsed.toMillis();
    if (averageCounter < timesAveraged) {
      // System.out.println(timeElapsed.toMillis());
      ++averageCounter;
    } else {
      averageTime = timeAccumulated / (timesAveraged + 1);
      timeAccumulated = 0;
      averageCounter = 0;
    }

    // This calulates the deconstructed bytes that come from the arduino
    for (int sensorNumber = 0; sensorNumber < NUM_SENSORS; ++sensorNumber) {
      int b = 0;
      for (int count = 0; count < 2; ++count) {
        int a = buffer[sensorNumber * 2 + count] & 0xFF;
        b = b | a;
        if (count != 1) {
          b = b << 8;
        }
      }
      // These should be negative, please don't change it if we work on this again.
      readingBuf[sensorNumber] = -b;
    }
  }
}
