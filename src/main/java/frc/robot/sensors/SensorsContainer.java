/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import java.util.ArrayList;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.sensors.camera.*;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;
import frc.robot.sensors.distanceSensor.pwfTofDistanceSensor.MockpwfTofDistanceSensor;
import frc.robot.sensors.distanceSensor.pwfTofDistanceSensor.RealPwfTofDistanceSensor;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.sensors.gyro.MockGyro;
import frc.robot.sensors.gyro.RealNavXGyroSensor;
import frc.robot.sensors.gyro.RealPigeonGyroSensor;
import frc.robot.sensors.photonvision.MockPhotonVision;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.RealPhotonVision;
import frc.robot.telemetries.Trace;

/**
 * The Container that controls whether the sensors are real or mock. Uses the
 * config to do this.
 */
public class SensorsContainer {
  private Camera m_camera0;
  private Camera m_camera1;
  private Gyro4905 m_gyro;
  private ArrayList<PhotonVisionBase> m_photonVision = new ArrayList<PhotonVisionBase>();
  private boolean m_hasPhotonVision = false;
  private DistanceSensorBase m_tof0;
  private DistanceSensorBase m_tof1;
  private DistanceSensorBase m_tof2;
  private Config m_sensorConfig;

  public SensorsContainer() {
    m_sensorConfig = Config4905.getConfig4905().getSensorConfig();

    if (m_sensorConfig.hasPath("navx")) {
      Trace.getInstance().logInfo("Using real NavX Gyro sensor");
      m_gyro = new RealNavXGyroSensor();
    } else if (m_sensorConfig.hasPath("pigeon")) {
      Trace.getInstance().logInfo("Using real Pigeon Gyro sensor");
      m_gyro = new RealPigeonGyroSensor();
    } else {
      Trace.getInstance().logInfo("Using mock Navx Gyro sensor");
      m_gyro = new MockGyro();
    }

    if (m_sensorConfig.hasPath("sensors.cameras")) {
      if (m_sensorConfig.hasPath("sensors.cameras.camera0")) {
        Trace.getInstance().logInfo(
            "Using real camera with id: " + m_sensorConfig.getInt("sensors.cameras.camera0.port"));
        m_camera0 = new RealCamera(0, m_sensorConfig.getInt("sensors.cameras.camera0.port"));
      }
      if (m_sensorConfig.hasPath("sensors.cameras.camera1")) {
        Trace.getInstance().logInfo(
            "Using real camera with id: " + m_sensorConfig.getInt("sensors.cameras.camera1.port"));
        m_camera1 = new RealCamera(1, m_sensorConfig.getInt("sensors.cameras.camera1.port"));
      }
    } else {
      Trace.getInstance().logInfo("Using fake cameras");
      m_camera0 = new MockCamera();
      m_camera1 = new MockCamera();
    }
    if (m_sensorConfig.hasPath("photonvision")) {
      Trace.getInstance().logInfo("Using real Photon Vision");
      for (int i = 1; i <= m_sensorConfig.getInt("photonvision.numberOfCameras"); i++) {
        String cameraName = m_sensorConfig.getString("photonvision.cameraName" + i);
        m_photonVision.add(new RealPhotonVision(cameraName));
        Trace.getInstance().logInfo("added camera: " + cameraName);
      }
      m_hasPhotonVision = true;
    } else {
      Trace.getInstance().logInfo("Using mock Photon Vision");
      m_photonVision.add(new MockPhotonVision());
    }
    if (m_sensorConfig.hasPath("sensors.tof0")) {
      Trace.getInstance().logInfo("Using real tof sensor 0");
      m_tof0 = new RealPwfTofDistanceSensor("tof0");
    } else {
      Trace.getInstance().logInfo("Using mock tof sensor 0");
      m_tof0 = new MockpwfTofDistanceSensor();
    }
    if (m_sensorConfig.hasPath("sensors.tof1")) {
      Trace.getInstance().logInfo("Using real tof sensor 1");
      m_tof1 = new RealPwfTofDistanceSensor("tof1");
    } else {
      Trace.getInstance().logInfo("Using mock tof sensor 1");
      m_tof1 = new MockpwfTofDistanceSensor();
    }
    if (m_sensorConfig.hasPath("sensors.tof2")) {
      Trace.getInstance().logInfo("Using real tof sensor 2");
      m_tof2 = new RealPwfTofDistanceSensor("tof2");
    } else {
      Trace.getInstance().logInfo("Using mock tof sensor 2");
      m_tof2 = new MockpwfTofDistanceSensor();
    }
  }

  public Gyro4905 getGyro() {
    return m_gyro;
  }

  public Camera getCamera0() {
    return m_camera0;
  }

  public Camera getCamera1() {
    return m_camera1;
  }

  public boolean hasPhotonVision() {
    return m_hasPhotonVision;
  }

  public ArrayList<PhotonVisionBase> getPhotonVisionList() {
    return m_photonVision;
  }

  public DistanceSensorBase getTof0() {
    return m_tof0;
  }

  public DistanceSensorBase getTof1() {
    return m_tof1;
  }

  public DistanceSensorBase getTof2() {
    return m_tof2;
  }

  public void periodic() {
    RealSensorBase.periodic();
  }
}
