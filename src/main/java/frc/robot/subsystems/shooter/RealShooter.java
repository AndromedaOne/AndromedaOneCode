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

    m_shooterTwo = new SparkMaxController(shooterConfig, "shootertwo")

    m_shooterGroup = new SpeedControllerGroup(m_shooterOne, m_shooterTwo);

  }

  @Override
  public double getShooterVelocity() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setShooterPower(double power) {
    // TODO Auto-generated method stub

  }

  @Override
  public double getShooterPower() {
    // TODO Auto-generated method stub
    return 0;
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