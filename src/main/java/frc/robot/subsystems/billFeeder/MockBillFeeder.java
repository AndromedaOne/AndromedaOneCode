package frc.robot.subsystems.billFeeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockBillFeeder implements BillFeederBase {
  @Override
  public void runBillFeederIntake() {
  }

  @Override
  public void stopBillFeeder() {
  }

  @Override
  public void runBillFeederEject() {
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

  @Override
  public void runBillFeederShooting() {
  }

  @Override
  public void runBillFeederTrapShooting() {
  }

  @Override
  public void runBillFeederSlowEject() {

  }

  @Override
  public void setBrakeMode() {

  }

  @Override
  public void setCoastMode() {

  }

}
