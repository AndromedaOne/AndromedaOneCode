package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToHighPosition;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToLowPosition;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;

public class BillSpeakerScore extends Command {
  public enum SpeakerScoreDistanceEnum {
    CLOSE, MID, FAR
  }

  public enum SpeakerScoreArmPositionEnum {
    LOW, HIGH
  }

  private BillArmRotateBase m_armRotate;
  private BillEndEffectorPositionBase m_endEffector;
  private BillFeederBase m_feeder;
  private BillShooterBase m_shooter;
  private SpeakerScoreDistanceEnum m_distance;
  private boolean m_useSmartDashboard;

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance,
      boolean useSmartDashboard) {
    m_armRotate = armRotate;
    m_endEffector = endEffector;
    m_feeder = feeder;
    m_shooter = shooter;
    m_distance = distance;
    m_useSmartDashboard = useSmartDashboard;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("ShooterCommand RPM", 3000);
      SmartDashboard.putNumber("ShooterCommand ArmPosition", 300);
    }
    addRequirements(armRotate.getSubsystemBase(), endEffector.getSubsystemBase(), feeder.getSubsystemBase(), 
      shooter.getSubsystemBase());
  }

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    this(armRotate, endEffector, feeder, shooter, distance, false);
  }
@Override
  public void initialize() {
    // need to determine final values
    // these are going to be our close distance defalt
    double m_armSetpointInit = 300;
    double m_shooterSpeedInit = 1000;
    Command endEffectorPosition;
    SpeakerScoreArmPositionEnum armPosition = SpeakerScoreArmPositionEnum.LOW;

    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getBillShootingPositionButton().getAsBoolean()) {
      armPosition = SpeakerScoreArmPositionEnum.HIGH;
    }

    if (m_distance == SpeakerScoreDistanceEnum.MID) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 315;
        m_shooterSpeedInit = 3650;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      } else {
        m_armSetpointInit = 285;
        m_shooterSpeedInit = 3650;
        endEffectorPosition = new MoveToHighPosition(m_endEffector);
      }
    } else if (m_distance == SpeakerScoreDistanceEnum.FAR) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 306;
        m_shooterSpeedInit = 3800;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      } else { // not currently used idea of lower power to just chuck notes over
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(m_endEffector);
      }
    } else {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 333;
        m_shooterSpeedInit = 3250;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      } else { // these are the same because there is no point in shooting high over a defense
               // robot
        m_armSetpointInit = 333;
        m_shooterSpeedInit = 3250;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      }
    }
    if (m_useSmartDashboard) {
      m_armSetpointInit = SmartDashboard.getNumber("ShooterCommand ArmPosition", 300);
      m_shooterSpeedInit = SmartDashboard.getNumber("ShooterCommand RPM", 1000);

    }
    final double m_armSetpoint = m_armSetpointInit;
    final double m_shooterSpeed = m_shooterSpeedInit;
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(m_shooter, m_shooterSpeed);

    CommandScheduler.getInstance()
        .schedule(new SequentialCommandGroup4905(
            new ParallelCommandGroup4905(new ArmRotate(m_armRotate, () -> m_armSetpoint, true),
                endEffectorPosition),
            new ParallelDeadlineGroup4905(new RunBillFeeder(m_feeder, FeederStates.SHOOTING,
                runShooterCommand.getOnTargetSupplier()), runShooterCommand)));
  }
  @Override
  public boolean isFinished() {
    return true;
  }

}
