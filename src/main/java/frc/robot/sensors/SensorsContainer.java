/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.sensors.camera.*;
import frc.robot.sensors.encoder.EncoderBase;
import frc.robot.sensors.encoder.MockEncoder;
import frc.robot.sensors.encoder.RealEncoder;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.sensors.gyro.MockGyro;
import frc.robot.sensors.gyro.RealNavXGyroSensor;
import frc.robot.sensors.gyro.RealPigeonGyroSensor;
import frc.robot.sensors.gyro.RomiGyro;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.sensors.limelightcamera.MockLimeLightCamera;
import frc.robot.sensors.limelightcamera.RealLimelightCamera;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.MockLimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;
import frc.robot.sensors.photonvision.MockPhotonVision;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.RealPhotonVision;
import frc.robot.sensors.ultrasonicsensor.MockUltrasonicSensor;
import frc.robot.sensors.ultrasonicsensor.RealUltrasonicSensor;
import frc.robot.sensors.ultrasonicsensor.UltrasonicSensor;
import frc.robot.telemetries.Trace;

/**
 * The Container that controls whether the sensors are real or mock. Uses the
 * config to do this.
 */
public class SensorsContainer {
  private Camera m_camera0;
  private Camera m_camera1;
  private LimeLightCameraBase m_limelightCameraBase;
  private Gyro4905 m_gyro;
  private UltrasonicSensor m_cannonSafetyUltrasonic;
  private EncoderBase m_cannonElevatorEncoder;
  private LimitSwitchSensor m_cannonHomeSwitch;
  private PhotonVisionBase m_photonVision;
  private boolean m_hasPhotonVision = false;
  private Config m_sensorConfig;

  public SensorsContainer() {
    m_sensorConfig = Config4905.getConfig4905().getSensorConfig();

    if (m_sensorConfig.hasPath("navx")) {
      Trace.getInstance().logInfo("Using real NavX Gyro sensor");
      m_gyro = new RealNavXGyroSensor();
    } else if (m_sensorConfig.hasPath("pigeon")) {
      Trace.getInstance().logInfo("Using real Pigeon Gyro sensor");
      m_gyro = new RealPigeonGyroSensor();
    } else if (m_sensorConfig.hasPath("RomiGyro")) {
      Trace.getInstance().logInfo("Using RomiGyro");
      m_gyro = new RomiGyro();
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

    if (m_sensorConfig.hasPath("limelight")) {
      Trace.getInstance().logInfo("Using real LimeLight");
      m_limelightCameraBase = new RealLimelightCamera();
    } else {
      Trace.getInstance().logInfo("Using fake LimeLight");
      m_limelightCameraBase = new MockLimeLightCamera();
    }
    if (m_sensorConfig.hasPath("photonvision")) {
      Trace.getInstance().logInfo("Using real Photon Vision");
      m_photonVision = new RealPhotonVision(m_sensorConfig.getString("photonvision.cameraName1"));
      m_hasPhotonVision = true;
    } else {
      Trace.getInstance().logInfo("Using mock Photon Vision");
      m_photonVision = new MockPhotonVision();
    }
    if (m_sensorConfig.hasPath("sensors.cannonSafetyUltrasonic")) {
      m_cannonSafetyUltrasonic = new RealUltrasonicSensor("cannonSafetyUltrasonic");
      Trace.getInstance().logInfo("Using Real Cannon Safety Ultrasonic");
    } else {
      m_cannonSafetyUltrasonic = new MockUltrasonicSensor();
    }
    if (m_sensorConfig.hasPath("sensors.cannonElevationEncoder")) {
      Trace.getInstance().logInfo("Using real cannon elevator encoder");
      m_cannonElevatorEncoder = new RealEncoder("cannonElevationEncoder");
    } else {
      Trace.getInstance().logInfo("Using mock cannon elevator encoder");
      m_cannonElevatorEncoder = new MockEncoder();
    }
    if (m_sensorConfig.hasPath("sensors.cannonHomeSwitch")) {
      Trace.getInstance().logInfo("Using real cannon home switch");
      m_cannonHomeSwitch = new RealLimitSwitchSensor("cannonHomeSwitch");
    } else {
      Trace.getInstance().logInfo("Using mock cannon home switch");
      m_cannonHomeSwitch = new MockLimitSwitchSensor();
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

  public boolean hasLimeLight() {
    return false;
  }

  public boolean hasPhotonVision() {
    return m_hasPhotonVision;
  }

  public LimeLightCameraBase getLimeLight() {
    return m_limelightCameraBase;
  }

  public UltrasonicSensor getCannonSafetyUltrasonic() {
    return m_cannonSafetyUltrasonic;
  }

  public EncoderBase getCannonElevatorEncoder() {
    return m_cannonElevatorEncoder;
  }

  public LimitSwitchSensor getCannonHomeSwitch() {
    return m_cannonHomeSwitch;
  }

  public PhotonVisionBase getPhotonVision() {
    return m_photonVision;
  }

  public void periodic() {
    RealSensorBase.periodic();
  }
}
