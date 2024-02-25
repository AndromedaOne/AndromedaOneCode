package frc.robot.subsystems.billEndEffectorPosition;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.subsystems.compressor.CompressorBase;

public class RealBillEndEffectorPosition extends SubsystemBase
    implements BillEndEffectorPositionBase {
  private Config m_config;
  private DoubleSolenoid4905 m_solenoid0_1;
  private BillEndEffectorState m_endEffectorState = BillEndEffectorState.LOWSHOOTING;

  public RealBillEndEffectorPosition(CompressorBase compressorBase) {
    requireNonNullParam(compressorBase, "compressorBase", "RealEndEffector Constructor");
    m_config = Config4905.getConfig4905().getEndEffectorPositionConfig();
    m_solenoid0_1 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid0_1");
  }

  @Override
  public void moveHighEndEffector() {
    m_solenoid0_1.extendPiston();
    m_endEffectorState = BillEndEffectorState.HIGHSHOOTING;
  }

  @Override
  public void moveLowEndEffector() {
    m_solenoid0_1.retractPiston();
    m_endEffectorState = BillEndEffectorState.LOWSHOOTING;
  }

  public BillEndEffectorState getState() {
    return m_endEffectorState;
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Real End Effector state", getState().toString());
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
