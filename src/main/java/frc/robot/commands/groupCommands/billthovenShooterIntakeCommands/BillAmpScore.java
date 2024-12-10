
package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import frc.robot.commands.billthovenArmRotateCommands.ArmRotateCommand;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;

public class BillAmpScore extends SequentialCommandGroup4905 {

  public BillAmpScore(ArmTestBenchRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder) {
    // need to determine final values
    final double m_armSetpoint = 300;
    addCommands(
        new ParallelCommandGroup4905(new ArmRotateCommand(armRotate, () -> m_armSetpoint, true),
            new MoveEndEffector(endEffector, () -> true)),
        new RunBillFeeder(feeder, FeederStates.AMPSHOOTING),
        new DrivePositionCommand(endEffector, armRotate));
  }

}
