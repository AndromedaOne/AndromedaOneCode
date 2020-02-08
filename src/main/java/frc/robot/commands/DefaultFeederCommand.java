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

  /**
   * Creates a new FeederCommand.
   */
  public DefaultFeederCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getInstance().getSubsystemsContainer().getFeeder());
    this.m_feeder = Robot.getInstance().getSubsystemsContainer().getFeeder();
    m_feederSensor = Robot.getInstance().getSensorsContainer().ballFeederSensor;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /*
     * If there's nothing in stage one OR if there's a ball at the end of stage two,
     * don't run the feeder
     */
    if ((!m_feederSensor.isBall(STAGE_1_LEFT) && !m_feederSensor.isBall(STAGE_1_RIGHT)
        && !m_feederSensor.isBall(STAGE_1_END)) || m_feederSensor.isBall(STAGE_2_END)) {
      m_feeder.stopBothStages();
    } else if (!m_feederSensor.isBall(STAGE_1_END)
        && (m_feederSensor.isBall(STAGE_1_LEFT) || m_feederSensor.isBall(STAGE_1_RIGHT))) {
      m_feeder.driveStageOne();
      m_feeder.stopStageTwo();
    } else if (m_feederSensor.isBall(STAGE_1_END)) {
      m_feeder.driveBothStages();
    } else if ((m_feederSensor.isBall(STAGE_2_MIDDLE) && !m_feederSensor.isBall(STAGE_2_BEGINNING))
        || (m_feederSensor.isBall(STAGE_2_END) && !m_feederSensor.isBall(STAGE_2_MIDDLE))) {
      m_feeder.runReverseStageTwo();
    }

    System.out.println(m_feederSensor.isBall(STAGE_1_LEFT) + " " + m_feederSensor.isBall(STAGE_1_END));
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
