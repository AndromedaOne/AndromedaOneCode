package frc.robot.subsystems.billFeeder;

import frc.robot.subsystems.SubsystemInterface;

public interface BillFeederBase extends SubsystemInterface {

  public abstract void runBillFeeder(double speed);

  public abstract void stopBillFeeder();

  public abstract void runBillFeederInReverse(double speed);
}
