package frc.robot.subsystems.billClimber;

import com.revrobotics.CANSparkBase.IdleMode;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.telemetries.Trace;

public class RealBillClimber extends SubsystemBase implements BillClimberBase {
  public SparkMaxController m_winch;
  private double m_initialEncoderWinch = 0;
  private double m_maxExtendHeight = 0;
  private double m_contractHeight = 0;

  public RealBillClimber() {
    Config climberConfig = Config4905.getConfig4905().getBillClimberConfig();
    m_winch = new SparkMaxController(climberConfig, "winch");
    m_initialEncoderWinch = m_winch.getBuiltInEncoderPositionTicks();
    m_maxExtendHeight = climberConfig.getDouble("maxExtendHeight");
    m_contractHeight = climberConfig.getDouble("contractHeight");
  }

  public void periodic() {
    SmartDashboard.putNumber("ClimberHeight", getWinchAdjustedEncoderValue());
    SmartDashboard.putNumber("Raw Climber Height", m_winch.getBuiltInEncoderPositionTicks());
    SmartDashboard.putBoolean("is Limit Switch On", m_winch.isForwardLimitSwitchOn());
  }

  @Override
  public void driveWinch(double speed) {

    // when the speed is greater than 0 the robot is climbing towards the contracted
    // position

    if ((speed > 0) && ((getWinchAdjustedEncoderValue() <= m_contractHeight)
        || m_winch.isForwardLimitSwitchOn())) {
      Trace.getInstance()
          .logInfo("Climb limits have been reached " + speed + " " + m_contractHeight);
      m_winch.setSpeed(0);
    }
    // when the speed is less than 0 then the is robot lowering to the default
    // position
    else if ((speed < 0) && (getWinchAdjustedEncoderValue() >= m_maxExtendHeight)) {
      Trace.getInstance()
          .logInfo("Climb limits have been reached " + speed + " " + m_maxExtendHeight);
      m_winch.setSpeed(0);
    } else {
      m_winch.setSpeed(speed);
    }
  }

  @Override
  public void stopWinch() {
    m_winch.setSpeed(0);
  }

  @Override
  public double getWinchAdjustedEncoderValue() {
    return m_winch.getBuiltInEncoderPositionTicks() - m_initialEncoderWinch;
  }

  @Override
  public void setWinchBrakeMode(boolean brakeOn) {
    if (brakeOn) {
      m_winch.setIdleMode(IdleMode.kBrake);
    } else {
      m_winch.setIdleMode(IdleMode.kCoast);
    }
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void resetOffset() {
    m_initialEncoderWinch = m_winch.getBuiltInEncoderPositionTicks();
  }

}
