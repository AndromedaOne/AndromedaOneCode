package frc.robot.subsystems.ejectbelt;

import org.wpilib.command2.Command;
import org.wpilib.command2.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockEjectBelt implements EjectBeltBase {
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  public void setDefaultCommand(Command command) {

  }

  @Override
  public void stop() {
  }

  @Override
  public void feedShooter() {
  }

  @Override
  public void ejectRight() {
  }

}
