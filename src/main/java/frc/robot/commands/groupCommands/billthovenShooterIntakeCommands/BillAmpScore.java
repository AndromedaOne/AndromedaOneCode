package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.DisengageEndEffectorPosition;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;

public class BillAmpScore extends SequentialCommandGroup4905 {

  public BillAmpScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter) {
    // need to determine final values
    final double m_armSetpoint = 0.0;
    final double m_feederSpeed = 1000;
    final double m_shooterSpeed = 4000;
    final boolean m_feederReverseState = false;

    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);

    addCommands(
        new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armSetpoint, true),
            new DisengageEndEffectorPosition(endEffector)),
        new ParallelCommandGroup4905(runShooterCommand, new RunBillFeeder(feeder,
            () -> m_feederSpeed, m_feederReverseState, runShooterCommand.atSetpoint())));

  }

}
