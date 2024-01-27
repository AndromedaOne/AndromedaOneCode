package frc.robot.subsystems.billClimber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MockBillClimber implements BillClimberBase {

  @Override
  public void driveWinch(double speed) {

  }

  @Override
  public void unwindWinch() {

  }

  @Override
  public void stopWinch() {

  }

  @Override
  public double getWinchAdjustedEncoderValue() {
    return 0;
  }

  @Override
  public void setWinchBrakeMode(boolean brakeOn) {

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
