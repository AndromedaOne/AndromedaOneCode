package frc.robot.commands.billthovenFeederCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.billthovenClimberCommands.BillClimberSingleton;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.ledlights.LEDs;
import frc.robot.telemetries.Trace;

public class RunBillFeeder extends Command {
  private BillFeederBase m_feeder;
  private BooleanSupplier m_shooterReadyToShoot;
  private BooleanSupplier m_armComplete;
  private FeederStates m_feederState = FeederStates.EJECT;
  private boolean m_noteInPlace = false;
  private boolean m_autonomous = false;
  private int m_count = 0;
  private SubsystemController m_controller;
  private LEDs m_LEDs;

  /** use this if you are shooting. */
  public RunBillFeeder(BillFeederBase feeder, FeederStates feederState,
      BooleanSupplier shooterReadyToShoot, BooleanSupplier armComplete) {
    m_feeder = feeder;
    m_shooterReadyToShoot = shooterReadyToShoot;
    m_armComplete = armComplete;
    m_feederState = feederState;
    m_LEDs = Robot.getInstance().getSubsystemsContainer().getWs2812LEDs();
    addRequirements(m_feeder.getSubsystemBase());
  }

// use this constructor if not shooting
  public RunBillFeeder(BillFeederBase feeder, FeederStates feederState) {
    this(feeder, feederState, () -> false, () -> false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_noteInPlace = false;
    m_LEDs.setNoteState(false);
    m_autonomous = Robot.getInstance().isAutonomous();
    Trace.getInstance().logCommandInfo(this, "in auto : " + m_autonomous);
    m_count = 0;
    m_controller = Robot.getInstance().getOIContainer().getSubsystemController();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean readyToShoot = m_shooterReadyToShoot.getAsBoolean() && m_armComplete.getAsBoolean();

    if (BillClimberSingleton.getInstance().getClimberEnabled()) {
      return; // Remove this when trap scoring becomes a thing
    }
    switch (m_feederState) {
    case INTAKE:
      if (!m_feeder.getNoteDetectorState() && !m_noteInPlace) {
        m_feeder.runBillFeederIntake();
      } else if (m_feeder.getNoteDetectorState()) {
        m_feeder.runBillFeederSlowEject();
        m_noteInPlace = true;
        m_LEDs.setNoteState(true);
      } else {
        m_feeder.stopBillFeeder();
      }
      return;
    case EJECT:
      m_feeder.runBillFeederEject();
      return;
    case SHOOTING:
      if ((readyToShoot) && (m_controller.getBillFireTrigger())) {
        m_feeder.runBillFeederShooting();
        SmartDashboard.putBoolean("Bill Fire Trigger", m_controller.getBillFireTrigger());
        m_count++;
      } else if ((readyToShoot) && (m_autonomous)) {
        m_feeder.runBillFeederShooting();
        m_count++;
      } else {
        m_feeder.stopBillFeeder();
      }
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
    System.out.println("feeder stopped");
    m_noteInPlace = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (BillClimberSingleton.getInstance().getClimberEnabled()) {
      return true; // Also remove this one
    }
    if (m_feederState == FeederStates.INTAKE) {
      if ((!m_feeder.getNoteDetectorState() && m_noteInPlace)
          || ((!m_controller.getBillFeederIntakeNoteButton().getAsBoolean()) && (!m_autonomous))) {
        Trace.getInstance().logCommandInfo(this, "Note in feeder");
        return true;
      }
    } else if ((m_feederState == FeederStates.SHOOTING)
        && (m_controller.getBillTrapShotButton().getAsBoolean())) {
      return false;
    } else if (m_feederState == FeederStates.SHOOTING) {
      if ((m_count >= 10) || ((!m_controller.getBillSpeakerAwayScoreButton().getAsBoolean())
          && (!m_controller.getBillSpeakerCloseScoreButton().getAsBoolean())
          && (!m_controller.getBillTrapShotButton().getAsBoolean())
          && (!m_controller.getBillSpeakerShuttleScoreButton().getAsBoolean())
          && (!m_autonomous))) {
        System.out.println("RunBillFeeder Finished ");
        return true;
      }
    } else if (m_feederState == FeederStates.AMPSHOOTING) {
      if (((m_count >= 50) && (m_autonomous))
          || ((!m_controller.getBillAmpScoreButton().getAsBoolean()) && (!m_autonomous))) {
        System.out.println("RunBillFeeder Finished ");
        return true;
      }
    }
    return false;
  }
}