package frc.robot.subsystems.shooter;

public class MockShooter extends ShooterBase {

  @Override
  public double getShooterVelocity() {
    System.out.println("Getting Mock Shooter Velocity");
    return 0;
  }

  @Override
  public void setShooterPower(double power) {
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

}