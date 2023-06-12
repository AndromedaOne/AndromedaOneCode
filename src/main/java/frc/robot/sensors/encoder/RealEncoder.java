// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.encoder;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

/** Add your docs here. */
public class RealEncoder extends RealSensorBase implements EncoderBase {

  private Encoder m_encoder;
  private String m_name;

  public RealEncoder(String configString) {
    m_encoder = new Encoder(
        Config4905.getConfig4905().getSensorConfig()
            .getInt("sensors." + configString + ".aChannel"),
        Config4905.getConfig4905().getSensorConfig()
            .getInt("sensors." + configString + ".bChannel"));
    m_name = configString;
  }

  public void resetEncoder() {
    m_encoder.reset();
  }

  public double getEncoderValue() {
    return m_encoder.getDistance();
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber(m_name, getEncoderValue());
  }
}
