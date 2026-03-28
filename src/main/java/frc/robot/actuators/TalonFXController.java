package frc.robot.actuators;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.typesafe.config.Config;

public class TalonFXController {
  private TalonFX m_talonFXMotor;
  private boolean m_hasAbsoluteEncoder = false;
  private CANcoder m_absoluteEncoder;
  private TalonFXConfiguration m_talonConfig;
  private double m_rampRate;
  // removed the limit switches from this...
  // i have no clue how they work in the ctre api.
  // it's not a high priority, but just keep this in mind :)

  public TalonFXController(Config subsystemConfig, String configString) {
    m_talonFXMotor = new TalonFX(subsystemConfig.getInt("ports." + configString),
        new CANBus("rio"));
    System.out.println("Enabling talonFX \"" + configString + "\" for port "
        + subsystemConfig.getInt("ports." + configString));
    m_hasAbsoluteEncoder = subsystemConfig.getBoolean(configString + ".hasAbsoluteEncoder");
    m_talonConfig = new TalonFXConfiguration();
    m_rampRate = subsystemConfig.getDouble(configString + ".rampRate");
    configure(subsystemConfig, configString);
  }

  private void configure(Config subsystemConfig, String configString) {
    // might want to reverse the logic here
    // atm, inverted is CCW+, and normal is CW+
    if (subsystemConfig.getBoolean(configString + ".inverted")) {
      m_talonConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    } else {
      m_talonConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    }

    // The Kraken cannot have a set conversion factor for getPosition and
    // getVelocity
    if (m_hasAbsoluteEncoder) {
      // legit no clue what this does. something about abs encoders?
      m_talonConfig.Slot0 = new Slot0Configs().withKP(100).withKI(0.0).withKD(0);
      m_talonConfig.ClosedLoopGeneral.ContinuousWrap = true;
      m_talonConfig.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
      m_talonConfig.Feedback.FeedbackRemoteSensorID = subsystemConfig.getInt("dude idk");
    }

    if (subsystemConfig.hasPath(configString + ".isFollower")) {
      if (subsystemConfig.getBoolean(configString + ".isFollower")) {
        MotorAlignmentValue MAV;
        if (subsystemConfig.getBoolean(configString + ".inverted")) {
          MAV = MotorAlignmentValue.Opposed;
        } else {
          MAV = MotorAlignmentValue.Aligned;
        }
        m_talonFXMotor
            .setControl(new Follower(subsystemConfig.getInt(configString + ".leader"), MAV));
      }
    }

    m_talonFXMotor.getConfigurator().apply(m_talonConfig);
  }

  public TalonFX getMotorController() {
    return m_talonFXMotor;
  }

  public double getBuiltInEncoderPositionTicks() {
    // in rotations
    return m_talonFXMotor.getPosition().getValueAsDouble();
  }

  public double getBuiltInEncoderVelocityTicks() {
    // in rotations per second
    return m_talonFXMotor.getVelocity().getValueAsDouble();
  }

  public boolean hasAbsoluteEncoder() {
    return m_hasAbsoluteEncoder;
  }

  public void setCoastMode() {
    m_talonFXMotor.setNeutralMode(NeutralModeValue.Coast);
    System.out.println("Talon set to coast");
  }

  public void setBrakeMode() {
    m_talonFXMotor.setNeutralMode(NeutralModeValue.Brake);
    System.out.println("Talon set to brake");
  }

  public void disableAccelerationLimiting() {
    // do we need to apply the config?
    m_talonConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0;
  }

  public void enableAccelerationLimiting(double rate) {
    // do we need to apply the config?
    m_talonConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = rate;
  }

  public void enableAccelerationLimiting() {
    // do we need to apply the config?
    m_talonConfig.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_rampRate;
  }

  public void resetEncoder() {
    m_absoluteEncoder.setPosition(0);
  }

  public void setSpeed(double speed) {
    m_talonFXMotor.set(speed);
  }

  public double getSpeed() {
    return m_talonFXMotor.get();
  }

  // Returns the position between -1 and 1 - It rolls over at 1
  // this could seriously mess up code, due to neos using 0-1
  // PLEASE use this with caution!
  public double getAbsoluteEncoderPosition() {
    return m_absoluteEncoder.getAbsolutePosition().getValueAsDouble();
  }
}