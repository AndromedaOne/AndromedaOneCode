/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climberCommands;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.climber.ClimberBase;
import frc.robot.telemetries.Trace;

public class ClimberRetract extends SequentialCommandGroup4905 {
  public ClimberRetract() {
    ClimberBase climber = Robot.getInstance().getSubsystemsContainer().getClimber();
    Config climberPIDConfig = Config4905.getConfig4905().getClimberConfig();

    addCommands(new ClimberRetractInternal(climber,
        climberPIDConfig.getDouble("ClimberRetract.wantedPosition"),
        climberPIDConfig.getDouble("ClimberRetract.maxOutput"), climberPIDConfig));
  }

  private class ClimberRetractInternal extends PIDCommand4905 {
    private ClimberBase m_climber;
    private double m_wantedPosition;
    private double m_maxOutput;
    private Config m_config;

    /**
     * Creates a new MoveUsingEncoder.
     */
    public ClimberRetractInternal(ClimberBase climber, double wantedPosition, double maxOutput,
        Config config) {
      super(
          // The controller that the command will use
          new PIDController4905SampleStop("ClimberRetract"),
          // This should return the measurement
          () -> climber.getExtenderPosition(),
          // This should return the setpoint (can also be a constant)
          () -> wantedPosition,
          // This uses the output
          output -> {
            // Use the output here
            climber.moveExtender(output);
            ;
          });
      m_climber = climber;
      m_wantedPosition = wantedPosition;
      m_config = config;
      addRequirements(m_climber.getSubsystemBase());
    }

    public void initialize() {
      super.initialize();
      getController().setP(m_config.getDouble("ClimberRetract.Kp"));
      getController().setI(m_config.getDouble("ClimberRetract.Ki"));
      getController().setD(m_config.getDouble("ClimberRetract.Kd"));
      getController().setMinOutputToMove(m_config.getDouble("ClimberRetract.minOutputToMove"));
      getController().setTolerance(m_config.getDouble("ClimberRetract.positionTolerance"));
      getController().setIZone(m_config.getDouble("ClimberRetract.iZone"));
      if (m_maxOutput != 0) {
        getController().setMaxOutput(m_maxOutput);
      } else if (m_config.hasPath("ClimberRetract.maxOutput")) {
        getController().setMaxOutput(m_config.getDouble("ClimberRetract.maxOutput"));
      }
      Trace.getInstance().logInfo("Going to position " + m_wantedPosition);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return getController().atSetpoint();
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      m_climber.stop();
    }
  }
}
