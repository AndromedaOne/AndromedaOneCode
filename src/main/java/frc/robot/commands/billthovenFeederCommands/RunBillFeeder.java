package frc.robot.commands.billthovenFeederCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billFeeder.BillFeederBase;

public class RunBillFeeder extends Command {
  private BillFeederBase m_feeder;
  private BooleanSupplier m_readyToShoot;
  private FeederStates m_feederState = FeederStates.EJECT;

  /** use this if you are shooting. */
  public RunBillFeeder(BillFeederBase feeder, FeederStates feederState,
      BooleanSupplier readyToShoot) {
    m_feeder = feeder;
    m_readyToShoot = readyToShoot;
    m_feederState = feederState;
    addRequirements(m_feeder.getSubsystemBase());
  }

// use this constructor if not shooting
  public RunBillFeeder(BillFeederBase feeder, FeederStates feederState) {
    this(feeder, feederState, () -> false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_feederState) {
    case INTAKE:
      if (m_feeder.getNoteDetectorState()) {
        m_feeder.stopBillFeeder();
      } else {
        m_feeder.runBillFeederIntake();
      }
      return;
    case EJECT:
      m_feeder.runBillFeederEject();
      return;
    case SHOOTING:
      if (m_readyToShoot.getAsBoolean()) {
        m_feeder.runBillFeederShooting();
      } else {
        m_feeder.stopBillFeeder();
      }
      return;
    case TRAPSHOOTING:
      m_feeder.runBillFeederTrapShooting();
      return;
    default:
      throw new UnsupportedOperationException("unknown feeder state: " + m_feederState.toString());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_feeder.stopBillFeeder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if ((m_feederState == FeederStates.INTAKE) && (m_feeder.getNoteDetectorState())) {
      return true;
    }
    return false;
  }
}