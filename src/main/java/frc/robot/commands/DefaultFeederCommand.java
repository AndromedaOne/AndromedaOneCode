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
  FeederBase m_feeder;
  private FeederStates m_feederState;
  private static final double DEFAULT_STAGES_ONE_AND_TWO_SPEED = 0.25;
  private static final double DEFAULT_STAGE_THREE_SPEED = 0.35;

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
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_feederState = getCurrentFeederState();
    switch (m_feederState) {
    case EMPTY:

      m_feeder.stopBothStages();
      break;

    case ONE_LOADING:

      m_feeder.runBothStages(DEFAULT_STAGES_ONE_AND_TWO_SPEED, DEFAULT_STAGE_THREE_SPEED);
      break;

    case ONE_LOADED:

      m_feeder.stopBothStages();
      break;

    case SECOND_LOADING_1:

      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.stopStageTwo();
      break;

    case SECOND_LOADING_2:

      m_feeder.runBothStages(DEFAULT_STAGES_ONE_AND_TWO_SPEED, DEFAULT_STAGE_THREE_SPEED);
      break;

    case SECOND_LOADED:

      m_feeder.stopBothStages();
      break;

    case THIRD_LOADING_1:

      m_feeder.runStageOne(DEFAULT_STAGES_ONE_AND_TWO_SPEED);
      m_feeder.stopStageTwo();
      break;

    case THIRD_LOADING_2:

      m_feeder.runBothStages(DEFAULT_STAGES_ONE_AND_TWO_SPEED, DEFAULT_STAGE_THREE_SPEED);
      break;

    case THIRD_LOADED:

      m_feeder.stopBothStages();
      break;

    case UNKNOWN:
      m_feeder.stopBothStages();
      break;
    }

    System.out.println("m_feederState: " + m_feederState);

  }

  private FeederStates getCurrentFeederState() {
    FeederStates feederState = FeederStates.UNKNOWN;

    if (m_feederSensor.isBall(STAGE_2_BEGINNING) && m_feederSensor.isBall(STAGE_2_END_MIDDLE)
        && m_feederSensor.isBall(STAGE_2_END) && !m_feederSensor.isBall(STAGE_1_END)) {
      feederState = FeederStates.THIRD_LOADED;

    } else if (m_feederSensor.isBall(STAGE_2_BEGINNING) && inTwoBallsLoadedEndingState()) {
      feederState = FeederStates.THIRD_LOADING_2;

    } else if (isABallInStageOne() && inTwoBallsLoadedEndingState()) {
      feederState = FeederStates.THIRD_LOADING_1;

    } else if (inTwoBallsLoadedEndingState() && !m_feederSensor.isBall(STAGE_2_BEGINNING)) {
      feederState = FeederStates.SECOND_LOADED;

    } else if (m_feederSensor.isBall(STAGE_2_BEGINNING) && inOneBallLoadedEndingState()) {
      feederState = FeederStates.SECOND_LOADING_2;

    } else if (isABallInStageOne() && inOneBallLoadedEndingState()) {
      feederState = FeederStates.SECOND_LOADING_1;

    } else if (inOneBallLoadedEndingState() && !m_feederSensor.isBall(STAGE_2_BEGINNING)) {
      feederState = FeederStates.ONE_LOADED;

    } else if (isABallInStageOne()) {
      feederState = FeederStates.ONE_LOADING;
    } else if (!isABallInStageOne() && !isABallInStageTwo()) {
      feederState = FeederStates.EMPTY;
    } else {
      feederState = FeederStates.UNKNOWN;
    }

    return feederState;
  }

  // In two balls loaded ending state, when one ball is in stage 2 beginning
  // middle
  // and another ball is in stage 2 end middle
  private boolean inTwoBallsLoadedEndingState() {
    return m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE) && m_feederSensor.isBall(STAGE_2_END_MIDDLE);
  }

  private boolean inOneBallLoadedEndingState() {
    return m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE);
  }

  private boolean isABallInStageOne() {
    return m_feederSensor.isBall(STAGE_1_LEFT) || m_feederSensor.isBall(STAGE_1_RIGHT)
        || m_feederSensor.isBall(STAGE_2_BEGINNING);
  }

  private boolean isABallInStageTwo() {
    return m_feederSensor.isBall(STAGE_2_BEGINNING) || m_feederSensor.isBall(STAGE_2_BEGINNING_MIDDLE)
        || m_feederSensor.isBall(STAGE_2_END_MIDDLE) || m_feederSensor.isBall(STAGE_2_END);
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
