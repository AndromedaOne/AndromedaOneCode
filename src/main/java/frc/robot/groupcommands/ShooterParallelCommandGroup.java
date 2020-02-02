package frc.robot.groupcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.pidcommands.RunShooterSeriesVelocity;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShooterParallelCommandGroup extends ParallelCommandGroup {
    private Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
    private final double m_seriesIdleSpeed = m_shooterConfig.getDouble("seriesidlespeed");
    private final double m_shooterIdleSpeed = m_shooterConfig.getDouble("shooteridlespeed");
    private ShooterBase m_shooter;

    public ShooterParallelCommandGroup(ShooterBase shooter) {
        m_shooter = shooter;
         }

    @Override
    public void initialize() {
        addCommands(new RunShooterSeriesVelocity(m_shooter, m_seriesIdleSpeed), new RunShooterWheelVelocity(m_shooter, m_shooterIdleSpeed));
    }

}
