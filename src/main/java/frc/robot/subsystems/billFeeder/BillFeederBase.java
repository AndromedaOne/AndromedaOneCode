package frc.robot.subsystems.billFeeder;

import frc.robot.subsystems.SubsystemInterface;

public interface BillFeederBase extends SubsystemInterface {

  public abstract void runBillFeederIntake();

  public abstract void runBillFeederEject();

  public abstract void runBillFeederShooting();

  public abstract void runBillFeederTrapShooting();

  public abstract void runBillFeederSlowEject();

  public abstract void stopBillFeeder();

  public abstract void setBrakeMode();

  public abstract void setCoastMode();

  public abstract boolean getNoteDetectorState();

}
