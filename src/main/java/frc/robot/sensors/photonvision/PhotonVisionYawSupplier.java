// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Add your docs here. */
public class PhotonVisionYawSupplier {
    private IntSupplier m_wantedID = () -> -1;
    private DoubleSupplier m_setpoint = () -> 0;
    private double m_counter = 0;
    private double m_previousYaw = 0;
    private PhotonVisionBase m_photonVision;
    private final double m_consecutiveFramesWithoutTarget = 16;
  
    

    public PhotonVisionYawSupplier(IntSupplier wantedID, DoubleSupplier setpoint,
      PhotonVisionBase photonVision) {
      m_wantedID = wantedID;
      m_setpoint = setpoint;
      m_photonVision = photonVision;
      SmartDashboard.putBoolean("lost target", true);
    }

    public double getAsDouble() {
      TargetDetectedAndAngle detectedAnAngle = m_photonVision.getTargetDetectedAndAngle(m_wantedID.getAsInt(), 
        m_setpoint.getAsDouble());
      if (detectedAnAngle.getDetected()) {
        m_counter = 0;
        m_previousYaw = detectedAnAngle.getAngle();
        return m_previousYaw;
      }
      m_counter++;
      if (m_counter >= m_consecutiveFramesWithoutTarget) {
        SmartDashboard.putBoolean("lost target", true);
        return m_setpoint.getAsDouble();
      }
      return m_previousYaw;
    }
  }
