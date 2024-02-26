package frc.robot.subsystems.billClimber;

import com.revrobotics.CANSparkBase.IdleMode;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealBillClimber extends SubsystemBase implements BillClimberBase {
  public SparkMaxController m_winch;
  private double m_encoderOffset = 0;
  private double m_maxHeight = 0;
  private double m_minHeight = 0;

  public RealBillClimber() {
    Config climberConfig = Config4905.getConfig4905().getBillClimberConfig();
    m_winch = new SparkMaxController(climberConfig, "winch");
    m_encoderOffset = m_winch.getBuiltInEncoderPositionTicks();
    m_maxHeight = climberConfig.getDouble("maxHeight");
    m_minHeight = climberConfig.getDouble("minHeight");
  }

  public void periodic() {
    SmartDashboard.putNumber("ClimberHeight", getWinchAdjustedEncoderValue());
    SmartDashboard.putNumber("Raw Climber Height", m_winch.getBuiltInEncoderPositionTicks());
    SmartDashboard.putBoolean("is Limit Switch On", m_winch.isReverseLimitSwitchOn());
  }

  @Override
  public void driveWinch(double speed, boolean override) {

    // when the speed is greater than 0 the robot is climbing towards the contracted
    // position

    if ((speed > 0) && (getWinchAdjustedEncoderValue() >= m_maxHeight) && !override) {
      m_winch.setSpeed(0);
    }
    // when the speed is less than 0 then the is robot lowering to the default
    // position
    else if ((speed < 0)
        && ((getWinchAdjustedEncoderValue() <= m_minHeight) || m_winch.isReverseLimitSwitchOn())) {
      m_winch.setSpeed(0);
    } else if ((speed > 0) && (getWinchAdjustedEncoderValue() + 30 >= m_maxHeight)) {
      // Slows it down while going up if it is close to the top
      m_winch.setSpeed(speed / 2);
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
    return m_winch.getBuiltInEncoderPositionTicks() - m_encoderOffset;
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
    m_encoderOffset = m_winch.getBuiltInEncoderPositionTicks();
  }
}