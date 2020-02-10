package frc.robot.sensors.camera;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class RealCamera extends Camera {
  public RealCamera(int port, int width, int height, int fps) {
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("Camera " + port, port);
    camera.setResolution(width, height);
    camera.setFPS(fps);
  }
}