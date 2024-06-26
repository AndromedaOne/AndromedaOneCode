package frc.robot.subsystems.billFeeder;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealBillFeeder extends SubsystemBase implements BillFeederBase {

  private Config m_feederConfig = Config4905.getConfig4905().getBillFeederConfig();
  private SparkMaxController m_feederMotor;
  private DigitalInput m_noteDetector;
  private double m_counter = 0;
  private boolean m_noteDetected = false;

  public RealBillFeeder() {
    m_feederMotor = new SparkMaxController(m_feederConfig, "feederMotor");
    m_noteDetector = new DigitalInput(m_feederConfig.getInt("ports.noteDetector"));
    m_counter = 0;
    m_noteDetected = false;

  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Note detection", getNoteDetectorState());
    SmartDashboard.putBoolean("ActualNoteDetectorValue", !m_noteDetector.get());
    if (!m_noteDetector.get()) {
      if (m_counter >= 6) {
        m_noteDetected = true;
      } else {
        m_counter++;
      }

    } else {
      m_counter = 0;
      m_noteDetected = false;
    }

  }

// A positive value means intake
  @Override
  public void runBillFeederIntake() {
    m_feederMotor.setSpeed(1);
  }

  @Override
  public void runBillFeederEject() {
    m_feederMotor.setSpeed(-0.8);
  }

  @Override
  public void runBillFeederShooting() {
    m_feederMotor.setSpeed(1);
  }

  @Override
  public void runBillFeederTrapShooting() {
    m_feederMotor.setSpeed(-0.6);
  }

  @Override
  public void runBillFeederSlowEject() {
    m_feederMotor.setSpeed(-0.2);
  }

  @Override
  public void stopBillFeeder() {
    m_feederMotor.setSpeed(0);
  }

  // True means there is a note in the endeffector
  @Override
  public boolean getNoteDetectorState() {
    return m_noteDetected;
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
  public void setBrakeMode() {
    m_feederMotor.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_feederMotor.setCoastMode();
  }

}
