package frc.robot.subsystems.billArmRotate;

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
public class RealBillArmRotate extends SubsystemBase implements BillArmRotateBase {
  private final SparkMaxController m_motor1;
  private SparkAbsoluteEncoder m_armAngleEncoder;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;
  private BillArmBrakeState m_armAngleBrakeState = BillArmBrakeState.ENGAGEARMBRAKE;
  private DoubleSolenoid4905 m_solenoidBrake;

  public RealBillArmRotate(CompressorBase compressorBase) {
    Config armrotateConfig = Config4905.getConfig4905().getArmRotateConfig();
    m_motor1 = new SparkMaxController(armrotateConfig, "motor1");
    // m_solenoidBrake = new DoubleSolenoid4905(compressorBase, armrotateConfig,
    // "solenoidbrake");
    m_armAngleEncoder = m_motor1.getAbsoluteEncoder(Type.kDutyCycle);
    m_maxAngle = armrotateConfig.getDouble("maxAngle");
    m_minAngle = armrotateConfig.getDouble("minAngle");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Measured Arm Angle", getAngle());
    SmartDashboard.putString("Arm Brake State", getBrakeState().toString());
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

  @Override
  public double getAngle() {
    double fixedEncoderValue = (m_armAngleEncoder.getPosition());
    if (fixedEncoderValue >= 1) {
      fixedEncoderValue = fixedEncoderValue - 1;
    }
    return fixedEncoderValue * 360;
  }

  public BillArmBrakeState getBrakeState() {
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

  @Override
  public void disableMotorBrake() {
    m_motor1.setCoastMode();
  }

  @Override
  public void enableMotorBrake() {
    m_motor1.setBrakeMode();
  }

}
