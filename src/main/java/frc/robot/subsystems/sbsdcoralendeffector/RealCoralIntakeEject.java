// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.SparkMaxController;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpoints;
import frc.robot.oi.DriveController;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;

/** Add your docs here. */
public class RealCoralIntakeEject extends SubsystemBase implements CoralIntakeEjectBase {

  private SparkMaxController m_intakeMotor;
  private LimitSwitchSensor m_intakeSideSensor;
  private LimitSwitchSensor m_ejectSideSensor;
  private boolean m_hasCoral = false;
  private double m_intakeSpeed = 0.15;
  private double m_repositionSpeed = 0.1;
  private double m_ejectSpeed = 0.5;
  private boolean m_ejectCoral = false;
  private boolean m_inWaitForCoral = false;
  private final boolean m_useTimerForRumble;
  private boolean m_currentRumble = false;
  private double m_rumbleValue = 1.0;
  private DriveController m_driveController;
  private int m_count = 0;
  private int m_rumbleTimer = 0;
  private boolean m_exitScore = false;
  private boolean m_scoreL4 = false;
  private double m_L4SafeAngleOffset = 90;

  private enum CoralState {
    WAIT_FOR_CORAL, INTAKE_CORAL, POSITION_CORAL, HOLD_CORAL, EJECT_CORAL, PAUSE_FOR_EJECT,
    SCORE_L4_WAIT, POSITION_L4, HOLD_L4_POSITION
  }

  private CoralState m_currentState = CoralState.WAIT_FOR_CORAL;

  public RealCoralIntakeEject() {
    Config config = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig();
    m_intakeMotor = new SparkMaxController(config, "coralDelivery", false, false);
    m_intakeSideSensor = new RealLimitSwitchSensor("endEffectorIntakeSensor");
    m_ejectSideSensor = new RealLimitSwitchSensor("endEffectorEjectSensor");
    m_useTimerForRumble = config.getBoolean("useTimerForRumble");
    m_rumbleValue = config.getDouble("rumbleValue");
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
    SmartDashboard.putBoolean("Eject coral: ", false);
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
  public void runWheels(double speed) {
    m_intakeMotor.setSpeed(speed);
  }

  @Override
  public void runWheelsIntake() {
    /*
     * given that the coral only goes one way through the end effector (intake pulls
     * it in through the intake side, eject scores out the other end), a positive
     * speed will send the coral through the end effector. there should be no reason
     * to go in the opposite direction. also, the speed of the wheels will be
     * determined by experiment, a command will not tell it the speed, the speed
     * will depend upon where the coral is within the end effector and whether it is
     * intaking or scoring or holding. first thimg is to detect that a coral has
     * arrived at the intake and pull it in to the end effector.
     */
    switch (m_currentState) {
    // no coral has been detected on either side of the end effector
    case WAIT_FOR_CORAL:
      stop();
      m_inWaitForCoral = true;
      m_currentRumble = false;
      m_rumbleTimer = 0;
      if (intakeDetector() && !ejectDetector()) {
        m_currentState = CoralState.INTAKE_CORAL;
      } else if (!intakeDetector() && ejectDetector()) {
        m_currentState = CoralState.HOLD_CORAL;
      }
      break;

    // a coral has been detected on the intake side but nothing is on the eject
    // side. so pull the coral into the end effector
    case INTAKE_CORAL:
      m_inWaitForCoral = false;
      m_intakeMotor.setSpeed(m_intakeSpeed);
      if (intakeDetector() && ejectDetector()) {
        m_currentState = CoralState.POSITION_CORAL;
      }
      break;

    /*
     * there's a coral in the end effector but it is detected on both the intake and
     * eject detector, we will need to push the coral out of the end effector until
     * only the eject side detector is true. this must be done to get the coral out
     * of the way so the arm can move to a scoring position.
     */
    case POSITION_CORAL:
      m_intakeMotor.setSpeed(m_repositionSpeed);
      if (!intakeDetector() && ejectDetector()) {
        m_currentState = CoralState.HOLD_CORAL;
      }
      break;

    // the coral is in the end effector and is in the correct position to be scored
    case HOLD_CORAL:
      stop();
      m_hasCoral = true;
      m_inWaitForCoral = false;
      rumbleController();
      if (m_scoreL4) {
        m_currentState = CoralState.SCORE_L4_WAIT;
        m_scoreL4 = false;
      }
      if (!ejectDetector()) {
        m_hasCoral = false;
        m_currentRumble = false;
        m_rumbleTimer = 0;
        m_currentState = CoralState.WAIT_FOR_CORAL;
      } else if (m_ejectCoral) {
        m_ejectCoral = false;
        m_currentRumble = false;
        m_rumbleTimer = 0;
        m_currentState = CoralState.EJECT_CORAL;
      } else if (intakeDetector()) {
        m_hasCoral = false;
        m_currentRumble = false;
        m_rumbleTimer = 0;
        m_currentState = CoralState.POSITION_CORAL;
      }
      break;

    // Eject coral
    case EJECT_CORAL:
      m_intakeMotor.setSpeed(m_ejectSpeed);
      if (!intakeDetector() && !ejectDetector()) {
        m_currentState = CoralState.PAUSE_FOR_EJECT;
      }
      break;

    // wait for the coral to be on the reef before moving
    case PAUSE_FOR_EJECT:
      m_count++;
      stop();
      if (m_count > 5) {
        m_hasCoral = false;
        m_count = 0;
        m_currentState = CoralState.WAIT_FOR_CORAL;
      }
      break;

    case SCORE_L4_WAIT:
      double getSafeAngleToScoreL4 = SBSDArmSetpoints.getInstance()
          .getEndEffectorAngleInDeg(ArmSetpoints.CORAL_LOAD) - m_L4SafeAngleOffset;
      if (Robot.getInstance().getSubsystemsContainer().getSBSDCoralEndEffectorRotateBase()
          .getAngleDeg() <= getSafeAngleToScoreL4) {
        m_currentState = CoralState.POSITION_L4;
      }
      break;

    case POSITION_L4:
      m_intakeMotor.setSpeed(-m_repositionSpeed);
      if (intakeDetector() && !ejectDetector()) {
        m_currentState = CoralState.HOLD_L4_POSITION;
      }
      break;

    case HOLD_L4_POSITION:
      stop();
      m_hasCoral = true;
      m_inWaitForCoral = false;
      rumbleController();
      if (m_exitScore) {
        m_exitScore = false;
        m_currentRumble = false;
        m_rumbleTimer = 0;
        m_currentState = CoralState.POSITION_CORAL;
      }
      if (m_ejectCoral) {
        m_ejectCoral = false;
        m_currentRumble = false;
        m_rumbleTimer = 0;
        m_currentState = CoralState.EJECT_CORAL;
      }
      if (!intakeDetector()) {
        m_hasCoral = false;
        m_currentRumble = false;
        m_rumbleTimer = 0;
        m_currentState = CoralState.WAIT_FOR_CORAL;
      }
      break;

    default:
      stop();
      break;
    }
    SmartDashboard.putString("Coral State: ", m_currentState.toString());
    m_ejectCoral = SmartDashboard.getBoolean("Eject coral: ", false);
    SmartDashboard.putBoolean("Is in wait for coral: ", m_inWaitForCoral);
    runRumble();
  }

  @Override
  public void runWheelsEject() {
    m_intakeMotor.setSpeed(m_ejectSpeed);
  }

  @Override
  public void setEjectState() {
    m_ejectCoral = true;
  }

  @Override
  public void stop() {
    m_intakeMotor.setSpeed(0);
  }

  @Override
  public boolean intakeDetector() {
    return m_intakeSideSensor.isAtLimit();

  }

  @Override
  public boolean ejectDetector() {
    return m_ejectSideSensor.isAtLimit();
  }

  @Override
  public void setCoastMode() {
    m_intakeMotor.setCoastMode();
  }

  @Override
  public void setBrakeMode() {
    m_intakeMotor.setBrakeMode();
  }

  @Override
  public boolean getCoralDetected() {
    return m_hasCoral;
  }

  @Override
  public boolean hasScored() {
    return m_inWaitForCoral;
  }

  private void runRumble() {
    if (m_currentRumble) {
      m_driveController.rumbleOn(m_rumbleValue);
    } else {
      m_driveController.rumbleOff();
    }
  }

  public void exitScore() {
    m_exitScore = true;
  }

  public void scoreL4() {
    m_scoreL4 = true;
  }

  private void rumbleController() {
    if (m_useTimerForRumble) {
      if (m_rumbleTimer < 50) {
        m_currentRumble = true;
        m_rumbleTimer++;
      } else {
        m_currentRumble = false;
      }
    } else {
      m_currentRumble = true;
    }

  }
}
