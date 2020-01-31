package frc.robot.subsystems.shooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.actuators.*;

import frc.robot.Config4905;

public class RealShooter extends ShooterBase {

  private SparkMaxController m_shooterOne;
  private SparkMaxController m_shooterTwo;
  private SpeedControllerGroup m_shooterGroup;
   // TODO Add Talon And pneumatic Controllers

  public RealShooter(){
    Config shooterConfig = Config4905.getConfig4905().getShooterConfig();

    m_shooterOne = new SparkMaxController(shooterConfig, "shooterone");

    m_shooterTwo = new SparkMaxController(shooterConfig, "shootertwo");

    m_shooterGroup = new SpeedControllerGroup(m_shooterOne, m_shooterTwo);

  }

  @Override
  public double getShooterVelocity() {
    // Averaging Both Motor Controllers
    double average = m_shooterOne.getEncoderVelocityTicks() + m_shooterTwo.getEncoderVelocityTicks();
    return average / 2;
  }

  @Override
  public void setShooterPower(double power) {
    m_shooterGroup.set(power);
  }

  @Override
  public double getShooterPower() {
    return m_shooterGroup.get();
  }

  @Override
  public void openShooterHood() {
    // TODO Auto-generated method stub

  }

  @Override
  public void closeShooterHood() {
    // TODO Auto-generated method stub

  }

}