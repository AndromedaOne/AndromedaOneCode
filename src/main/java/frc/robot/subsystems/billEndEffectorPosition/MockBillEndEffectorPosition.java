package frc.robot.subsystems.billEndEffectorPosition;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockBillEndEffectorPosition implements BillEndEffectorPositionBase {
  @Override
  public void moveHighEndEffector() {
  }

  @Override
  public void moveLowEndEffector() {
  }

  public BillEndEffectorState getState() {
    return BillEndEffectorState.LOWSHOOTING;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }
}
