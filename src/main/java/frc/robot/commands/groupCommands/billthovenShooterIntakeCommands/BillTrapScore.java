package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
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

public class BillTrapScore extends SequentialCommandGroup4905 {
  private BillArmRotateBase m_armRotate;
  private boolean m_endEffectorToHighPosition = true;
  private double m_shooterSpeed = 0;
  private double m_armSetpoint = 0;
  private boolean m_useSmartDashboard;

  public BillTrapScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, boolean useSmartDashboard) {
    m_armRotate = armRotate;

    m_useSmartDashboard = useSmartDashboard;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("TrapCommand RPM", 1500);
      SmartDashboard.putNumber("TrapCommand ArmPosition", 330);
    }
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);
    ArmRotate runArmCommand = new ArmRotate(m_armRotate, () -> m_armSetpoint, m_useSmartDashboard);
    addCommands(
        new ParallelCommandGroup4905(new ArmRotate(m_armRotate, () -> m_armSetpoint, true),
            new MoveEndEffector(endEffector, () -> m_endEffectorToHighPosition)),
        new ParallelDeadlineGroup4905(
            new RunBillFeeder(feeder, FeederStates.SHOOTING,
                runShooterCommand.getOnTargetSupplier(), runArmCommand.getOnTargetSupplier()),
            runShooterCommand),
        // intentionally leaving with need to end = false to not smash the arm into the
        // stage
        new ArmRotate(m_armRotate, () -> 340, false));
  }

  public BillTrapScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter) {
    this(armRotate, endEffector, feeder, shooter, false);
  }

  @Override
  public void additionalInitialize() {
    // need to determine final values
    // these are going to be our close distance defalt
    m_armSetpoint = 331;
    m_shooterSpeed = 1500;

    if (m_useSmartDashboard) {
      m_armSetpoint = SmartDashboard.getNumber("TrapCommand ArmPosition", 330);
      m_shooterSpeed = SmartDashboard.getNumber("TrapCommand RPM", 1500);

    }
  }

}
