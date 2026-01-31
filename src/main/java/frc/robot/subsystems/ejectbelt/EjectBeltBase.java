package frc.robot.subsystems.ejectbelt;

import frc.robot.subsystems.SubsystemInterface;

public interface EjectBeltBase extends SubsystemInterface {

  public void stop();

  public void intake();

  public void eject();
}
