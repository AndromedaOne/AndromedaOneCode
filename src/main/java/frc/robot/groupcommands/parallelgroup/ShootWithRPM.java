package frc.robot.groupcommands.parallelgroup;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.FeedBothStagesIntoShooter;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class ShootWithRPM extends ParallelCommandGroup {
  private BooleanSupplier m_isDoneFeedingSupplier = this::isDoneFeeding;
  private boolean m_isDone = false;
  private ShooterBase m_shooter;
  private FeederBase m_feeder;
  private BallFeederSensorBase m_ballFeederSensor;
  // This is the amount of samples we need to determine whether
  // the feeder is actually empty
  private final int kNumOfSamples;
  private int m_samples;
  private boolean m_useSmartDashboardForRPM = false;
  private double m_shooterRPM = 0;
  private double m_seriesRPM = 0;

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
  public ShootWithRPM(ShooterBase shooter, FeederBase feeder, double shooterRPM, double seriesRPM,
      boolean useSmartDashboardForRPM) {

    m_shooter = shooter;
    m_feeder = feeder;
    m_ballFeederSensor = Robot.getInstance().getSensorsContainer().getBallFeederSensor();
    kNumOfSamples = Config4905.getConfig4905().getCommandConstantsConfig()
        .getInt("ShootWithRPM.numOfFeederTestSamples");
    m_seriesRPM = seriesRPM;
    m_useSmartDashboardForRPM = useSmartDashboardForRPM;
    m_shooterRPM = shooterRPM;
    addCommands(new ShooterParallelSetShooterVelocity(m_shooter, seriesRPM, () -> m_shooterRPM),
            new FeedBothStagesIntoShooter(m_feeder, m_shooter, m_isDoneFeedingSupplier));
  }

  public ShootWithRPM(ShooterBase shooter, FeederBase feeder, double shooterRPM, double seriesRPM) {
    this(shooter, feeder, shooterRPM, seriesRPM, false);
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
    this(shooter, feeder, rpm,
        rpm * Config4905.getConfig4905().getCommandConstantsConfig().getDouble("ShootWithRPM.seriesRPMScale"), false);
  }

  public ShootWithRPM(ShooterBase shooter, FeederBase feeder, boolean useSmartDashboardForRPM) {
    this(shooter, feeder, 0, 0, true);
  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
    m_isDone = false;
    m_samples = 0;

    double seriesRPM = m_seriesRPM;
    if (m_useSmartDashboardForRPM) {
      m_shooterRPM = SmartDashboard.getNumber("ShooterRPMTarget", 0);
      seriesRPM = m_shooterRPM
          * Config4905.getConfig4905().getCommandConstantsConfig().getDouble("ShootWithRPM.seriesRPMScale");
    }
    System.out.println("ShooterRPM = " + m_shooterRPM);

  }

  @Override
  public void execute() {
    super.execute();
    int numOfCells = m_ballFeederSensor.getNumberOfPowerCellsInFeeder();

    if (numOfCells == 0) {
      m_samples++;
    } else {
      m_samples = 0;
    }

    if (m_samples >= kNumOfSamples) {
      m_isDone = true;
    }
  }

  @Override
  public boolean isFinished() {
    return isDoneFeeding();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_isDone = true;
    m_shooter.setShooterSeriesPower(0);
    m_shooter.setShooterWheelPower(0);
    Trace.getInstance().logCommandStop(this);
  }

  private boolean isDoneFeeding() {
    return m_isDone;
  }
}