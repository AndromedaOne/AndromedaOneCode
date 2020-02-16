/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import static frc.robot.sensors.ballfeedersensor.EnumBallLocation.*;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;

public class DefaultFeederCommand extends CommandBase {

  BallFeederSensorBase m_feederSensor;
  FeederBase m_feeder;
  private int m_reverseCounter;
  private int m_readyForStage2Counter;

  /**
   * Creates a new FeederCommand.
   */
  public DefaultFeederCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getInstance().getSubsystemsContainer().getFeeder());
    this.m_feeder = Robot.getInstance().getSubsystemsContainer().getFeeder();
    m_feederSensor = Robot.getInstance().getSensorsContainer().getBallFeederSensor();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getInstance().getSubsystemsContainer().getShooter().closeShooterHood();
    m_reverseCounter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /*
     * If there's nothing in stage one OR if there's a ball at the end of stage two,
     * don't run the feeder Additionally, covers when there is a ball in the 3rd
     * slot of stage 2 but not in the first two, and covers when there is a ball in
     * the middle but not in the other two.
     */
    if(m_feederSensor.isBall(STAGE_2_END) && m_feederSensor.isBall(STAGE_2_END_MIDDLE) && m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE)) {
      System.out.println("Stopping both stages");
      m_feeder.stopBothStages();
      m_reverseCounter = 0;
      m_readyForStage2Counter = 0;
    }else if(!m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE) && (m_feederSensor.isBall(STAGE_2_END) || m_feederSensor.isBall(STAGE_2_END_MIDDLE))) {
      if(m_reverseCounter >= 10){
        m_feeder.runReverseStageTwo();
        System.out.println("Running stage 2 reverse");
      }
      m_feeder.stopStageOne();
      m_reverseCounter++;
      
      m_readyForStage2Counter = 0;
    }else if(m_feederSensor.isBall(STAGE_2_BEGINNING) && !m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE)){
      m_feeder.driveBothStages();
      System.out.println("Driving Stage 2 ");
      m_readyForStage2Counter = 0;;
      m_reverseCounter =0;
    }else if(m_feederSensor.isBall(STAGE_1_END)){
      System.out.println("Driving Stage 1 ");
      m_feeder.driveStageOne();
      m_readyForStage2Counter = 0;;
      m_reverseCounter =0;
    }else if(m_feederSensor.isBall(STAGE_1_LEFT) || m_feederSensor.isBall(STAGE_1_RIGHT)) {
      System.out.println("Driving just stage 1");
      m_feeder.driveStageOne();
      m_feeder.stopStageTwo();
      m_reverseCounter = 0;
      m_readyForStage2Counter = 0;
    }else {
      System.out.println("Stopping both stages");
      m_feeder.stopBothStages();
      m_reverseCounter = 0;
      m_readyForStage2Counter = 0;
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_feeder.stopBothStages();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
