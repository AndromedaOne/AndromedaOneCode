package frc.robot.subsystems.armTestBenchRotate;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockArmTestBenchRotate implements ArmTestBenchRotateBase {
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

  public ArmTestBenchRotateBrakeState getBrakeState() {
    return ArmTestBenchRotateBrakeState.ENGAGEARMBRAKE;
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
