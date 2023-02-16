// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.telemetries.Trace;

public class ToggleBrakes extends CommandBase {
  /** Creates a new ToggleBrakes. */
  DriveTrain m_driveTrain;
  private double m_brakeEngagedValue = 0.0;
  private double m_brakeDisengagedValue = 0.0;

  public ToggleBrakes(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;
    addRequirements(driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Config driveTrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    Trace.getInstance().logCommandStart(this);
    m_brakeDisengagedValue = driveTrainConfig.getDouble("brakedisengage");
    m_brakeEngagedValue = driveTrainConfig.getDouble("brakeengaged");

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESOFF) {
      m_driveTrain.enableParkingBrake(m_brakeEngagedValue);
    } else if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESON) {
      m_driveTrain.disableParkingBrake(m_brakeDisengagedValue);
    } else {
      m_driveTrain.setParkingBrakes(0, 0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
