package frc.robot.commands.billthovenFeederCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.billFeeder.BillFeederBase;

public class RunBillFeeder extends Command {
  private BillFeederBase m_feeder;
  private BooleanSupplier m_readyToShoot;
  private FeederStates m_feederState = FeederStates.EJECT;
  private boolean m_noteInPlace = false;
  private boolean m_autonomous = false;
  private int m_count = 0;
  private SubsystemController m_controller;

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
    m_noteInPlace = false;
    m_autonomous = Robot.getInstance().isAutonomous();
    m_count = 0;
    m_controller = Robot.getInstance().getOIContainer().getSubsystemController();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_feederState) {
    case INTAKE:
      if (!m_feeder.getNoteDetectorState() && !m_noteInPlace) {
        m_feeder.runBillFeederIntake();
      } else if (m_feeder.getNoteDetectorState()) {
        m_feeder.runBillFeederSlowEject();
        m_noteInPlace = true;
      } else {
        m_feeder.stopBillFeeder();
      }
      return;
    case EJECT:
      m_feeder.runBillFeederEject();
      return;
    case SHOOTING:
      if ((m_readyToShoot.getAsBoolean()) && (m_controller.getBillFireTrigger())) {
        m_feeder.runBillFeederShooting();
        m_count++;
      } else if ((m_readyToShoot.getAsBoolean()) && (m_autonomous)) {
        m_feeder.runBillFeederShooting();
        m_count++;
      } else {
        m_feeder.stopBillFeeder();
      }
      return;
    case TRAPSHOOTING:
      m_feeder.runBillFeederTrapShooting();
      return;
    case AMPSHOOTING:
      if ((m_controller.getBillFireTrigger())) {
        m_feeder.runBillFeederEject();
        m_count++;
      } else if (m_autonomous) {
        m_feeder.runBillFeederEject();
        m_count++;
      }
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
    if (m_feederState == FeederStates.INTAKE) {
      if ((!m_feeder.getNoteDetectorState() && m_noteInPlace)
          || ((!m_controller.getBillFeederIntakeNoteButton().getAsBoolean()) && (!m_autonomous))) {
        return true;
      }
    } else if ((m_feederState == FeederStates.SHOOTING)
        || (m_feederState == FeederStates.AMPSHOOTING)) {
      if ((m_count >= 50) || ((!m_controller.getBillAmpScoreButton().getAsBoolean())
          && (!m_controller.getBillSpeakerFarScoreButton().getAsBoolean())
          && (!m_controller.getBillSpeakerCloseScoreButton().getAsBoolean())
          && (!m_controller.getBillSpeakerMidScoreButton().getAsBoolean()) && (!m_autonomous)))
        return true;
    }
    return false;
  }
}