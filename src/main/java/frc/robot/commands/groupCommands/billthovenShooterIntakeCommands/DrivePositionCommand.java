package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import frc.robot.commands.billthovenArmRotateCommands.ArmRotateCommand;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;

public class DrivePositionCommand extends ParallelCommandGroup4905 {

  public DrivePositionCommand(BillEndEffectorPositionBase endEffector,
      ArmTestBenchRotateBase armRotate) {

    final double m_armSetpoint = 290.0;

    addCommands(new ArmRotateCommand(armRotate, () -> m_armSetpoint, true),
        new MoveEndEffector(endEffector, () -> false));
  }
}
