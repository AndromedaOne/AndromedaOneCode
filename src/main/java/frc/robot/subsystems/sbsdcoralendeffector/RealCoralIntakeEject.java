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
  private boolean m_hasCoral = false;
  private double m_intakeSpeed = 0.1;

  public RealCoralIntakeEject() {
    Config config = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig();
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
    super.setDefaultCommand(command);
  }

  @Override
  public void runWheels(double speed) {
    m_intakeMotor.setSpeed(speed);
  }

  @Override
  public void runWheelsIntake() {
    /*
     * given that the coral only goes one way through the end effector (intake pulls
     * it in through the intake side, eject scores out the other end), a positive
     * speed will send the coral through the end effector. there should be no reason
     * to go in the opposite direction. also, the speed of the wheels will be
     * determined by experiment, a command will not tell it the speed, the speed
     * will depend upon where the coral is within the end effector and whether it is
     * intaking or scoring or holding. first thimg is to detect that a coral has
     * arrived at the intake and pull it in to the end effector.
     */
    double speed = 0;
    if (intakeDetector() && !ejectDetector()) {
      // a coral has been detected on the intake side but nothing is on the eject
      // side. so pull the coral into the end effector
      speed = m_intakeSpeed;
    } else if (intakeDetector() && ejectDetector()) {
      /*
       * there's a coral in the end effector but it is detected on both the intake and
       * eject detector, we will need to push the coral out of the end effector until
       * only the eject side detector is true. this must be done to get the coral out
       * of the way so the arm can move to a scoring position.
       */
      stop(); // TODO: right now just stop, will need further logic to pull the coral into the
      // end effector
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
