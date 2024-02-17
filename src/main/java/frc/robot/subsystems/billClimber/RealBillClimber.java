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
  private double m_initialEncoderWinch = 0;
  private double m_maxHeight = 0;
  private double m_contractHeight = 0;
  private BillClimberBrakeState brakeState = BillClimberBrakeState.ENGAGECLIMBERBRAKE;

  public RealBillClimber() {
    Config climberConfig = Config4905.getConfig4905().getBillClimberConfig();
    m_winch = new SparkMaxController(climberConfig, "winch");
    m_initialEncoderWinch = m_winch.getBuiltInEncoderPositionTicks();
    m_maxHeight = climberConfig.getDouble("maxExtendHeight");
    m_contractHeight = climberConfig.getDouble("contractHeight");
  }

  public void periodic() {
    SmartDashboard.putNumber("ClimberHeight", getWinchAdjustedEncoderValue());
    SmartDashboard.putNumber("Raw Climber Height", m_winch.getEncoderPositionTicks());
  }

  @Override
  public void driveWinch(double speed) {
    if (brakeState == BillClimberBrakeState.ENGAGECLIMBERBRAKE) {
      m_winch.setSpeed(0);
      return;
    }
    if ((speed < 0) && (getWinchAdjustedEncoderValue() <= m_contractHeight)) {
      m_winch.setSpeed(0);
    } else if ((speed > 0) && (getWinchAdjustedEncoderValue() >= m_maxHeight)) {
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
  public void unwindWinch() {
    driveWinch(-1);

  }

  @Override
  public double getWinchAdjustedEncoderValue() {
    return m_winch.getBuiltInEncoderPositionTicks() - m_initialEncoderWinch;
  }

  @Override
  public void setWinchBrakeMode(boolean brakeOn) {
    if (brakeOn) {
      brakeState = BillClimberBrakeState.ENGAGECLIMBERBRAKE;
      m_winch.setIdleMode(IdleMode.kBrake);
    } else {
      brakeState = BillClimberBrakeState.DISENGAGECLIMBERBRAKE;
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

}
