package frc.robot.subsystems.billArmRotate;

import frc.robot.subsystems.SubsystemInterface;

public interface BillArmRotateBase extends SubsystemInterface {

  public abstract void rotate(double speed);

  public void stop();

  public abstract double getAngle();

  public abstract void engageArmBrake();

  public abstract void disengageArmBrake();

  public abstract BillArmBrakeState getState();

}
