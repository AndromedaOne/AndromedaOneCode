package frc.robot.commands.billthovenFeederCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.telemetries.Trace;

public class RunBillFeeder extends Command {
  private BillFeederBase m_feeder;
  private DoubleSupplier m_speed;
  private boolean m_runInReverse;
  private BooleanSupplier m_readyToShoot;

  /** Creates a new RunFeeder. */
  public RunBillFeeder(BillFeederBase feeder, DoubleSupplier speed, boolean runInReverse,
      BooleanSupplier readyToShoot) {
    m_feeder = feeder;
    m_speed = speed;
    m_runInReverse = runInReverse;
    m_readyToShoot = readyToShoot;
    addRequirements(m_feeder.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_runInReverse) {
      m_feeder.runBillFeeder(-m_speed.getAsDouble());
    } else if (m_readyToShoot.getAsBoolean()) {
      m_feeder.runBillFeeder(m_speed.getAsDouble());
    } /*
       * else if (Robot.getInstance().getOIContainer().getDriveController()
       * .getTopGunEjectCargoButton()) {
       * m_feeder.runBillFeeder(m_speed.getAsDouble());
       * 
       * }
       */ else {
      m_feeder.runBillFeeder(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    m_feeder.stopBillFeeder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}