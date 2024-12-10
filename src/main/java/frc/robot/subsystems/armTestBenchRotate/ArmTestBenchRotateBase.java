package frc.robot.subsystems.armTestBenchRotate;

import frc.robot.subsystems.SubsystemInterface;

public interface ArmTestBenchRotateBase extends SubsystemInterface {

  public abstract void rotate(double speed);

  public void stop();

  public abstract double getAngle();

  public abstract void engageArmBrake();

  public abstract void disengageArmBrake();

  public abstract ArmTestBenchRotateBrakeState getBrakeState();

  public abstract void enableMotorBrake();

  public abstract void disableMotorBrake();

}
