/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.sensors.ballfeedersensor.MockBallFeederSensor;
import frc.robot.sensors.ballfeedersensor.RealBallFeederSensor;
import frc.robot.sensors.camera.*;

/**
 * The Container that controls whether the sensors are real or mock. Uses the
 * config to do this.
 */
public class SensorsContainer {
  // TODO: Please add the sensors and (important)ADD JAVADOCS FOR EVERYTHING kthx
  private BallFeederSensorBase ballFeederSensor;
  private Camera camera0;

  public SensorsContainer() {
    final Config sensorConfig = Config4905.getConfig4905().getSensorConfig();
    if (sensorConfig.hasPath("ballFeederSensor")) {
      System.out.println("Using real ball feeder sensor");
      ballFeederSensor = new RealBallFeederSensor("ballFeederSensor");
    } else {
      System.out.println("Using mock ball feeder sensor");
      ballFeederSensor = new MockBallFeederSensor();
    }

    if (sensorConfig.hasPath("cameras")) {
      if (sensorConfig.hasPath("cameras.camera0")) {
        System.out.println("Using real camera with id: " + sensorConfig.getInt("cameras.camera0.port"));
        camera0 = new RealCamera(0, sensorConfig.getInt("sensors.cameras.camera0.port"));
      }
    } else {
      System.out.println("Using fake cameras");
      camera0 = new MockCamera();
    }
  }

  public BallFeederSensorBase getBallFeederSensor() {
    return ballFeederSensor;
  }

  public Camera getCamera0() {
    return camera0;
  }
}
