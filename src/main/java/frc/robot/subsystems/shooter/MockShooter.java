package frc.robot.subsystems.shooter;

public class MockShooter extends ShooterBase {

  public MockShooter() {
    System.out.println(" - Constructing Mock Shooter - ");
  }

  @Override
  public double getShooterWheelVelocity() {
    return 0;
  }

  @Override
  public void setShooterWheelPower(double power) {
  }

  @Override
  public double getShooterPower() {
    return 0;
  }

  @Override
  public void openShooterHood() {
  }

  @Override
  public void closeShooterHood() {
  }

  @Override
  public void setShooterPIDIsReady(boolean isReady) {
  }

  @Override
  public boolean shooterIsReady() {
    return true;
  }

  @Override
  public boolean isShooterHoodOpen() {
    return false;
  }

  @Override
  public void setShooterSeriesPower(double power) {
  }

  @Override
  public double getShooterSeriesVelocity() {
    return 0;
  }

  @Override
  public double getSeriesPower() {
    return 0;
  }

  @Override
  public void setSeriesPIDIsReady(boolean isReady) {
  }

  @Override
  public void setShooterIsIdle(boolean isIdle) {
  }

}