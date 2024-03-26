package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;

public class IntakeNote extends SequentialCommandGroup4905 {

  public IntakeNote(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, boolean endInDrivePosition) {
    final double m_armIntakeSetpoint = 352.0; // we dont know - 300 test only
    if (endInDrivePosition) {
      addCommands(
          new ParallelDeadlineGroup4905(new RunBillFeeder(feeder, FeederStates.INTAKE),
              new ArmRotate(armRotate, () -> m_armIntakeSetpoint, false, false),
              new MoveEndEffector(endEffector, () -> false),
              new RunBillShooterRPM(shooter, () -> -200)),
          new DrivePositionCommand(endEffector, armRotate));
    } else {
      addCommands(new ParallelDeadlineGroup4905(new RunBillFeeder(feeder, FeederStates.INTAKE),
          new ArmRotate(armRotate, () -> m_armIntakeSetpoint, false, false),
          new MoveEndEffector(endEffector, () -> false),
          new RunBillShooterRPM(shooter, () -> -200)));
    }
  }
}