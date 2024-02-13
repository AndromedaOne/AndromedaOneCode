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

  public RealBillClimber() {
    Config climberConfig = Config4905.getConfig4905().getBillClimberConfig();
    m_winch = new SparkMaxController(climberConfig, "winch");
    m_initialEncoderWinch = m_winch.getBuiltInEncoderPositionTicks();
  }

  public void periodic() {
    SmartDashboard.putNumber("ClimberHeight", getWinchAdjustedEncoderValue());
  }

  @Override
  public void driveWinch(double speed) {
    m_winch.setSpeed(speed);

  }

  @Override
  public void stopWinch() {
    m_winch.setSpeed(0);
  }

  @Override
  public void unwindWinch() {
    m_winch.setSpeed(-1);

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

}
