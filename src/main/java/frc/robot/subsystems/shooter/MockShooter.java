package frc.robot.subsystems.shooter;

public class MockShooter extends ShooterBase {
  @Override
  public void tiltToDegree(double degrees) {
    System.out.println("Tilting shooter to degree: " + degrees);
  }

  public void setShooterVelocity(double velocity) {
    System.out.println("Setting shooter velocity to: " + velocity);
  }

  public void turnTurretToDegree(double degrees) {
    System.out.println("Turning turret to degree: " + degrees);
  }
}