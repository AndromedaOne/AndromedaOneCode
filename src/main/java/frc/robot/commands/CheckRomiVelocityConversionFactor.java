package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.RomiDriveTrain;

public class CheckRomiVelocityConversionFactor extends CommandBase {
  private RomiDriveTrain m_driveTrain;
  private boolean m_initialPositionSet;
  private double m_initialPosition;
  private Timer m_timer;
  public static final double TIME_OFFSET = 2;
  public static final double TIME_CALCULATING = 60;
  private int count;
  private double rateAccum;
  private double initialTime;

  public CheckRomiVelocityConversionFactor(DriveTrain driveTrain) {
    m_driveTrain = (RomiDriveTrain) driveTrain;
    m_initialPositionSet = false;
    m_initialPosition = 0;
    m_timer = new Timer();
    rateAccum = 0;
    count = 0;
    initialTime = 0;
  }

  @Override
  public void initialize() {
    super.initialize();
    rateAccum = 0;
    count = 0;
    initialTime = 0;
    m_initialPositionSet = false;
    m_initialPosition = 0;
    m_timer.reset();
    m_timer.start();

  }

  @Override
  public void execute() {
    super.execute();
    m_driveTrain.move(0.5, 0, false);
    if (!m_timer.hasElapsed(TIME_OFFSET)) {
      return;
    }

    if (!m_initialPositionSet) {
      m_initialPosition = m_driveTrain.getLeftRightAverageTicks();
      m_initialPositionSet = true;
      initialTime = m_timer.get();
    }
    double currentPosition = m_driveTrain.getLeftRightAverageTicks();
    double deltaT = m_timer.get() - initialTime;
    double currentAverageVelocity = (currentPosition - m_initialPosition) / deltaT;

    rateAccum += (m_driveTrain.getLeftRateMetersPerSecond() + m_driveTrain.getRightRateMetersPerSecond()) / 2.0;
    count++;
    double currentAverageRate = rateAccum / count;
    double ratio = currentAverageVelocity / currentAverageRate;
    SmartDashboard.putNumber("AAA ActualVelocityToRateRatio", ratio);

  }

  @Override
  public boolean isFinished() {
    return m_timer.hasElapsed(TIME_OFFSET + TIME_CALCULATING);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_driveTrain.stop();
  }
}
