package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterBase extends SubsystemBase {
    public abstract void tiltToDegree(double degrees);
    
    public abstract void setShooterVelocity(double velocity);

    public abstract void turnTurretToDegree(double degrees);
}