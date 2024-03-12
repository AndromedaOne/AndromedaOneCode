// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AllianceConfig;

public class TurnToTarget extends PIDCommand4905 {
  /* Creates a new TurnToTarget. */
  private int m_wantedID = -1;
  Config pidConfig = Config4905.getConfig4905().getCommandConstantsConfig();

  public TurnToTarget(IntSupplier wantedID, DoubleSupplier setpoint) {
    super(
        // The controller that the command will use
        new PIDController4905SampleStop("TurnToTarget"),
        // This should return the measurement
        Robot.getInstance().getSensorsContainer().getPhotonVision().getYaw(wantedID, setpoint),
        // This should return the setpoint (can also be a constant)
        setpoint,
        // This uses the output
        output -> {
          Robot.getInstance().getSubsystemsContainer().getDriveTrain().move(0, -output, false);
        });
    m_setpoint = setpoint;
    addRequirements(
        Robot.getInstance().getSubsystemsContainer().getDriveTrain().getSubsystemBase());
    // Configure additional PID options by calling `getController` here.

    getController().setP(pidConfig.getDouble("TurnToTarget.TurningPTerm"));
    getController().setI(pidConfig.getDouble("TurnToTarget.TurningITerm"));
    getController().setD(pidConfig.getDouble("TurnToTarget.TurningDTerm"));
    getController().setMinOutputToMove(pidConfig.getDouble("TurnToTarget.minOutputToMove"));
    getController().setTolerance(pidConfig.getDouble("TurnToTarget.positionTolerance"));
    getController().setMaxOutput(0.25);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    // 4 is the middle speaker april tag on red, 8 is blue
    m_wantedID = 4;
    if (alliance == Alliance.Blue) {
      m_wantedID = 8;
    }
    Trace.getInstance().logCommandInfo(this, "Wanted ID:" + m_wantedID);
    Trace.getInstance().logCommandInfo(this, "Setpoint:" + m_setpoint.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandInfo(this,
        "Finish turn angle: " + Robot.getInstance().getSensorsContainer().getPhotonVision()
            .getYaw(() -> m_wantedID, m_setpoint).getAsDouble());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
