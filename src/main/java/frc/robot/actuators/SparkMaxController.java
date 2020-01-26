package frc.robot.actuators;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.Config4905;

public class SparkMaxController implements SpeedController {
  private CANSparkMax m_sparkMaxController;
  private CANEncoder m_sparkMaxEncoder;
  private int ticks_per_inch = Config4905.getConfig4905().getDrivetrainConfig().getInt("drivetrain.ticks_per_inch");

  public SparkMaxController(String configString) {
    m_sparkMaxController = new CANSparkMax(
        Config4905.getConfig4905().getDrivetrainConfig().getInt("ports." + configString), MotorType.kBrushless);
    m_sparkMaxEncoder = new CANEncoder(m_sparkMaxController);
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

  public double getEncoderPositionTicks() {
    return m_sparkMaxEncoder.getPosition();
  }

  public double getEncoderVelocityTicks() {
    return m_sparkMaxEncoder.getVelocity();
  }

  public double getEncoderPositionInches() {
    return m_sparkMaxEncoder.getPosition() / ticks_per_inch;
  }

  public double getEncoderVelocityInches() {
    return m_sparkMaxEncoder.getVelocity() / ticks_per_inch;
  }
}
