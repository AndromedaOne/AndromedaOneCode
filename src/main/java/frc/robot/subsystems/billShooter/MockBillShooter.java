package frc.robot.subsystems.billShooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(Command command) {
    throw new UnsupportedOperationException("Unimplemented method 'setDefaultCommand'");
  }
}
