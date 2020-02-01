package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Config4905;
import frc.robot.actuators.*;

public class RealShooter extends ShooterBase {

  private final static double SERIES_WHEEL_TICKS_TO_ROTATION = 0;
  private SparkMaxController m_shooterOne;
  private SparkMaxController m_shooterTwo;
  private TalonSRXController m_shooterSeries;
  private SpeedControllerGroup m_shooterGroup;
  private DoubleSolenoid4905 m_shooterHoodOne;
  private DoubleSolenoid4905 m_shooterHoodTwo;
  private boolean m_shooterIsReady = false;

  public RealShooter() {
    Config shooterConfig = Config4905.getConfig4905().getShooterConfig();

    m_shooterOne = new SparkMaxController(shooterConfig, "shooterone");
    m_shooterTwo = new SparkMaxController(shooterConfig, "shootertwo");
    m_shooterSeries = new TalonSRXController(shooterConfig, "shooterseries");
    m_shooterGroup = new SpeedControllerGroup(m_shooterOne, m_shooterTwo);
    m_shooterHoodOne = new DoubleSolenoid4905(shooterConfig, "hoodone");
    m_shooterHoodTwo = new DoubleSolenoid4905(shooterConfig, "hoodtwo");

    m_shooterSeries.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_1Ms);
  }

  @Override
  public void setShooterWheelPower(double power) {
    m_shooterGroup.set(power);
  }

  @Override
  public void setShooterSeriesPower(double power) {
    m_shooterSeries.set(power);
  }

  @Override
  public double getShooterSeriesVelocity() {
    //Converts to rpm
    return (m_shooterSeries.getEncoderVelocityTicks() / SERIES_WHEEL_TICKS_TO_ROTATION) * 1000;
  }

  @Override
  public double getShooterWheelVelocity() {
    double average = m_shooterOne.getEncoderVelocityTicks() + m_shooterTwo.getEncoderVelocityTicks();
    return average / 2;
  }

  @Override
  public double getShooterPower() {
    return m_shooterGroup.get();
  }

  @Override
  public double getSeriesPower() {
    return m_shooterSeries.get();
  }

  @Override
  public void openShooterHood() {
    m_shooterHoodOne.extendPiston();
    m_shooterHoodTwo.extendPiston();
  }

  @Override
  public void closeShooterHood() {
    m_shooterHoodOne.retractPiston();
    m_shooterHoodTwo.retractPiston();
  }

  @Override
  public boolean isShooterHoodOpen() {
    return m_shooterHoodOne.isSolenoidOpen();
  }

  @Override
  public void setPIDIsReady(boolean isReady) {
    m_shooterIsReady = isReady;
  }

  @Override
  public boolean shooterIsReady() {
    return m_shooterIsReady;
  }
}