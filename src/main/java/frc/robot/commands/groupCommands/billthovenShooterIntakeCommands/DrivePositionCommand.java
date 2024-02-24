package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveToLowPosition;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;

public class DrivePositionCommand extends SequentialCommandGroup4905 {

  public DrivePositionCommand(BillEndEffectorPositionBase endEffector,
      BillArmRotateBase armRotate) {

    final double m_armSetpoint = 333.00;

    addCommands(new ParallelCommandGroup4905(new ArmRotate(armRotate, () -> m_armSetpoint, true),
        new MoveToLowPosition(endEffector)));
  }
}
