package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Timer;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.DisengageEndEffectorPosition;
import frc.robot.commands.billthovenEndEffectorPositionCommands.EngageEndEffectorPosition;
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

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    // need to determine final values
    // these are going to be our close distance defalt
    double m_armSetpointInit = 0.0;
    double m_shooterSpeedInit = 4000;
    Command endEffectorPosition;
    if (distance == SpeakerScoreDistanceEnum.MID) {
      m_armSetpointInit = 0.0;
      m_shooterSpeedInit = 4000;
      endEffectorPosition = new EngageEndEffectorPosition(endEffector);
    } else if (distance == SpeakerScoreDistanceEnum.FAR) {
      m_armSetpointInit = 0.0;
      m_shooterSpeedInit = 4000;
      endEffectorPosition = new DisengageEndEffectorPosition(endEffector);
    } else {
      endEffectorPosition = new EngageEndEffectorPosition(endEffector);
    }
    final double m_armSetpoint = m_armSetpointInit;
    final double m_shooterSpeed = m_shooterSpeedInit;
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);

    addCommands(
        new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armSetpoint, true),
            endEffectorPosition),
        new ParallelDeadlineGroup4905(new Timer(10000), new ParallelCommandGroup4905(
            runShooterCommand, new RunBillFeeder(feeder, FeederStates.SHOOTING))));

  }

}
