package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.Timer;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.DisengageEndEffectorPosition;
import frc.robot.commands.billthovenEndEffectorPositionCommands.EngageEndEffectorPosition;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;

public class IntakeNote extends SequentialCommandGroup4905 {

  public IntakeNote(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder) {

    final double m_armIntakeSetpoint = 300.0; // we dont know - 300 test only
    final double m_armDriveSetpoint = 265.0; // we dont know - 265 test only
    final double m_feederSpeed = 1000.0; // we dont know
    final boolean m_feederReverseState = false;

    addCommands(
        new ParallelDeadlineGroup(new Timer(15000),
            new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armIntakeSetpoint, true),
                new DisengageEndEffectorPosition(endEffector),
                new RunBillFeeder(feeder, () -> m_feederSpeed, m_feederReverseState, () -> false))),
        new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armDriveSetpoint, true),
            new EngageEndEffectorPosition(endEffector)));
  }
}