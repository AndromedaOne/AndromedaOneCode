// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannon;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.subsystems.compressor.CompressorBase;

/** Add your docs here. */
public class RealCannon extends CannonBase {
  private DoubleSolenoid4905 m_solenoid0_7;
  private DoubleSolenoid4905 m_solenoid1_6;
  private DoubleSolenoid4905 m_solenoid2_5;
  private DoubleSolenoid4905 m_solenoid3_4;
  private double m_maxsafetyRange;
  private Config m_config;
  // contact switch true is out of range; false is in range
  private DigitalInput m_cannonElevatorContactSwitch;

  public RealCannon(CompressorBase compressorBase) {
    m_config = Config4905.getConfig4905().getShowBotCannonConfig();
    m_solenoid0_7 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid0_7");
    m_solenoid1_6 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid1_6");
    m_solenoid2_5 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid2_5");
    m_solenoid3_4 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid3_4");
    m_maxsafetyRange = m_config.getInt("detectionrange");
    m_cannonElevatorContactSwitch = new DigitalInput(m_config.getInt("contactswitch"));
  }

  @Override
  public void pressurize() {
    m_solenoid0_7.extendPiston();
    m_solenoid1_6.extendPiston();
    m_solenoid2_5.extendPiston();
    m_solenoid3_4.extendPiston();
  }

  @Override
  public void shoot() {
    if (Robot.getInstance().getSensorsContainer().getCannonSafetyUltrasonic()
        .getDistanceInches() >= m_maxsafetyRange) {
      System.out.println("Distance: " + Robot.getInstance().getSensorsContainer()
          .getCannonSafetyUltrasonic().getDistanceInches());
      m_solenoid0_7.retractPiston();
      m_solenoid1_6.retractPiston();
      m_solenoid2_5.retractPiston();
      m_solenoid3_4.retractPiston();

    } else {

      System.out.println("Cannot shoot due to something being in the way. Distance being: " + Robot
          .getInstance().getSensorsContainer().getCannonSafetyUltrasonic().getDistanceInches());
    }

  }

  @Override
  public boolean isPressurized() {

    return false;
  }

  public void periodic() {
    SmartDashboard.putNumber("cannonSafetyUltrasonic",
        Robot.getInstance().getSensorsContainer().getCannonSafetyUltrasonic().getDistanceInches());
    SmartDashboard.putBoolean("cannonElevatorContactSwitch", m_cannonElevatorContactSwitch.get());
  }

  @Override
  public boolean isCannonElevationInRange() {
    return !m_cannonElevatorContactSwitch.get();
  }

  @Override
  public void reset() {
    m_solenoid0_7.stopPiston();
    m_solenoid1_6.stopPiston();
    m_solenoid2_5.stopPiston();
    m_solenoid3_4.stopPiston();
  }
}