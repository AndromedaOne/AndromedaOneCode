// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannon;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.sensors.encoder.EncoderBase;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.subsystems.compressor.CompressorBase;

/** Add your docs here. */
public class RealCannon extends SubsystemBase implements CannonBase {
  private DoubleSolenoid4905 m_solenoid0_7;
  private DoubleSolenoid4905 m_solenoid1_6;
  private DoubleSolenoid4905 m_solenoid2_5;
  private DoubleSolenoid4905 m_solenoid3_4;
  private double m_maxsafetyRange;
  private Config m_config;
  private LimitSwitchSensor m_cannonElevatorContactSwitch;
  private EncoderBase m_canonRotateEncoder;
  private boolean m_cannonIsPressurized = false;

  public RealCannon(CompressorBase compressorBase) {
    m_config = Config4905.getConfig4905().getShowBotCannonConfig();
    m_solenoid0_7 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid0_7");
    m_solenoid1_6 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid1_6");
    m_solenoid2_5 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid2_5");
    m_solenoid3_4 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid3_4");
    m_maxsafetyRange = m_config.getInt("detectionrange");
    m_cannonElevatorContactSwitch = Robot.getInstance().getSensorsContainer().getCannonHomeSwitch();
    m_canonRotateEncoder = Robot.getInstance().getSensorsContainer().getCannonElevatorEncoder();
  }

  @Override
  public void pressurize() {
    m_solenoid0_7.extendPiston();
    m_solenoid1_6.extendPiston();
    m_solenoid2_5.extendPiston();
    m_solenoid3_4.extendPiston();
    m_cannonIsPressurized = true;
  }

// Returns true if the cannon actually shot, returns false if the cannon did not shoot

  @Override
  public boolean shoot() {
    if (Robot.getInstance().getSensorsContainer().getCannonSafetyUltrasonic()
        .getDistanceInches() >= m_maxsafetyRange) {
      System.out.println("Distance: " + Robot.getInstance().getSensorsContainer()
          .getCannonSafetyUltrasonic().getDistanceInches());
      m_solenoid0_7.retractPiston();
      m_solenoid1_6.retractPiston();
      m_solenoid2_5.retractPiston();
      m_solenoid3_4.retractPiston();
      m_cannonIsPressurized = false;
      return true;

    } else {
      System.out.println("Cannot shoot due to something being in the way. Distance being: " + Robot
          .getInstance().getSensorsContainer().getCannonSafetyUltrasonic().getDistanceInches());
      return false;
    }

  }

  @Override
  public boolean isPressurized() {
    return m_cannonIsPressurized;
  }

  @Override
  public boolean isCannonElevationInRange() {
    return !m_cannonElevatorContactSwitch.isAtLimit();
  }

  @Override
  public void reset() {
    m_solenoid0_7.stopPiston();
    m_solenoid1_6.stopPiston();
    m_solenoid2_5.stopPiston();
    m_solenoid3_4.stopPiston();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("cannonSafetyUltrasonic",
        Robot.getInstance().getSensorsContainer().getCannonSafetyUltrasonic().getDistanceInches());
    SmartDashboard.putBoolean("cannonElevatorContactSwitch",
        m_cannonElevatorContactSwitch.isAtLimit());
    SmartDashboard.putNumber("canon rotate encoder ticks", m_canonRotateEncoder.getEncoderValue());
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }
}