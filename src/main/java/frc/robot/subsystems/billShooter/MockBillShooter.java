package frc.robot.subsystems.billShooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockBillShooter implements BillShooterBase {
  @Override
  public void setShooterWheelPower(double power) {
  }

  @Override
  public double getShooterWheelPower() {
    return 0;
  }

  @Override
  public double getShooterWheelRpm() {
    return 0;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }
}
