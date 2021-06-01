package frc.robot.subsystems.romiwings;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class RomiWingsBase extends SubsystemBase{
    
    public abstract void letWingsDown();

    public abstract void bringWingsUp();

    public abstract void stop();
}
