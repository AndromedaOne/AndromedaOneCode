package frc.robot.subsystems.hopper;

public class RealHopper extends HopperBase {
  public boolean ballInHopperPos(int position) {
    // Check if ball is in a certain position on the hopper, using a sensor
    return false;
  }

  public void turnHopperNumPositions(double positions) {
    // Turn hopper a certain number of positions
  }

  public void numBallsInHopper() {
    // Check how many balls are in the hopper
  }

  public void sendBallToShooter() {
    // Run motor to send the ball in ShooterPos up to the shooter
  }

  public boolean ballInIntakePos() {
    // Check if a ball is already in the hopper position which the intake sends ball
    // to
    return false;
  }

  public boolean ballInShooterPos() {
    // Check if a ball is in position to be sent to the shooter
    return false;
  }
}