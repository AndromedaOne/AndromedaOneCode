/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import frc.robot.Config4905;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.sensors.ballfeedersensor.MockBallFeederSensor;
import frc.robot.sensors.ballfeedersensor.RealBallFeederSensor;

/**
 * The Container that controls whether the sensors are real or mock. Uses the
 * config to do this.
 */
public class SensorsContainer {
  // TODO: Please add the sensors and (important)ADD JAVADOCS FOR EVERYTHING kthx
  public BallFeederSensorBase ballFeederSensor;

  public SensorsContainer() {
    if (Config4905.getConfig4905().getSensorConfig().hasPath("ballFeederSensor")) {
      System.out.println("Using real ball feeder sensor");
      ballFeederSensor = new RealBallFeederSensor("ballFeederSensor");
    } else {
      System.out.println("Using mock ball feeder sensor");
      ballFeederSensor = new MockBallFeederSensor();
    }
  }
}
