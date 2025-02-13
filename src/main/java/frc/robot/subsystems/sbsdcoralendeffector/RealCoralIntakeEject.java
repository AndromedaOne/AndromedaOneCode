// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;

/** Add your docs here. */
public class RealCoralIntakeEject extends SubsystemBase implements CoralIntakeEjectBase {

  private SparkMaxController m_intakeMotor;
  private LimitSwitchSensor m_intakeSideSensor;
  private LimitSwitchSensor m_ejectSideSensor;

  public RealCoralIntakeEject() {
    Config config = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig();
    Config sensorConfig = Config4905.getConfig4905().getSensorConfig();
    m_intakeMotor = new SparkMaxController(config, "coralDelivery", false, false);
    m_intakeSideSensor = new RealLimitSwitchSensor("endEffectorIntakeSensor");
    m_ejectSideSensor = new RealLimitSwitchSensor("endEffectorEjectSensor");

  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void runWheels(double speed) {
    m_intakeMotor.setSpeed(speed);
  }

  @Override
  public void runWheelsIntake(double speed) {
    // not sure whether these are the correct speed polarities
    if (intakeDetector() && speed > 0) {
      stop();
    } else if (ejectDetector() && speed < 0) {
      stop();
    } else {
      m_intakeMotor.setSpeed(speed);
    }
  }

  @Override
  public void stop() {
    m_intakeMotor.setSpeed(0);
  }

  @Override
  public boolean intakeDetector() {
    return m_intakeSideSensor.isAtLimit();

  }

  @Override
  public boolean ejectDetector() {
    return m_ejectSideSensor.isAtLimit();
  }

  @Override
  public void setCoastMode() {
    m_intakeMotor.setCoastMode();
  }

  @Override
  public void setBrakeMode() {
    m_intakeMotor.setBrakeMode();
  }

}
