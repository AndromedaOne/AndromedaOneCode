package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

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

public class BillSpeakerScore extends SequentialCommandGroup4905 {
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

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    m_armRotate = armRotate;
    m_endEffector = endEffector;
    m_feeder = feeder;
    m_shooter = shooter;
    m_distance = distance;

  }

  public void additionalInitialize() {
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
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(m_endEffector);
      } else {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(m_endEffector);
      }
    } else if (m_distance == SpeakerScoreDistanceEnum.FAR) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 3000;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      } else {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      }
    } else {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 350;
        m_shooterSpeedInit = 3000;
        endEffectorPosition = new MoveToLowPosition(m_endEffector);
      } else {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 3000;
        endEffectorPosition = new MoveToHighPosition(m_endEffector);
      }
    }
    final double m_armSetpoint = m_armSetpointInit;
    final double m_shooterSpeed = m_shooterSpeedInit;
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(m_shooter, m_shooterSpeed);

    CommandScheduler.getInstance().schedule(
        new ParallelCommandGroup4905(new ArmRotate(m_armRotate, () -> m_armSetpoint, true),
            endEffectorPosition),
        new ParallelDeadlineGroup4905(new RunBillFeeder(m_feeder, FeederStates.SHOOTING,
            runShooterCommand.getOnTargetSupplier()), runShooterCommand),
        new ParallelCommandGroup4905(new ArmRotate(m_armRotate, () -> 300, true),
            new MoveToLowPosition(m_endEffector)));
  }

}
