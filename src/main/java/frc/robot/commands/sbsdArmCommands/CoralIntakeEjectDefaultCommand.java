// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class CoralIntakeEjectDefaultCommand extends Command {
  private CoralIntakeEjectBase m_coralIntakeEject;
  private DriveController m_driveController;
  private SubsystemController m_subsystemController;

  public CoralIntakeEjectDefaultCommand(boolean useSmartDashboard) {
    m_coralIntakeEject = Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    addRequirements(m_coralIntakeEject.getSubsystemBase());

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_coralIntakeEject.setDriveController(m_driveController);
    m_coralIntakeEject.setSubsystemController(m_subsystemController);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_subsystemController.getManualEject()) {
      m_coralIntakeEject.setEjectState();
    }
    m_coralIntakeEject.runWheelsIntake();
  }

  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
