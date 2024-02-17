package frc.robot.subsystems.billShooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealBillShooter extends SubsystemBase implements BillShooterBase {

  private Config m_shooterConfig = Config4905.getConfig4905().getBillShooterConfig();
  private SparkMaxController m_shooterMotor;

  public RealBillShooter() {
    m_shooterMotor = new SparkMaxController(m_shooterConfig, "shooterMotor");
  }

// positive speed shoots the note
  @Override
  public void setShooterWheelPower(double power) {
    m_shooterMotor.setSpeed(power);
    SmartDashboard.putNumber("ShooterWheelPower", power);

  }

  @Override
  public double getShooterWheelPower() {
    return m_shooterMotor.getSpeed();
  }

  @Override
  public double getShooterWheelRpm() {
    double speed = m_shooterMotor.getBuiltInEncoderVelocityTicks();
    SmartDashboard.putNumber("ShooterSpeed", speed);
    return speed;
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
