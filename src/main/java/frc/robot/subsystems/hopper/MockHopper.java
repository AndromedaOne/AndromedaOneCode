package frc.robot.subsystems.hopper;

public class MockHopper extends HopperBase {

    public boolean ballInHopperPos(int position) {
        System.out.println("Checking if ball in hopper pos: " + position);
        return false;
    }

    public void turnHopperNumPositions(double positions) {
        System.out.println("Turning hopper " + positions + " positions");
    }

    public void numBallsInHopper() {
        System.out.println("0 balls in hopper.");
    }

    public void sendBallToShooter() {
        System.out.println("Sending ball to shooter.");
    }

    public boolean ballInIntakePos() {
        System.out.println("Checking if ball in intake position.");
        return false;
    }

    public boolean ballInShooterPos() {
        System.out.println("Checking if ball in shooter position.");
        return false;
    }
}
