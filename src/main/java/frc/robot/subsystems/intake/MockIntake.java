package frc.robot.subsystems.intake;

public class MockIntake extends IntakeBase {
  public void runIntake() {
    System.out.println("Running intake.");
  }

  public void stopIntake() {
    System.out.println("Stopping intake.");
  }

  public boolean ballInIntake() {
    System.out.println("Checking if ball in intake");
    return false;
  }

  public void moveBallToHopper() {
    System.out.println("Moving ball to hopper.");
  }
}