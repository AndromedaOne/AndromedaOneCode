// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.billthovenShooterIntakeCommands;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.billthovenShooterCommands.RunBillShooterRPM;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AllianceConfig;
import frc.robot.utils.InterpolatingMap;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class BillDistanceSpeakerScore extends SequentialCommandGroup4905 {
  private BillArmRotateBase m_armRotate;
  private BillEndEffectorPositionBase m_endEffector;
  private boolean m_endEffectorToHighPosition = false;
  private double m_shooterSpeed = 0;
  private double m_armSetpoint = 0;
  private int m_wantedID = -1;
  private InterpolatingMap m_shotArmAngleMap;
  private InterpolatingMap m_shotShootingRPMMap;

  /** Creates a new BillDistanceSpeakerScore. */
  public BillDistanceSpeakerScore(BillArmRotateBase armRotate,
      BillEndEffectorPositionBase endEffector, BillFeederBase feeder, BillShooterBase shooter,
      boolean useSmartDashboard) {
    // Why is this a thing
    // It isn't called anywhere
    // Why does it exist
    m_armRotate = armRotate;
    m_endEffector = endEffector;
    m_shotArmAngleMap = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
        "shotArmAngle");

    m_shotShootingRPMMap = new InterpolatingMap(Config4905.getConfig4905().getBillShooterConfig(),
        "shooterMotor.shotShootingRPM");
    RunBillShooterRPM runShooterCommand = new RunBillShooterRPM(shooter, () -> m_shooterSpeed);
    ArmRotate runArmCommand = new ArmRotate(m_armRotate, () -> m_armSetpoint, true);
    // Add the deadline command in the super() call. Add other commands using
    addCommands(new ParallelDeadlineGroup4905(
        new RunBillFeeder(feeder, FeederStates.SHOOTING, runShooterCommand.getOnTargetSupplier(),
            runArmCommand.getOnTargetSupplier()),
        runArmCommand, new MoveEndEffector(m_endEffector, () -> m_endEffectorToHighPosition),
        runShooterCommand,
        new PauseRobot(Robot.getInstance().getSubsystemsContainer().getDriveTrain())));

  }

  @Override
  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    // 4 is the middle speaker april tag on red, 8 is blue
    m_wantedID = 4;
    if (alliance == Alliance.Blue) {
      m_wantedID = 7;
    }
    double measuredDistance = Robot.getInstance().getSensorsContainer().getPhotonVision()
        .getDistanceToTargetInInches(m_wantedID);
    Trace.getInstance().logCommandInfo(this, "Measured Distance: " + measuredDistance);
    m_armSetpoint = m_shotArmAngleMap.getInterpolatedValue(measuredDistance);
    m_shooterSpeed = m_shotShootingRPMMap.getInterpolatedValue(measuredDistance);
    Trace.getInstance().logInfo("Distance: " + measuredDistance);
    Trace.getInstance().logInfo("Arm Setpoint: " + m_armSetpoint);
    Trace.getInstance().logInfo("Shooter Setpoint: " + m_shooterSpeed);
    m_endEffectorToHighPosition = false;
  }
}
