package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToTarget;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.utils.InterpolatingMap;

public class BillSpeakerScore extends SequentialCommandGroup4905 {
  public enum SpeakerScoreDistanceEnum {
    CLOSE, MID, FAR
  }

  public enum SpeakerScoreArmPositionEnum {
    LOW, HIGH
  }

  private BillArmRotateBase m_armRotate;
  private BillEndEffectorPositionBase m_endEffector;
  private SpeakerScoreDistanceEnum m_distance;
  private boolean m_endEffectorToHighPosition = false;
  private double m_shooterSpeed = 0;
  private double m_armSetpoint = 0;
  private boolean m_useSmartDashboard;
  private InterpolatingMap m_shotArmAngleMap;
  private InterpolatingMap m_shotShootingRPMMap;

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance,
      boolean useSmartDashboard) {
    m_armRotate = armRotate;
    m_distance = distance;
    m_endEffector = endEffector;
    m_useSmartDashboard = useSmartDashboard;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("ShooterCommand RPM", 3000);
      SmartDashboard.putNumber("ShooterCommand ArmPosition", 300);
    }
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);
    ArmRotate runArmCommand = new ArmRotate(m_armRotate, () -> m_armSetpoint, m_useSmartDashboard);
    if (distance == SpeakerScoreDistanceEnum.CLOSE) {
      addCommands(new ParallelDeadlineGroup4905(
          new RunBillFeeder(feeder, FeederStates.SHOOTING, runShooterCommand.getOnTargetSupplier(),
              runArmCommand.getOnTargetSupplier()),
          new ArmRotate(m_armRotate, () -> m_armSetpoint, true),
          new MoveEndEffector(m_endEffector, () -> m_endEffectorToHighPosition),
          runShooterCommand));
    } else {
      addCommands(new TurnToTarget(() -> -1, () -> 0), new ParallelDeadlineGroup4905(
          new RunBillFeeder(feeder, FeederStates.SHOOTING, runShooterCommand.getOnTargetSupplier(),
              runArmCommand.getOnTargetSupplier()),
          new ArmRotate(m_armRotate, () -> m_armSetpoint, true),
          new MoveEndEffector(m_endEffector, () -> m_endEffectorToHighPosition), runShooterCommand,
          new PauseRobot(Robot.getInstance().getSubsystemsContainer().getDriveTrain())));
    }
  }

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    this(armRotate, endEffector, feeder, shooter, distance, false);

    m_shotArmAngleMap = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
        "shotArmAngle");

    m_shotShootingRPMMap = new InterpolatingMap(Config4905.getConfig4905().getBillShooterConfig(),
        "shotShootingRPM");
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
        m_armSetpoint = 320;
        m_shooterSpeed = 3650;
        m_endEffectorToHighPosition = false;
      } else {
        m_armSetpoint = 290;
        m_shooterSpeed = 3650;
        m_endEffectorToHighPosition = true;
      }
    } else if (m_distance == SpeakerScoreDistanceEnum.FAR) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) { // now from the podium
        m_armSetpoint = 315;
        m_shooterSpeed = 3650;
        m_endEffectorToHighPosition = false;
      } else {
        m_armSetpoint = 285;
        m_shooterSpeed = 3650;
        m_endEffectorToHighPosition = true;
      }
    } else {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpoint = 332;
        m_shooterSpeed = 3250;
        m_endEffectorToHighPosition = false;
      } else { // these are the same because there is no point in shooting high over a defense
               // robot
        m_armSetpoint = 332;
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
