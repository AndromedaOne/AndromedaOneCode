package frc.robot.commands.climber;

import java.util.BitSet;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.climber.ClimberBase;

public class TeleopClimber extends CommandBase {
  private ClimberBase m_climberBase;
  private SubsystemController m_subsystemController;
  private DriveController m_driveController;
  private int m_counter;
  private BitSet m_previousSolenoidStates;
  private final int BUFFERSIZE = 10;

  public TeleopClimber(ClimberBase climberBase) {
    m_climberBase = climberBase;
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    // m_driveController =
    // Robot.getInstance().getOIContainer().getdriveController();
    m_counter = 0;
    m_previousSolenoidStates = new BitSet(BUFFERSIZE);
    addRequirements(climberBase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}