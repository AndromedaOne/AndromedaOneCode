package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
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

public class BillSpeakerScore extends SequentialCommandGroup4905 {
  public enum SpeakerScoreDistanceEnum {
    CLOSE, MID, FAR
  }

  public enum SpeakerScoreArmPositionEnum {
    LOW, HIGH
  }

  private BillArmRotateBase m_armRotate;
  private SpeakerScoreDistanceEnum m_distance;
  private boolean m_endEffectorToHighPosition = false;
  private double m_shooterSpeed = 0;
  private double m_armSetpoint = 0;
  private boolean m_useSmartDashboard;

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance,
      boolean useSmartDashboard) {
    m_armRotate = armRotate;
    m_distance = distance;
    m_useSmartDashboard = useSmartDashboard;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("ShooterCommand RPM", 3000);
      SmartDashboard.putNumber("ShooterCommand ArmPosition", 300);
    }
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);
    addCommands(
        new ParallelCommandGroup4905(new ArmRotate(m_armRotate, () -> m_armSetpoint, true),
            new MoveEndEffector(endEffector, () -> m_endEffectorToHighPosition)),
        new ParallelDeadlineGroup4905(new RunBillFeeder(feeder, FeederStates.SHOOTING,
            runShooterCommand.getOnTargetSupplier()), runShooterCommand));
  }

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    this(armRotate, endEffector, feeder, shooter, distance, false);
  }

  @Override
  public void additionalInitialize() {
    // need to determine final values
    // these are going to be our close distance defalt
    m_armSetpoint = 300;
    m_shooterSpeed = 1000;
    SpeakerScoreArmPositionEnum armPosition = SpeakerScoreArmPositionEnum.LOW;

    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getBillShootingPositionButton().getAsBoolean()) {
      armPosition = SpeakerScoreArmPositionEnum.HIGH;
    }

    if (m_distance == SpeakerScoreDistanceEnum.MID) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpoint = 315;
        m_shooterSpeed = 3650;
        m_endEffectorToHighPosition = false;
      } else {
        m_armSetpoint = 285;
        m_shooterSpeed = 3650;
        m_endEffectorToHighPosition = true;
      }
    } else if (m_distance == SpeakerScoreDistanceEnum.FAR) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpoint = 306;
        m_shooterSpeed = 3800;
        m_endEffectorToHighPosition = false;
      } else { // not currently used idea of lower power to just chuck notes over
        m_armSetpoint = 300;
        m_shooterSpeed = 1000;
        m_endEffectorToHighPosition = true;
      }
    } else {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpoint = 333;
        m_shooterSpeed = 3250;
        m_endEffectorToHighPosition = false;
      } else { // these are the same because there is no point in shooting high over a defense
               // robot
        m_armSetpoint = 333;
        m_shooterSpeed = 3250;
        m_endEffectorToHighPosition = false;
      }
    }
    if (m_useSmartDashboard) {
      m_armSetpoint = SmartDashboard.getNumber("ShooterCommand ArmPosition", 300);
      m_shooterSpeed = SmartDashboard.getNumber("ShooterCommand RPM", 1000);

    }
  }

}
