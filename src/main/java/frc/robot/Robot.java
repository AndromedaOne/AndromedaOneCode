/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.driveTrainCommands.EnableParkingBrake;
import frc.robot.oi.OIContainer;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.telemetries.Trace;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private SubsystemsContainer m_subsystemContainer;
  private SensorsContainer m_sensorsContainer;
  private OIContainer m_oiContainer;
  private LimeLightCameraBase m_limelight;
  private boolean m_parkingBrakeScheduled = true;

  private Robot() {
  }

  static Robot m_instance;

  public static Robot getInstance() {
    if (m_instance == null) {
      m_instance = new Robot();
    }
    return m_instance;
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.

    Trace.getInstance().logInfo("robot init started");
    m_sensorsContainer = new SensorsContainer();
    m_subsystemContainer = new SubsystemsContainer();
    m_oiContainer = new OIContainer(m_subsystemContainer, m_sensorsContainer);
    m_subsystemContainer.setDefaultCommands();
    m_limelight = m_sensorsContainer.getLimeLight();
    m_limelight.disableLED();
    m_subsystemContainer.getDrivetrain().setCoast(true);
    LiveWindow.disableAllTelemetry();
    m_subsystemContainer.getArmExtRetBase().setZeroOffset();
    Trace.getInstance().logInfo("robot init finished");
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    m_sensorsContainer.getGyro().updateSmartDashboardReadings();
    m_sensorsContainer.getLimeLight().updateSmartDashboardReadings();
    m_sensorsContainer.periodic();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    if (DriverStation.isFMSAttached()) {
      Trace.getInstance().matchStarted(DriverStation.getMatchNumber());
    }
    m_subsystemContainer.getDrivetrain().setCoast(true);
    Trace.getInstance().flushTraceFiles();
    m_limelight.disableLED();
    m_subsystemContainer.getShooterAlignment().setCoastMode();
    System.out.println("Shooter Allignment set to coast");
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    Trace.getInstance().logInfo("autonomousInit called");
    setInitialZangleOffset();

    m_autonomousCommand = m_oiContainer.getSmartDashboard().getSelectedAutoChooserCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
      System.out.println("Autonamous Command Schedule: " + m_autonomousCommand.getName());
    } else {
      System.out.println("No Autonamous Command Scheduled");
    }
    if (DriverStation.isFMSAttached()) {
      Trace.getInstance().matchStarted(DriverStation.getMatchNumber());
    }
    m_limelight.enableLED();
    m_subsystemContainer.getDrivetrain().setCoast(false);
    m_subsystemContainer.getShooterAlignment().setBrakeMode();
    System.out.println("Shooter Allignment set to brake");
    m_subsystemContainer.getDrivetrain().disableParkingBrakes();
    LiveWindow.disableAllTelemetry();
    m_parkingBrakeScheduled = false;
    Trace.getInstance().logInfo("autonomousInit finished");
  }

  private boolean m_autoPeriodicLogged = false;

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    if (!m_autoPeriodicLogged) {
      Trace.getInstance().logInfo("autonomousPeriodic called");
      m_autoPeriodicLogged = true;
    }
    if (m_subsystemContainer.getDrivetrain().hasParkingBrake()) {
      double matchTime = DriverStation.getMatchTime();
      if (matchTime <= 0.5) {
        // Call enable parking brake
        System.out.println("Parking Brake First Call");
        if (!m_parkingBrakeScheduled) {
          CommandScheduler.getInstance()
              .schedule(new EnableParkingBrake(m_subsystemContainer.getDrivetrain()));
          m_parkingBrakeScheduled = true;
          System.out.println("PArking Brake is enabled");
        }
      }
    }

  }

  private void setInitialZangleOffset() {
    // For the Charged Up game 2023, the robot starts facing north.
    m_sensorsContainer.getGyro().setInitialZangleOffset(180);
  }

  @Override
  public void teleopInit() {
    Trace.getInstance().logInfo("teleopInit called");
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    setInitialZangleOffset();

    if (DriverStation.isFMSAttached()) {
      Trace.getInstance().matchStarted(DriverStation.getMatchNumber());
    }
    m_limelight.disableLED();
    m_subsystemContainer.getDrivetrain().setCoast(false);
    m_subsystemContainer.getShooterAlignment().setBrakeMode();
    System.out.println("Shooter Allignment set to brake");
    m_subsystemContainer.getDrivetrain().disableParkingBrakes();
    LiveWindow.disableAllTelemetry();
    m_parkingBrakeScheduled = false;
    Trace.getInstance().logInfo("teleopInit finished");
  }

  private boolean m_teleopPeriodicLogged = false;

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    if (!m_teleopPeriodicLogged) {
      Trace.getInstance().logInfo("teleopPeriodic called");
      m_teleopPeriodicLogged = true;
    }
    if (m_subsystemContainer.getDrivetrain().hasParkingBrake()) {
      double matchTime = DriverStation.getMatchTime();
      if (matchTime <= 0.5) {
        // Call enable parking brake
        if (!m_parkingBrakeScheduled) {
          CommandScheduler.getInstance()
              .schedule(new EnableParkingBrake(m_subsystemContainer.getDrivetrain()));
          m_parkingBrakeScheduled = true;
        }
      }

    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    m_subsystemContainer.getDrivetrain().setCoast(false);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  // getters for various OI things below

  public SubsystemsContainer getSubsystemsContainer() {
    return m_subsystemContainer;
  }

  public SensorsContainer getSensorsContainer() {
    return m_sensorsContainer;
  }

  public OIContainer getOIContainer() {
    return m_oiContainer;
  }

}