package frc.robot.subsystems.billArmPivot;

import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.subsystems.compressor.CompressorBase;

//This was just copied over from RealSamArmRotate so it will need editing
public class RealBillArmPivot extends SubsystemBase implements BillArmPivotBase {
  private final SparkMaxController m_motor1;
  private SparkAbsoluteEncoder m_armAngleEncoder;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;
  private BillArmBrakeState m_armAngleBrakeState = BillArmBrakeState.ENGAGEARMBRAKE;
  private DoubleSolenoid4905 m_solenoidBrake;

  public RealBillArmPivot(CompressorBase compressorBase) {
    Config armrotateConfig = Config4905.getConfig4905().getArmPivotConfig();
    m_motor1 = new SparkMaxController(armrotateConfig, "motor1");
    m_solenoidBrake = new DoubleSolenoid4905(compressorBase, armrotateConfig, "solenoidbrake");
    m_armAngleEncoder = m_motor1.getAbsoluteEncoder(Type.kDutyCycle);
    m_maxAngle = armrotateConfig.getDouble("maxAngle");
    m_minAngle = armrotateConfig.getDouble("minAngle");

  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("arm angle", getAngle());
    SmartDashboard.putString("Arm Brake State", getState().toString());
  }

  @Override
  public void rotate(double speed) {
    if (m_armAngleBrakeState == BillArmBrakeState.ENGAGEARMBRAKE) {
      m_motor1.setSpeed(0);
      return;
    }
    if ((speed < 0) && (getAngle() <= m_minAngle)) {
      m_motor1.setSpeed(0);
    } else if ((speed > 0) && (getAngle() >= m_maxAngle)) {
      m_motor1.setSpeed(0);
    } else {
      m_motor1.setSpeed(speed * 0.63);
    }
  }

  @Override
  public void engageArmBrake() {
    m_solenoidBrake.retractPiston();
    m_armAngleBrakeState = BillArmBrakeState.ENGAGEARMBRAKE;
    m_motor1.setSpeed(0);
  }

  @Override
  public void disengageArmBrake() {
    m_solenoidBrake.extendPiston();
    m_armAngleBrakeState = BillArmBrakeState.DISENGAGEARMBRAKE;
  }

  // 90 Degrees is pointing forward, 270 is pointing backwards
  @Override
  public double getAngle() {
    double fixedEncoderValue = (1.56 - m_armAngleEncoder.getPosition());
    if (fixedEncoderValue >= 1) {
      fixedEncoderValue = fixedEncoderValue - 1;
    }
    return fixedEncoderValue * 360;
  }

  public BillArmBrakeState getState() {
    return m_armAngleBrakeState;
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
  public void stop() {
    rotate(0);
  }

}