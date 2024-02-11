package frc.robot.subsystems.billFeeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockBillFeeder implements BillFeederBase {
  @Override
  public void runBillFeeder(double speed) {
  }

  @Override
  public void stopBillFeeder() {
  }

  @Override
  public void runBillFeederInReverse(double speed) {
  }

  @Override
  public boolean getNoteDetectorState() {
    return false;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

}
