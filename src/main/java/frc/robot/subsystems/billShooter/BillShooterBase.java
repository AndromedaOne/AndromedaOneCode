package frc.robot.subsystems.billShooter;

import frc.robot.subsystems.SubsystemInterface;

public interface BillShooterBase extends SubsystemInterface {

  public abstract void setShooterWheelPower(double power);

  public abstract double getShooterWheelPower();

  public abstract double getShooterWheelRpm();

}