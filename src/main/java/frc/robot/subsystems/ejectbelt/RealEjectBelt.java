package frc.robot.subsystems.ejectbelt;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealEjectBelt extends SubsystemBase implements EjectBeltBase {
  private SparkMaxController m_leaderMotor;
  private SparkMaxController m_followerMotor;
  private Config m_ejectBeltConfig = Config4905.getConfig4905().getEjectBeltConfig();

  public RealEjectBelt() {
    m_leaderMotor = new SparkMaxController(m_ejectBeltConfig, "leaderMotor", false, false);
    m_followerMotor = new SparkMaxController(m_ejectBeltConfig, "followerMotor", false, false);
  }

  @Override
  public void stop() {
    m_leaderMotor.setSpeed(0);
    m_followerMotor.setSpeed(0);
  }

  // not sure which way this will actually go...
  @Override
  public void ejectLeft() {
    m_leaderMotor.setSpeed(1);
    m_followerMotor.setSpeed(1);
  }

  @Override
  public void ejectRight() {
    m_leaderMotor.setSpeed(-1);
    m_followerMotor.setSpeed(-1);
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
