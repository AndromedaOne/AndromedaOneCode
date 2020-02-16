package frc.robot.groupcommands.parallelgroup;

import java.util.function.BooleanSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.FeedWhenReadyStarter;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShootWithRPM extends ParallelCommandGroup {
  private BooleanSupplier m_isDoneFeedingSupplier = this::isDoneFeeding;
  private boolean m_isDone = false;
  private BallFeederSensorBase m_ballFeederSensor;
  // This is the amount of samples we need to determine whether
  // the feeder is actually empty
  private final int kNumOfSamples;
  private int m_samples;

  /**
   * This takes in a shooter and an rpm and intelligently shoots all the balls
   * that are currently inside the feeder But you have to manually set both the
   * shooter and series wheel rpm
   * 
   * @param shooter
   * @param feeder
   * @param shooterRPM
   * @param seriesRPM
   */
  public ShootWithRPM(ShooterBase shooter, FeederBase feeder, double shooterRPM, double seriesRPM) {
    Config feederConfig = Config4905.getConfig4905().getFeederConfig();

    m_ballFeederSensor = Robot.getInstance().getSensorsContainer().getBallFeederSensor();
    kNumOfSamples = feederConfig.getInt("shootWithRPM.numOfFeederTestSamples");

    addCommands(new ShooterParallelSetShooterVelocity(shooter, seriesRPM, shooterRPM),
        new FeedWhenReadyStarter(shooter, feeder, m_isDoneFeedingSupplier));
  }

  /**
   * This takes in a shooter and an rpm and intelligently shoots all the balls
   * that are currently inside the feeder
   * 
   * @param shooter
   * @param feeder
   * @param rpm     This will we scaled for the series wheel rpm based off the
   *                scale factor in the config
   */
  public ShootWithRPM(ShooterBase shooter, FeederBase feeder, double rpm) {
    this(shooter, feeder, rpm, rpm * Config4905.getConfig4905().getShooterConfig().getDouble("seriesRPMScale"));
  }

  @Override
  public void execute() {
    super.execute();

    if (m_ballFeederSensor.getNumberOfPowerCellsInFeeder() == 0) {
      m_samples++;
    } else {
      m_samples = 0;
    }

    if (m_samples == kNumOfSamples) {
      m_isDone = true;
    }
  }

  @Override
  public boolean isFinished() {
    return isDoneFeeding();
  }

  private boolean isDoneFeeding() {
    return m_isDone;
  }
}