package frc.robot.subsystems.ejectbelt;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SparkMaxController;

public class RealEjectBelt extends SubsystemBase implements EjectBeltBase {
  private SparkMaxController m_eject;

  public RealEjectBelt() {
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
