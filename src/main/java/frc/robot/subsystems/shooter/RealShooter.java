package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.*;

public class RealShooter extends ShooterBase {

  private Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
  private final double SERIES_TICKS_TO_ROTATION = m_shooterConfig.getDouble("seriesticksperrotation");
  private SparkMaxController m_shooterOne;
  private SparkMaxController m_shooterTwo;
  private TalonSRXController m_shooterSeries;
  private SpeedControllerGroup m_shooterGroup;
  private DoubleSolenoid4905 m_shooterHood;
  private boolean m_shooterWheelIsReady = false;
  private boolean m_shooterSeriesIsReady = false;
  private boolean m_shooterIsIdle = false;

  public RealShooter() {
    m_shooterOne = new SparkMaxController(m_shooterConfig, "shooterone");
    m_shooterTwo = new SparkMaxController(m_shooterConfig, "shootertwo");
    m_shooterSeries = new TalonSRXController(m_shooterConfig, "shooterseries");
    m_shooterGroup = new SpeedControllerGroup(m_shooterOne, m_shooterTwo);
    m_shooterHood = new DoubleSolenoid4905(m_shooterConfig, "hood");

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
    // Converts to rpm
    return (m_shooterSeries.getEncoderVelocityTicks() / SERIES_TICKS_TO_ROTATION) * 1000;
  }

  @Override
  public double getShooterWheelVelocity() {
    double average = m_shooterOne.getEncoderVelocityTicks() + m_shooterTwo.getEncoderVelocityTicks();
    average = average / 2;
    SmartDashboard.putNumber("ShooterOne Velocity", m_shooterOne.getEncoderVelocityTicks());
    SmartDashboard.putNumber("ShooterTwo Velocity", m_shooterTwo.getEncoderVelocityTicks());
    return average;
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
    System.out.println("Opening Shooter Hood");
    m_shooterHood.extendPiston();
  }

  @Override
  public void closeShooterHood() {
    System.out.println("Closing Shooter Hood");
    m_shooterHood.retractPiston();
  }

  @Override
  public boolean isShooterHoodOpen() {
    return m_shooterHood.isSolenoidOpen();
  }

  @Override
  public void setShooterPIDIsReady(boolean isReady) {
    m_shooterWheelIsReady = isReady;
  }

  @Override
  public void setSeriesPIDIsReady(boolean isReady) {
    m_shooterSeriesIsReady = isReady;
  }

  @Override
  public void setShooterIsIdle(boolean isIdle) {
    m_shooterIsIdle = isIdle;
  }

  @Override
  public boolean shooterIsReady() {
    // This does not include the series wheel ready bc the series wheel currently
    // Does not have an encoder
    return m_shooterWheelIsReady && !m_shooterIsIdle;
  }

}