package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IntakeBase extends SubsystemBase {
    public abstract void runIntake();

    public abstract void stopIntake();

    public abstract boolean ballInIntake();

    public abstract void moveBallToHopper();
}