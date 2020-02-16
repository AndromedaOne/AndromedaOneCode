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
import frc.robot.subsystems.feeder.FeederStates;

public class DefaultFeederCommand extends CommandBase {

  BallFeederSensorBase m_feederSensor;
  private FeederBase m_feeder;
  private FeederStates m_feederState;
  private static final double DEFAULT_STAGES_ONE_AND_TWO_SPEED = 0.3;
  private static final double DEFAULT_DIFFERENCE_STAGE_TWO_AND_THREE_SPEED = 0.0;
  private static final double DEFAULT_STAGE_THREE_SPEED = DEFAULT_STAGES_ONE_AND_TWO_SPEED
      + DEFAULT_DIFFERENCE_STAGE_TWO_AND_THREE_SPEED;
  private static final double STAGE_TWO_SLOW_SPEED = 0.15;
  private static final double STAGE_THREE_SLOW_SPEED = STAGE_TWO_SLOW_SPEED
      + DEFAULT_DIFFERENCE_STAGE_TWO_AND_THREE_SPEED;
  private FeederStates m_previousState;
  private int emptyCounter = 0;

  /**
   * Creates a new FeederCommand.
   */
  public DefaultFeederCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getInstance().getSubsystemsContainer().getFeeder());
    this.m_feeder = Robot.getInstance().getSubsystemsContainer().getFeeder();
    m_feederSensor = Robot.getInstance().getSensorsContainer().getBallFeederSensor();
    m_feederState = FeederStates.EMPTY;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getInstance().getSubsystemsContainer().getShooter().closeShooterHood();
    m_feederState = FeederStates.EMPTY;
    m_previousState = null;
    emptyCounter = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.getInstance().isDisabled()) {
      m_feederState = FeederStates.EMPTY;
      return;
    }

    boolean[] ballSensorValues = m_feederSensor.isThereBall();
    int sensoredTriggered = 0;

    for (boolean ballSensed : ballSensorValues) {
      if (ballSensed) {
        sensoredTriggered++;
      }
    }
    if (sensoredTriggered == 0) {
      emptyCounter++;
    } else {
      emptyCounter = 0;
    }
    if (emptyCounter >= 5) {
      m_feederState = FeederStates.EMPTY;
    }
    switch (m_feederState) {
    case EMPTY:
      if (ballSensorValues[STAGE_1_LEFT.getIndex()] || ballSensorValues[STAGE_1_RIGHT.getIndex()]
          || ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.ONE_LOADING;
      }
      m_feeder.stopBothStages();
      break;

    case ONE_LOADING:
      if (ballSensorValues[STAGE_2_BEGINNING_MIDDLE.getIndex()] && !ballSensorValues[STAGE_2_BEGINNING.getIndex()]) {
        m_feederState = FeederStates.ONE_LOADED;
      }
      m_feeder.runBothStages(DEFAULT_STAGES_ONE_AND_TWO_SPEED, DEFAULT_STAGE_THREE_SPEED);
      break;

    case ONE_LOADED:
      if (ballSensorValues[STAGE_1_LEFT.getIndex()] || ballSensorValues[STAGE_1_RIGHT.getIndex()]
          || ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.SECOND_LOADING_1;
      }
      m_feeder.stopBothStages();
      break;

    case SECOND_LOADING_1:
      if (ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.SECOND_LOADING_2;
      }
      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.stopStageTwo();
      break;

    case SECOND_LOADING_2:
      if (!ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.SECOND_LOADING_3;
      }
      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.runStagesTwoAndThree(STAGE_TWO_SLOW_SPEED, STAGE_THREE_SLOW_SPEED);
      break;

    case SECOND_LOADING_3:
      if (ballSensorValues[STAGE_2_BEGINNING_MIDDLE.getIndex()] && !ballSensorValues[STAGE_2_BEGINNING.getIndex()]
          && ballSensorValues[STAGE_2_END_MIDDLE.getIndex()]) {
        m_feederState = FeederStates.SECOND_LOADED;
      }
      m_feeder.runBothStages(DEFAULT_STAGES_ONE_AND_TWO_SPEED, DEFAULT_STAGE_THREE_SPEED);
      break;

    case SECOND_LOADED:
      if (ballSensorValues[STAGE_1_LEFT.getIndex()] || ballSensorValues[STAGE_1_RIGHT.getIndex()]
          || ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.THIRD_LOADING_1;
      }
      m_feeder.stopBothStages();
      break;

    case THIRD_LOADING_1:
      if (ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.THIRD_LOADING_2;
      }
      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.stopStageTwo();
      break;

    case THIRD_LOADING_2:
      if (!ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.THIRD_LOADING_3;
      }
      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.runStagesTwoAndThree(STAGE_TWO_SLOW_SPEED, STAGE_THREE_SLOW_SPEED);

    case THIRD_LOADING_3:
      if (ballSensorValues[STAGE_2_BEGINNING.getIndex()] && !ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.THIRD_LOADED;
      }
      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.runStagesTwoAndThree(STAGE_TWO_SLOW_SPEED, STAGE_THREE_SLOW_SPEED);
      break;

    case THIRD_LOADED:
      if (!ballSensorValues[STAGE_2_BEGINNING.getIndex()] && !ballSensorValues[STAGE_2_BEGINNING_MIDDLE.getIndex()]
          && !ballSensorValues[STAGE_1_END.getIndex()]) {
        m_feederState = FeederStates.EMPTY;
      }
      m_feeder.stopBothStages();
      break;

    case UNKNOWN:
      m_feeder.stopBothStages();
      break;
    }
    if (m_previousState != m_feederState) {
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("LOOK HERE -------> m_feederState: " + m_feederState);
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("");
    }
    m_previousState = m_feederState;

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
