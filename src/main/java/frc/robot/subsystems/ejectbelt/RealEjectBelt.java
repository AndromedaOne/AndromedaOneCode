package frc.robot.subsystems.ejectbelt;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealEjectBelt extends SubsystemBase implements EjectBeltBase {
  private SparkMaxController m_eject;
  private Config m_ejectBeltConfig = Config4905.getConfig4905().getEjectBeltConfig();

  public RealEjectBelt() {
    m_eject = new SparkMaxController(m_ejectBeltConfig, "ejectBeltMotor", false, false);
  }

  @Override
  public void stop() {
    m_eject.setSpeed(0);
  }

  @Override
  public void intake() {
    m_eject.setSpeed(-0.5);
  }

  @Override
  public void eject() {
    m_eject.setSpeed(0.5);
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
