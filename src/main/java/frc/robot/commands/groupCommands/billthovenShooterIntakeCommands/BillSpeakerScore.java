package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.driveTrainCommands.TurnToTargetUsingGyro;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AllianceConfig;
import frc.robot.utils.InterpolatingMap;

public class BillSpeakerScore extends SequentialCommandGroup4905 {
  public enum SpeakerScoreDistanceEnum {
    CLOSE, AWAY, SHUTTLE
  }

  public enum SpeakerScoreArmPositionEnum {
    LOW, HIGH
  }

  private BillArmRotateBase m_armRotate;
  private BillEndEffectorPositionBase m_endEffector;
  private DriveTrainBase m_driveTrain;
  private PhotonVisionBase m_photonVision;
  private SpeakerScoreDistanceEnum m_distance;
  private boolean m_endEffectorToHighPosition = false;
  private double m_shooterSpeed = 0;
  private double m_armSetpoint = 0;
  private int m_wantedID = -1;
  private boolean m_useSmartDashboard;
  private InterpolatingMap m_shotArmAngleMap;
  private InterpolatingMap m_shotShootingRPMMap;
  private double m_shuttleAngle = 330;

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance,
      boolean useSmartDashboard) {
    m_armRotate = armRotate;
    m_distance = distance;
    m_endEffector = endEffector;
    m_useSmartDashboard = useSmartDashboard;
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    m_photonVision = Robot.getInstance().getSensorsContainer().getPhotonVision();
    if (useSmartDashboard) {
      SmartDashboard.putNumber("ShooterCommand RPM", 3000);
      SmartDashboard.putNumber("ShooterCommand ArmPosition", 300);
    }
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);
    ArmRotate runArmCommand = new ArmRotate(m_armRotate, () -> m_armSetpoint, true);
    if (distance == SpeakerScoreDistanceEnum.AWAY) {
      addCommands(
          new TurnToTargetUsingGyro(m_driveTrain, () -> m_wantedID, () -> 0, false, m_photonVision),
          new ParallelDeadlineGroup4905(
              new RunBillFeeder(feeder, FeederStates.SHOOTING,
                  runShooterCommand.getOnTargetSupplier(), runArmCommand.getOnTargetSupplier()),
              runArmCommand, new MoveEndEffector(m_endEffector, () -> m_endEffectorToHighPosition),
              runShooterCommand,
              new PauseRobot(Robot.getInstance().getSubsystemsContainer().getDriveTrain())));
    } else if (distance == SpeakerScoreDistanceEnum.SHUTTLE) {
      addCommands(new TurnToCompassHeading(() -> m_shuttleAngle),
          new ParallelDeadlineGroup4905(
              new RunBillFeeder(feeder, FeederStates.SHOOTING,
                  runShooterCommand.getOnTargetSupplier(), runArmCommand.getOnTargetSupplier()),
              runArmCommand, new MoveEndEffector(m_endEffector, () -> m_endEffectorToHighPosition),
              runShooterCommand, new PauseRobot(m_driveTrain)));
    } else {
      addCommands(new ParallelDeadlineGroup4905(
          new RunBillFeeder(feeder, FeederStates.SHOOTING, runShooterCommand.getOnTargetSupplier(),
              runArmCommand.getOnTargetSupplier()),
          runArmCommand, new MoveEndEffector(m_endEffector, () -> m_endEffectorToHighPosition),
          runShooterCommand));
    }
  }

  public BillSpeakerScore(BillArmRotateBase armRotate, BillEndEffectorPositionBase endEffector,
      BillFeederBase feeder, BillShooterBase shooter, SpeakerScoreDistanceEnum distance) {
    this(armRotate, endEffector, feeder, shooter, distance, false);

    m_shotArmAngleMap = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
        "shotArmAngle");

    m_shotShootingRPMMap = new InterpolatingMap(Config4905.getConfig4905().getBillShooterConfig(),
        "shooterMotor.shotShootingRPM");
  }

  @Override
  public void additionalInitialize() {
    // need to determine final values
    // these are going to be our close distance defalt
    m_armSetpoint = 300;
    m_shooterSpeed = 1000;
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    // 4 is the middle speaker april tag on red, 8 is blue
    m_wantedID = 4;
    m_shuttleAngle = 330; // should be 330 once it works
    if (alliance == Alliance.Blue) {
      m_wantedID = 7;
      m_shuttleAngle = 30; // should be 30 once it works
    }
    SpeakerScoreArmPositionEnum armPosition = SpeakerScoreArmPositionEnum.LOW;

    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getBillShootingPositionButton().getAsBoolean()) {
      armPosition = SpeakerScoreArmPositionEnum.HIGH;
    }
    if (m_distance == SpeakerScoreDistanceEnum.CLOSE) {
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
    } else if (m_distance == SpeakerScoreDistanceEnum.SHUTTLE) {
      if (armPosition == SpeakerScoreArmPositionEnum.LOW) {
        m_armSetpoint = 300;
        m_shooterSpeed = 4000;
        m_endEffectorToHighPosition = true;
      } else {
        m_armSetpoint = 300;
        m_shooterSpeed = 4000;
        m_endEffectorToHighPosition = true;
      }
    } else {
      double measuredDistance = m_photonVision.getDistanceToTargetInInches(m_wantedID);
      m_armSetpoint = m_shotArmAngleMap.getInterpolatedValue(measuredDistance);
      m_shooterSpeed = m_shotShootingRPMMap.getInterpolatedValue(measuredDistance);
      Trace.getInstance().logInfo("Distance: " + measuredDistance);
      Trace.getInstance().logInfo("Arm Setpoint: " + m_armSetpoint);
      Trace.getInstance().logInfo("Shooter Setpoint: " + m_shooterSpeed);
      m_endEffectorToHighPosition = false;
    }

    if (m_useSmartDashboard) {
      m_armSetpoint = SmartDashboard.getNumber("ShooterCommand ArmPosition", 300);
      m_shooterSpeed = SmartDashboard.getNumber("ShooterCommand RPM", 1000);

    }
  }

}
