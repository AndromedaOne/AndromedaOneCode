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

public class ClimberRotationCommand extends SequentialCommandGroup4905 {
  public ClimberRotationCommand(String position) {
    ClimberBase climber = Robot.getInstance().getSubsystemsContainer().getClimber();
    Config climberPIDConfig = Config4905.getConfig4905().getClimberConfig();

    if (!(position.contentEquals("ClimbUp") || position.contentEquals("ClimbDown")
        || position.contentEquals("ClimbUpAutoPosition"))) {
      throw new Error("Error: Unknown climber rotation position: " + position);
    }

    addCommands(new ClimberRotationInternal(climber,
        climberPIDConfig.getDouble(position + ".wantedPosition"),
        climberPIDConfig.getDouble(position + ".maxOutput"), climberPIDConfig, position));
  }

  private class ClimberRotationInternal extends PIDCommand4905 {
    private ClimberBase m_climber;
    private double m_wantedPosition;
    private double m_maxOutput;
    private Config m_config;
    private String m_position;

    /**
     * Creates a new MoveUsingEncoder.
     */
    public ClimberRotationInternal(ClimberBase climber, double wantedPosition, double maxOutput,
        Config config, String position) {
      super(
          // The controller that the command will use
          new PIDController4905SampleStop(position),
          // This should return the measurement
          () -> climber.getRotatorAngle(),
          // This should return the setpoint (can also be a constant)
          () -> wantedPosition,
          // This uses the output
          output -> {
            // Use the output here
            climber.rotateRotator(output);
            ;
          });
      m_climber = climber;
      m_wantedPosition = wantedPosition;
      m_config = config;
      m_position = position;
      m_maxOutput = maxOutput;
      addRequirements(m_climber.getSubsystemBase());
    }

    public void initialize() {
      super.initialize();
      getController().setP(m_config.getDouble(m_position + ".Kp"));
      getController().setI(m_config.getDouble(m_position + ".Ki"));
      getController().setD(m_config.getDouble(m_position + ".Kd"));
      getController().setMinOutputToMove(m_config.getDouble(m_position + ".minOutputToMove"));
      getController().setTolerance(m_config.getDouble(m_position + ".positionTolerance"));
      getController().setIZone(m_config.getDouble(m_position + ".iZone"));
      if (m_maxOutput != 0) {
        getController().setMaxOutput(m_maxOutput);
      } else if (m_config.hasPath(m_position + ".maxOutput")) {
        getController().setMaxOutput(m_config.getDouble(m_position + ".maxOutput"));
      }
      Trace.getInstance().logInfo("Going to position " + m_wantedPosition);
      Trace.getInstance().logInfo("Moving to climbing position " + m_position);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      // may want to make this finish if climber extender is not
      // in the extended position for safety reasons.
      // may want to store whether robot is moving up or down as a boolean
      // and do something with it here
      return getController().atSetpoint();
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      m_climber.stop();
    }
  }
}
