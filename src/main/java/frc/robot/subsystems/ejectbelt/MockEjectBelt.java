package frc.robot.subsystems.ejectbelt;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
  public void ejectLeft() {
  }

  @Override
  public void ejectRight() {
  }

}
