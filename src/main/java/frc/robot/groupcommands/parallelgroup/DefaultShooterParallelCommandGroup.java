package frc.robot.groupcommands.parallelgroup;

import com.typesafe.config.Config;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.pidcommands.RunShooterSeriesVelocity;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.subsystems.shooter.ShooterBase;

public class DefaultShooterParallelCommandGroup extends ParallelCommandGroup {
    private Config m_shooterConfig;
    private double m_shooterIdleSpeed;
    private double m_seriesIdleSpeed;
    private ShooterBase m_shooter;

    public DefaultShooterParallelCommandGroup(ShooterBase shooter) {
        m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
        m_shooterIdleSpeed = m_shooterConfig.getDouble("shooteridlespeed");
        m_seriesIdleSpeed = m_shooterConfig.getDouble("seriesidlespeed");
        m_shooter = shooter;
        addRequirements(shooter);

        addCommands(new RunShooterSeriesVelocity(m_shooter, m_seriesIdleSpeed),
        new RunShooterWheelVelocity(m_shooter, m_shooterIdleSpeed));
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
