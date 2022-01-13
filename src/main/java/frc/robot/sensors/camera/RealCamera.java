package frc.robot.sensors.camera;

import com.typesafe.config.Config;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import frc.robot.Config4905;

public class RealCamera extends Camera {
  public RealCamera(int cameraNum, int port) {
    Config sensorConfig = Config4905.getConfig4905().getSensorConfig();
    UsbCamera camera = CameraServer.startAutomaticCapture("Camera " + cameraNum, port);
    int width = sensorConfig.getInt("sensors.cameras.camera" + cameraNum + ".width");
    int height = sensorConfig.getInt("sensors.cameras.camera" + cameraNum + ".height");
    int fps = sensorConfig.getInt("sensors.cameras.camera" + cameraNum + ".fps");
    camera.setResolution(width, height);
    camera.setFPS(fps);
  }
}