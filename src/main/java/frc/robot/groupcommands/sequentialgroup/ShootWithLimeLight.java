package frc.robot.groupcommands.sequentialgroup;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.groupcommands.parallelgroup.ShootWithRPM;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShootWithLimeLight extends SequentialCommandGroup {

    private ShooterBase m_shooter;
    private FeederBase m_feeder;
    private LimeLightCameraBase m_limeLight;
    private boolean m_isDone = false;
    private int m_samples = 0;
    private BallFeederSensorBase m_ballFeederSensor;
      // This is the amount of samples we need to determine whether
    // the feeder is actually empty
    private final int kNumOfSamples;

    public ShootWithLimeLight(ShooterBase shooter, FeederBase feeder, LimeLightCameraBase limeLight) {
        Config feederConfig = Config4905.getConfig4905().getFeederConfig();

        m_ballFeederSensor = Robot.getInstance().getSensorsContainer().getBallFeederSensor();
        m_shooter = shooter;
        m_feeder = feeder;
        m_limeLight = limeLight;
        kNumOfSamples = feederConfig.getInt("shootWithRPM.numOfFeederTestSamples");
    }

    @Override
    public void initialize() {
        super.initialize();
        m_isDone = false;
        CommandScheduler.getInstance().schedule(new ShootWithRPM(m_shooter, m_feeder,
        m_shooter.getShooterMap().getInterpolatedRPM(m_limeLight.distanceToPowerPort())));
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
  
      if (m_samples == kNumOfSamples) {
        m_isDone = true;
      }
    }
  
    @Override
    public boolean isFinished() {
      return m_isDone;
    }
  
}