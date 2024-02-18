package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToHighPosition;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToLowPosition;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;

public class IntakeNote extends SequentialCommandGroup4905 {

  public IntakeNote(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder) {

    final double m_armIntakeSetpoint = 350.0; // we dont know - 300 test only
    final double m_armDriveSetpoint = 300.0; // we dont know - 265 test only

    addCommands(
        new ParallelDeadlineGroup4905(new RunBillFeeder(feeder, FeederStates.INTAKE),
            new ArmRotate(armRotate, () -> m_armIntakeSetpoint, true),
            new MoveToLowPosition(endEffector)),
        new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armDriveSetpoint, true),
            new MoveToHighPosition(endEffector)));
  }
}