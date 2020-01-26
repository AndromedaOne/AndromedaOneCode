package frc.robot.actuators;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedController;

public class SparkMaxController implements SpeedController {
  private CANSparkMax m_sparkMaxController;

  public SparkMaxController(String configString) {
    m_sparkMaxController = new CANSparkMax(0, MotorType.kBrushless);
    configure();
  }

  private void configure() {
    m_sparkMaxController.restoreFactoryDefaults();
  }

  @Override
  public void pidWrite(double output) {
    m_sparkMaxController.pidWrite(output);
  }

  @Override
  public void set(double speed) {
    m_sparkMaxController.set(speed);
  }

  @Override
  public double get() {
    return m_sparkMaxController.get();
  }

  @Override
  public void setInverted(boolean isInverted) {
    m_sparkMaxController.setInverted(isInverted);
  }

  @Override
  public boolean getInverted() {
    return m_sparkMaxController.getInverted();
  }

  @Override
  public void disable() {
    m_sparkMaxController.disable();
  }

  @Override
  public void stopMotor() {
    m_sparkMaxController.stopMotor();
  }
}