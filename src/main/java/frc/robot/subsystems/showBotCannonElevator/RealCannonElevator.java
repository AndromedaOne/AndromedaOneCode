// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannonElevator;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.VictorSPXController;

public class RealCannonElevator extends SubsystemBase implements CannonElevatorBase {
  private VictorSPXController m_elevationMotor;
  private Config m_config;

  public RealCannonElevator() {
    m_config = Config4905.getConfig4905().getShowBotCannonElevatorConfig();
    m_elevationMotor = new VictorSPXController(m_config, "elevationMotor");
  }

  @Override
  public void changeElevation(double speed) {
    m_elevationMotor.set(speed);
  }

  @Override
  public void holdElevation() {
    m_elevationMotor.set(0);
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
