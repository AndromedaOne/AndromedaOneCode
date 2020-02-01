package frc.robot.subsystems.shooter;

public class MockShooter extends ShooterBase {

  @Override
  public double getShooterWheelVelocity() {
    System.out.println("Getting Mock Shooter Velocity");
    return 0;
  }

  @Override
  public void setShooterWheelPower(double power) {
    System.out.println("Setting Mock Shooter Power: " + power);

  }

  @Override
  public double getShooterPower() {
    System.out.println("Getting Mock Shooter Power");
    return 0;
  }

  @Override
  public void openShooterHood() {
    System.out.println("Open Mock Shooter Hood");

  }

  @Override
  public void closeShooterHood() {
    System.out.println("Closed Mock Shooter Hood");

  }

  @Override
  public void setPIDIsReady(boolean isReady) {
    System.out.println("Setting Mock Shooter PID Is Ready To: " + isReady);
  }

  @Override
  public boolean shooterIsReady() {
    System.out.println("Getting Mock Shooter is Ready Flag");
    return false;
  }

  @Override
  public boolean isShooterHoodOpen() {
    System.out.println("Getting Mock is Shooter Hood Open");
    return false;
  }

  @Override
  public void setShooterSeriesPower(double power) {
    System.out.println("Setting shooter series power to: " + power);
  }

  @Override
  public double getShooterSeriesVelocity() {
    System.out.println("Getting Mock Shooter Series Velocity");
    return 0;
  }

  @Override
  public double getSeriesPower() {
    System.out.println("Getting Mock Shooter Series Power");
    return 0;
  }

}