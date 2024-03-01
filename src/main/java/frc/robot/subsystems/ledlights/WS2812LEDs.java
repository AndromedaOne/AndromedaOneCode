// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ledlights;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

/** Add your docs here. */
public class WS2812LEDs extends RealLEDs {
  private AddressableLED m_led;
  private AddressableLEDBuffer m_ledBuffer;
  private boolean m_haveNote = false;

  public WS2812LEDs(Config ws2812Config, DriveTrainBase driveTrain) {
    super(driveTrain);
    m_led = new AddressableLED(ws2812Config.getInt("PWMport"));
    m_ledBuffer = new AddressableLEDBuffer(ws2812Config.getInt("numbLEDs"));
    m_led.setLength(m_ledBuffer.getLength());
    updateRGBcolor(new Color(1, 1, 1));
    m_led.start();
  }

  @Override
  protected void updateRGBcolor(Color color) {
    for (int i = 0; i < m_ledBuffer.getLength(); ++i) {
      m_ledBuffer.setLED(i, color);
    }
    m_led.setData(m_ledBuffer);
  }
  //true indicates robot has a note, otherwise it is false. 
  public void setNoteState (boolean state){
   m_haveNote = state;
  }
  public boolean getNoteState(){
    return m_haveNote; 
  }
}
