package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToHighPosition;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToLowPosition;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
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

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance,
      SpeakerScoreArmPositionEnum armPosition) {
    // need to determine final values
    // these are going to be our close distance defalt
    double m_armSetpointInit = 300;
    double m_shooterSpeedInit = 1000;
    Command endEffectorPosition;
    if (distance == SpeakerScoreDistanceEnum.MID) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(endEffector);
      } else {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(endEffector);
      }
    } else if (distance == SpeakerScoreDistanceEnum.FAR) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 3000;
        endEffectorPosition = new MoveToLowPosition(endEffector);
      } else {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToLowPosition(endEffector);
      }
    } else {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(endEffector);
      } else {
        m_armSetpointInit = 300;
        m_shooterSpeedInit = 1000;
        endEffectorPosition = new MoveToHighPosition(endEffector);
      }
    }
    final double m_armSetpoint = m_armSetpointInit;
    final double m_shooterSpeed = m_shooterSpeedInit;
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, m_shooterSpeed);

    addCommands(
        new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armSetpoint, true),
            endEffectorPosition),
        new ParallelCommandGroup4905(runShooterCommand, new RunBillFeeder(feeder,
            FeederStates.SHOOTING, runShooterCommand.getOnTargetSupplier())));

  }

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    this(armRotate, endEffector, feeder, shooter, distance, SpeakerScoreArmPositionEnum.LOW);
  }

}
