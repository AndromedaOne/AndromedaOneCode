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
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.climber.ClimberBase;
import frc.robot.telemetries.Trace;

public class ClimberRotationCommand extends SequentialCommandGroup4905 {
  public ClimberRotationCommand(String position) {
    ClimberBase climber = Robot.getInstance().getSubsystemsContainer().getClimber();
    Config climberPIDConfig = Config4905.getConfig4905().getClimberConfig();
    Gyro4905 gyro = Robot.getInstance().getSensorsContainer().getGyro();

    if (!(position.contentEquals("ClimbUp") || position.contentEquals("ClimbDown")
        || position.contentEquals("ClimbUpAutoPosition"))) {
      throw new Error("Error: Unknown climber rotation position: " + position);
    }

    addCommands(new ClimberRotationInternal(climber,
        climberPIDConfig.getDouble(position + ".wantedPosition"),
        climberPIDConfig.getDouble(position + ".maxOutput"), climberPIDConfig, position, gyro));
  }

  private class ClimberRotationInternal extends PIDCommand4905 {
    private ClimberBase m_climber;
    private double m_wantedPosition;
    private double m_maxOutput;
    private Config m_config;
    private String m_position;
    // keep in mind these rotations are 90 degrees higher than what they will
    // actually be. see explanation in RealClimber.
    private double m_minMotorRotation;
    private double m_maxMotorRotation;
    private Gyro4905 m_gyro;

    /**
     * Creates a new MoveUsingEncoder.
     */
    public ClimberRotationInternal(ClimberBase climber, double wantedPosition, double maxOutput,
        Config config, String position, Gyro4905 gyro) {
      super(
          // The controller that the command will use
          new PIDController4905SampleStop(position),
          // This should return the measurement
          // pitch is Y (hopefully.)
          () -> gyro.getYAngle(),
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
      m_gyro = gyro;
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
      m_minMotorRotation = m_config.getDouble(m_position + ".minMotorRotation");
      m_maxMotorRotation = m_config.getDouble(m_position + ".maxMotorRotation");
      Trace.getInstance().logInfo("Going to position " + m_wantedPosition);
      Trace.getInstance().logInfo("Moving to climbing position " + m_position);
      Trace.getInstance().logInfo("Currently at pitch value " + m_gyro.getYAngle());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      // may want to make this finish if climber extender is not
      // in the extended position for safety reasons.
      // may want to store whether robot is moving up or down as a boolean
      // and do something with it here
      // do we have an absolute encoder?????
      // no idea lol
      // getRotatorAngle SHOULD go between 0-360...
      // which is an issue but like uh uh UH
      // so I fixed the issue by making the angles for this... wrong lol
      // i made a method which adds 90, which assuming moving up is pos,
      // will keep everything happy! yay!
      // the only issue is uhhh the min/max needs to be 90 off as well...
      // we could add 90 in the command but yeah idk
      // the issue is the wrap around at 0
      // maybe im being dumb and this isnt an issue. sorry if it isnt.
      // full explanation of my solution in RealClimber's
      // definition of getRotatorWithOffset. it's a doozy.
      if ((m_minMotorRotation > m_climber.getRotatorWithOffset())
          || (m_maxMotorRotation < m_climber.getRotatorWithOffset())) {
        Trace.getInstance().logInfo("ClimberRotationCommand ended by going beyond the limits.");
        Trace.getInstance().logInfo("Gyro Y at end: " + m_gyro.getYAngle());
        Trace.getInstance()
            .logInfo("Non adjusted climber rotation at end: " + m_climber.getRotatorAngle());
        Trace.getInstance()
            .logInfo("Adjusted climber rotation at end: " + m_climber.getRotatorWithOffset());
        return true;
      }
      return getController().atSetpoint();
    }

    public void end(boolean interrupted) {
      super.end(interrupted);
      m_climber.stop();
    }
  }
}
