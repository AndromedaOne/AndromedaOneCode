package frc.robot.subsystems.billArmRotate;

import com.typesafe.config.Config;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.subsystems.compressor.CompressorBase;

//This was just copied over from RealSamArmRotate so it will need editing
public class RealBillArmRotate extends ProfiledPIDSubsystem implements BillArmRotateBase {
  private final SparkMaxController m_motor1;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;
  private double m_Ks = 0; 
  private double m_Kg = 0; 
  private double m_Kv = 0; 
  private BillArmBrakeState m_armAngleBrakeState = BillArmBrakeState.ENGAGEARMBRAKE;
  private DoubleSolenoid4905 m_solenoidBrake;

  public RealBillArmRotate(CompressorBase compressorBase) {
    super(
        new ProfiledPIDController(0,0,0,
        new TrapezoidProfile.Constraints(0,0)));
    // Start arm at rest in neutral position
    setGoal(0);
        new ArmFeedforward(m_Ks, m_Kg, m_Kv);
    Config armrotateConfig = Config4905.getConfig4905().getArmRotateConfig();
    m_motor1 = new SparkMaxController(armrotateConfig, "motor1");
    m_solenoidBrake = new DoubleSolenoid4905(compressorBase, armrotateConfig, "solenoidbrake");
    m_maxAngle = armrotateConfig.getDouble("maxAngle");
    m_minAngle = armrotateConfig.getDouble("minAngle");
    m_Ks = armrotateConfig.getDouble("Ks");
    m_Kg = armrotateConfig.getDouble("Kg");
    m_Kv = armrotateConfig.getDouble("Kv");
  }

  @Override
  public void periodic() {
    super.periodic();
    SmartDashboard.putNumber("Measured Arm Angle", getAngle());
    SmartDashboard.putString("Arm Brake State", getBrakeState().toString());
  }

  @Override
  public void rotate(double speed) {
    if (m_armAngleBrakeState == BillArmBrakeState.ENGAGEARMBRAKE) {
      m_motor1.setSpeed(0);
      return;
    }
    // Positive value for speed makes the angle go up and the arm down
    if ((speed < 0) && (getAngle() <= m_minAngle)) {
      m_motor1.setSpeed(0);
    } else if ((speed > 0) && (getAngle() >= m_maxAngle)) {
      m_motor1.setSpeed(0);
    } else {
      m_motor1.setSpeed(speed); // 0.33 and .05 for test
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
    double angle;
    double preAngle;
    double offset = 350;
    double fixedEncoderValue = m_motor1.getAbsoluteEncoderPosition();
    // A 360 - angle is used to invert the encoder
    preAngle = (fixedEncoderValue * 360) + offset;
    angle = preAngle;
    if (preAngle >= 360) {
      angle = preAngle - 360;
    }
    return angle;
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

  @Override
  protected void useOutput(double output, State setpoint) {
  
  }

  @Override
  protected double getMeasurement() {
    return getAngle();
  }

}
