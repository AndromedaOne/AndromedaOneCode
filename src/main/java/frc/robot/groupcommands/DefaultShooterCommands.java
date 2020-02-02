package frc.robot.groupcommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import frc.robot.commands.pidcommands.RunShooterSeriesVelocity;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.subsystems.shooter.ShooterBase;

public class DefaultShooterCommands extends CommandGroupBase {
    public DefaultShooterCommands(ShooterBase shooter, double setpoint) {
        addCommands(parallel(new RunShooterSeriesVelocity(shooter, setpoint), new RunShooterWheelVelocity(shooter, setpoint)));
    }

    @Override
    public void addCommands(Command... commands) {
        // TODO Auto-generated method stub

    }
}
