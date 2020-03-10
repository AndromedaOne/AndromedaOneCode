package frc.robot.oi;

import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class AutoSubsystemsAndParameters {

  private ShooterBase m_shooter;

  public ShooterBase getShooter() {
    return m_shooter;
  }

  private FeederBase m_feeder;

  public FeederBase getFeeder() {
    return m_feeder;
  }

  public LimeLightCameraBase getLimelight() {
    return m_limelight;
  }

  private LimeLightCameraBase m_limelight;

  public IntakeBase getIntake() {
    return m_intake;
  }

  private IntakeBase m_intake;

  public double getMaxSpeedToPickupPowerCells() {
    return m_maxSpeedToPickupPowerCells;
  }

  private double m_maxSpeedToPickupPowerCells;

  private DriveTrain m_driveTrain;

  public DriveTrain getDriveTrain() {
    return m_driveTrain;
  }

  public AutoSubsystemsAndParameters(ShooterBase shooter, FeederBase feeder, LimeLightCameraBase limelight,
      IntakeBase intake, double maxSpeedToPickupPowerCells, DriveTrain driveTrain) {
    m_shooter = shooter;
    m_feeder = feeder;
    m_limelight = limelight;
    m_intake = intake;
    m_maxSpeedToPickupPowerCells = maxSpeedToPickupPowerCells;
    m_driveTrain = driveTrain;
  }
}