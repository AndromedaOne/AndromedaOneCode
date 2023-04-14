// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.encoder.EncoderBase;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.subsystems.showBotCannonElevator.CannonElevatorBase;
import frc.robot.telemetries.Trace;

public class AdjustElevation extends SequentialCommandGroup4905 {
  private CannonElevatorBase m_cannonElevator;
  private EncoderBase m_cannonElevatorEncoder;
  private LimitSwitchSensor m_cannonHomeSwitch;
  private static boolean m_initialized = false;
  private static double m_encoderOffset = 0;
  private static final double m_maxElevation = 380;

  public AdjustElevation(CannonElevatorBase cannonElevator) {
    m_cannonElevator = cannonElevator;
    m_cannonElevatorEncoder = Robot.getInstance().getSensorsContainer().getCannonElevatorEncoder();
    m_cannonHomeSwitch = Robot.getInstance().getSensorsContainer().getCannonHomeSwitch();
    addCommands(new InitializeElevation(), new AdjustElevationInternal(cannonElevator));
  }

  @Override
  public void additionalInitialize() {
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  public double getElevation() {
    return m_cannonElevatorEncoder.getEncoderValue() - m_encoderOffset;
  }

  private class InitializeElevation extends CommandBase {

    public InitializeElevation() {
      addRequirements(m_cannonElevator);
    }

    @Override
    public void initialize() {
      Trace.getInstance().logCommandStart(this);
      if (!m_initialized && !m_cannonHomeSwitch.isAtLimit()) {
        throw new RuntimeException(
            "CannonElevator cannot be initialized. The home switch is not triggered. Please rotate the cannon so the switch is engaged.");
      }
    }

    @Override
    public void execute() {
      if (!m_initialized) {
        if (m_cannonHomeSwitch.isAtLimit()) {
          m_cannonElevator.changeElevation(-0.25);
        } else {
          m_initialized = true;
          m_encoderOffset = m_cannonElevatorEncoder.getEncoderValue() + 50;
        }
      }
    }

    @Override
    public boolean isFinished() {
      return m_initialized;
    }

    @Override
    public void end(boolean interrupted) {
      Trace.getInstance().logCommandStop(this);
    }
  }

  private class AdjustElevationInternal extends CommandBase {
    /** Creates a new AdjustElevation. */
    private CannonElevatorBase m_cannonElevator;

    public AdjustElevationInternal(CannonElevatorBase cannonElevator) {
      m_cannonElevator = cannonElevator;
      addRequirements(m_cannonElevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      Trace.getInstance().logCommandStart(this);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      double upSpeed = Robot.getInstance().getOIContainer().getDriveController()
          .getShowBotElevatorUpTriggerValue();
      double downSpeed = Robot.getInstance().getOIContainer().getDriveController()
          .getShowBotElevatorDownTriggerValue();
      double speed = 0;
      if ((upSpeed > 0) && (getElevation() <= m_maxElevation)) {
        speed = upSpeed;
      } else if ((downSpeed > 0) && (getElevation() >= 0)) {
        speed = -downSpeed;
      }
      speed *= 0.25;
      m_cannonElevator.changeElevation(speed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      Trace.getInstance().logCommandStop(this);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }
}
