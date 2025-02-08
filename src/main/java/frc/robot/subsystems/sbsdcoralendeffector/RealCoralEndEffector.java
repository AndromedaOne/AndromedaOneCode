// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealCoralEndEffector extends SubsystemBase implements CoralEndEffectorBase {

  private SparkMaxController m_intakeMotor;
  private SparkMaxController m_angleMotor;
  private DoubleSupplier m_absoluteEncoderPosition;

  public RealCoralEndEffector() {
    Config config = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig();
    m_intakeMotor = new SparkMaxController(config, "coralDelivery", false, false);
    m_angleMotor = new SparkMaxController(config, "coralAngle", false, false);
    m_absoluteEncoderPosition = () -> m_angleMotor.getAbsoluteEncoderPosition();
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void eject() {
  }

  @Override
  public void intake() {
  }

  @Override
  public void stop() {
  }

  @Override
  public boolean hasCoral() {
    return false;
  }

  @Override
  public void setAngle(double angle) {
  }

}
