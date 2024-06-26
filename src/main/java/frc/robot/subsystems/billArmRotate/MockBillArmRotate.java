package frc.robot.subsystems.billArmRotate;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockBillArmRotate implements BillArmRotateBase {
  @Override
  public void rotate(double speed) {
  }

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public void engageArmBrake() {
  }

  @Override
  public void disengageArmBrake() {
  }

  public BillArmBrakeState getBrakeState() {
    return BillArmBrakeState.ENGAGEARMBRAKE;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void stop() {
  }

  @Override
  public void disableMotorBrake() {
  }

  @Override
  public void enableMotorBrake() {
  }
}
