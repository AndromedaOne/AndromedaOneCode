package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ExampleSubsystem;

public LimelightTTF extends PIDCommand {
    public LimelightTTF() {
        super(new PIDController())
    }
}