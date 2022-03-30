package frc.robot.subsystems.climber;

public class MockClimber extends ClimberBase {

  public MockClimber() {
  }

  @Override
  public void unwindLeftWinch() {

  }

  @Override
  public void unwindRightWinch() {

  }

  @Override
  public void stopLeftWinch() {

  }

  @Override
  public void stopRightWinch() {

  }

  @Override
  public boolean leftWinchAtTopLimitSwitch() {
    return false;
  }

  @Override
  public boolean leftWinchAtBottomLimitSwitch() {
    return false;
  }

  @Override
  public boolean rightWinchAtTopLimitSwitch() {
    return false;
  }

  @Override
  public boolean rightWinchAtBottomLimitSwitch() {
    return false;
  }

  @Override
  public double getLeftWinchAdjustedEncoderValue() {
    return 0;
  }

  @Override
  public double getRightWinchAdjustedEncoderValue() {
    return 0;
  }

  @Override
  public void setWinchBrakeMode(boolean brakeOn) {

  }

  @Override
  public void driveLeftWinch(double speed) {

  }

  @Override
  public void driveRightWinch(double speed) {

  }

}
