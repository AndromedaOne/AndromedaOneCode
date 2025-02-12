// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class EndEffectorDefaultCommand extends Command {
  private CoralIntakeEjectBase m_CoralIntakeEject;
  private boolean m_useSmartDashboard = false;
  private boolean m_hasCoral = false;

  public EndEffectorDefaultCommand(boolean useSmartDashboard) {
    m_useSmartDashboard = useSmartDashboard;
    m_CoralIntakeEject = Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();
    addRequirements(m_CoralIntakeEject.getSubsystemBase());
  
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}
    // coral always goes through the indefector one way
   // quere sensor for coral > intake until !Intake sensor -> has coral 
   // if has coral > stop
   // if manual mode & has coral & left trig & arm in position & EE in position -> eject coral 
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
