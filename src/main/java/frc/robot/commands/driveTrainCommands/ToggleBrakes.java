// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.telemetries.Trace;

public class ToggleBrakes extends CommandBase {
  /** Creates a new ToggleBrakes. */
  DriveTrain m_driveTrain;

  public ToggleBrakes(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;
    addRequirements(driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESOFF) {
      m_driveTrain.enableParkingBrakes();
    } else if (m_driveTrain.getParkingBrakeState() == ParkingBrakeStates.BRAKESON) {
      m_driveTrain.disableParkingBrakes();
    } else {
      // Not sure what value to set the brakes in this case.
      throw (new RuntimeException("Unknown Parking Brake State"));
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
    return true;
  }
}
