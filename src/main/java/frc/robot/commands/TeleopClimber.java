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
  private BitSet m_previousRightSolenoidStates;
  private BitSet m_previousLeftSolenoidStates;
  private int m_counter;
  private final int BUFFERSIZE = 10;
  private ClimberBase m_climberBase;

  public TeleopClimber(ClimberBase climberBase) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
    m_previousRightSolenoidStates = new BitSet(BUFFERSIZE);
    m_previousLeftSolenoidStates = new BitSet(BUFFERSIZE);
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
    double leftRightAdjustmentValue = m_subsystemController.getRightStickLeftRightValue();
    double forwardBackwardStickValue = m_subsystemController.getRightStickForwardBackwardValue();
    forwardBackwardStickValue = (forwardBackwardStickValue < 0) ? -Math.pow(forwardBackwardStickValue, 2)
        : Math.pow(forwardBackwardStickValue, 2);
    double percentRightSolenoidOpenCycles = forwardBackwardStickValue;
    
    leftRightAdjustmentValue = (leftRightAdjustmentValue < 0) ? -Math.pow(leftRightAdjustmentValue, 2)
        : Math.pow(leftRightAdjustmentValue, 2);
    if (leftRightAdjustmentValue < 0) {
      if(percentRightSolenoidOpenCycles > 0){
        percentRightSolenoidOpenCycles -= leftRightAdjustmentValue;
      }else {
        percentRightSolenoidOpenCycles += leftRightAdjustmentValue;
      }

    }
    if (percentRightSolenoidOpenCycles > 0.8) {
      percentRightSolenoidOpenCycles = 1;
    }
    boolean openingRightSolenoid = shouldSolenoidExtend(percentRightSolenoidOpenCycles, m_previousRightSolenoidStates,
        m_counter);

    if (openingRightSolenoid) {
      if (percentRightSolenoidOpenCycles > 0) {
        m_climberBase.extendRightArm();
      } else {
        m_climberBase.retractRightArm();
      }
    } else {
      m_climberBase.stopRightArm();
    }

    double percentLeftSolenoidOpenCycles = forwardBackwardStickValue;
    if (leftRightAdjustmentValue > 0) {
      if(percentLeftSolenoidOpenCycles > 0){
        percentLeftSolenoidOpenCycles += leftRightAdjustmentValue;
      }else {
        percentLeftSolenoidOpenCycles -= leftRightAdjustmentValue;
      }
    }
    if (percentLeftSolenoidOpenCycles > 0.8) {
      percentLeftSolenoidOpenCycles = 1;
    }
    boolean openingLeftSolenoid = shouldSolenoidExtend(percentLeftSolenoidOpenCycles, m_previousLeftSolenoidStates,
        m_counter);
    if (openingLeftSolenoid) {
      if (percentLeftSolenoidOpenCycles > 0) {
        m_climberBase.extendLeftArm();
      } else {
        m_climberBase.retractLeftArm();
      }
    } else {
      m_climberBase.stopLeftArm();
    }

    if (m_driveController.getLeftTriggerValue() > 0.3) {
      m_climberBase.driveLeftWinch();
    } else {
      m_climberBase.stopLeftWinch();
    }

    if (m_driveController.getRightTriggerValue() > 0.3) {
      m_climberBase.driveRightWinch();
    } else {
      m_climberBase.stopRightWinch();
    }
  }

  private boolean shouldSolenoidExtend(double percentSolenoidOpenCycles, BitSet previousSolenoidStates, int counter) {

    double previousSolenoidPercentOpenCycles = previousSolenoidStates.cardinality() / ((double) BUFFERSIZE);
    boolean openingSolenoid = previousSolenoidPercentOpenCycles < Math.abs(percentSolenoidOpenCycles);
    m_previousLeftSolenoidStates.set(counter, openingSolenoid);
    return openingSolenoid;

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
