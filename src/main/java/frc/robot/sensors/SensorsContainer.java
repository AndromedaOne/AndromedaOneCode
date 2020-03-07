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
import frc.robot.sensors.gyro.MockNavXGyroSensor;
import frc.robot.sensors.gyro.NavXGyroSensor;
import frc.robot.sensors.gyro.RealNavXGyroSensor;
import frc.robot.sensors.colorsensor.ColorSensor;
import frc.robot.sensors.colorsensor.MockColorSensor;
import frc.robot.sensors.colorsensor.RealColorSensor;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.sensors.limelightcamera.MockLimeLightCamera;
import frc.robot.sensors.limelightcamera.RealLimelightCamera;

/**
 * The Container that controls whether the sensors are real or mock. Uses the
 * config to do this.
 */
public class SensorsContainer {
  private BallFeederSensorBase m_ballFeederSensor;
  private Camera m_camera0;
  private Camera m_camera1;
  private LimeLightCameraBase m_limelightCameraBase;
  private NavXGyroSensor m_gyro;
  private ColorSensor m_colorSensor;

  public SensorsContainer() {
    final Config sensorConfig = Config4905.getConfig4905().getSensorConfig();

    if (sensorConfig.hasPath("navx")) {
      System.out.println("Using real NavX Gyro sensor");
      m_gyro = new RealNavXGyroSensor();
    } else {
      System.out.println("Using mock Navx Gyro sensor");
      m_gyro = new MockNavXGyroSensor();
    }

    if (sensorConfig.hasPath("sensors.ballFeederSensor")) {
      System.out.println("Using real ball feeder sensor");
      m_ballFeederSensor = new RealBallFeederSensor("ballFeederSensor");
    } else {
      System.out.println("Using mock ball feeder sensor");
      m_ballFeederSensor = new MockBallFeederSensor();
    }

    if (sensorConfig.hasPath("sensors.cameras")) {
      if (sensorConfig.hasPath("sensors.cameras.camera0")) {
        System.out.println("Using real camera with id: " + sensorConfig.getInt("sensors.cameras.camera0.port"));
        m_camera0 = new RealCamera(0, sensorConfig.getInt("sensors.cameras.camera0.port"));
      }
      if (sensorConfig.hasPath("sensors.cameras.camera1")) {
        System.out.println("Using real camera with id: " + sensorConfig.getInt("sensors.cameras.camera1.port"));
        m_camera1 = new RealCamera(1, sensorConfig.getInt("sensors.cameras.camera1.port"));
      }
    } else {
      System.out.println("Using fake cameras");
      m_camera0 = new MockCamera();
    }

    if (sensorConfig.hasPath("limelight")) {
      System.out.println("Using real LimeLight");
      m_limelightCameraBase = new RealLimelightCamera();
    } else {
      System.out.println("Using fake LimeLight");
      m_limelightCameraBase = new MockLimeLightCamera();
    }

    if (sensorConfig.hasPath("sensors.colorSensor")) {
      System.out.println("Using real colorSensor");
      m_colorSensor = new RealColorSensor();
    } else {
      System.out.println("Using fakecolorSensor");
      m_colorSensor = new MockColorSensor();
    }
  }

  public NavXGyroSensor getNavXGyro() {
    return m_gyro;
  }

  public BallFeederSensorBase getBallFeederSensor() {
    return m_ballFeederSensor;
  }

  public Camera getCamera0() {
    return m_camera0;
  }

  public Camera getCamera1() {
    return m_camera1;
  }

  public LimeLightCameraBase getLimeLight() {
    return m_limelightCameraBase;
  }

  public ColorSensor getColorSensor() {
    return m_colorSensor;
  }
}
