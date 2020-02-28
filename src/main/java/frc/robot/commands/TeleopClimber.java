/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.BitSet;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.climber.ClimberBase;

public class TeleopClimber extends CommandBase {
  /**
   * Creates a new TeleopClimber.
   */
  private SubsystemController m_subsystemController;
  private DriveController m_driveController;
  private BitSet m_previousSolenoidStates;
  private int m_counter;
  private final int BUFFERSIZE = 10;
  private ClimberBase m_climberBase;

  public TeleopClimber(ClimberBase climberBase) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
    m_previousSolenoidStates = new BitSet(BUFFERSIZE);
    m_counter = 0;
    m_climberBase = climberBase;
    addRequirements(climberBase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_counter = (m_counter + 1) % BUFFERSIZE;
    double percentOpenCycles = m_subsystemController.getRightStickForwardBackwardValue();
    double previousPercentOpenCycles = m_previousSolenoidStates.cardinality() / ((double) BUFFERSIZE);
    boolean openingSolenoid = previousPercentOpenCycles < Math.abs(percentOpenCycles);
    m_previousSolenoidStates.set(m_counter, openingSolenoid);
    if (openingSolenoid) {
      if (percentOpenCycles > 0) {
        m_climberBase.extendLeftArm();
        m_climberBase.extendRightArm();
      } else {
        m_climberBase.retractLeftArm();
        m_climberBase.retractRightArm();
      }
    } else {
      m_climberBase.stopLeftArm();
      m_climberBase.stopRightArm();
    }

    if (m_subsystemController.getLeftTriggerValue() > 0.3) {
      m_climberBase.extendLeftArm();
      m_climberBase.retractRightArm();
    } else if (m_subsystemController.getRightTriggerValue() > 0.3) {
      m_climberBase.retractLeftArm();
      m_climberBase.extendRightArm();
    }
    
    if (m_driveController.getLeftTriggerValue() > 0.3) {
      m_climberBase.driveLeftWinch();
    } else if (m_driveController.getLetOutLeftWinchButton().get()) {
      m_climberBase.adjustLeftWinch(-1);
    } else {
      m_climberBase.stopLeftWinch();
    }

    if (m_driveController.getRightTriggerValue() > 0.3) {
      m_climberBase.driveRightWinch();
    } else if (m_driveController.getLetOutRightWinchButton().get()) {
      m_climberBase.adjustRightWinch(-1);
    } else {
      m_climberBase.stopRightWinch();
    }

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
