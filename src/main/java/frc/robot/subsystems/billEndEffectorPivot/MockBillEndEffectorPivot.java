package frc.robot.subsystems.billEndEffectorPivot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MockBillEndEffectorPivot implements BillEndEffectorPivotBase {
  @Override
  public void initialize() {

  }

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
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(Command command) {
  }
}
