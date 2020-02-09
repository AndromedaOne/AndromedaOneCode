package frc.robot.sensors.camera;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class RealCamera extends Camera {
    public RealCamera(int port) {
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("Camera " + port, port);
        camera.setResolution(320, 240);
        camera.setFPS(10);
    }
}