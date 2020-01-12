package frc.robot.subsystems.hopper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class HopperBase extends SubsystemBase {
    public abstract void ballInHopperPos(int position);

    public abstract void turnHopperNumPositions(double positions);

    public abstract void numBallsInHopper();

    public abstract void sendBallToShooter();

    public abstract boolean ballInIntakePos();

    public abstract boolean ballInShooterPos();
}