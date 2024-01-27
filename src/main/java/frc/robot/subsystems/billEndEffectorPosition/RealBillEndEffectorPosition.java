package frc.robot.subsystems.billEndEffectorPivot;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.subsystems.compressor.CompressorBase;

public class RealBillEndEffectorPivot extends SubsystemBase implements BillEndEffectorPivotBase {
  private Config m_config;
  private DoubleSolenoid4905 m_solenoid0_1;
  private BillEndEffectorState m_endEffectorState = BillEndEffectorState.LOWSHOOTING;

  public RealBillEndEffectorPivot(CompressorBase compressorBase) {
    requireNonNullParam(compressorBase, "compressorBase", "RealEndEffector Constructor");
    m_config = Config4905.getConfig4905().getEndEffectorPivotConfig();
    m_solenoid0_1 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid0_1");
  }

  @Override
  public void initialize() {
    moveLowEndEffector();
    m_endEffectorState = BillEndEffectorState.LOWSHOOTING;

  }

  @Override
  public void moveHighEndEffector() {
    // retracts piston
    m_solenoid0_1.extendPiston();
    System.out.println("extend piston");
    m_endEffectorState = BillEndEffectorState.HIGHSHOOTING;
  }

  @Override
  public void moveLowEndEffector() {
    // extends piston
    m_solenoid0_1.retractPiston();
    System.out.println("retract piston");
    m_endEffectorState = BillEndEffectorState.LOWSHOOTING;
  }

  public BillEndEffectorState getState() {
    return m_endEffectorState;
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Real End Effector state", getState().name());
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
