/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;
import java.util.Optional;

import org.json.simple.parser.ParseException;

import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.FuelRaiderCommands.SmartDashboardExtend;
import frc.robot.commands.FuelRaiderCommands.SmartDashboardRetract;
import frc.robot.commands.autoCommands.LeftHubSetPoseManually;
import frc.robot.commands.autoCommands.RightHubSetPoseManually;
import frc.robot.commands.climberCommands.ClimberExtensionCommand;
import frc.robot.commands.climberCommands.ClimberRotationCommand;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetVelocityToZero;
import frc.robot.commands.driveTrainCommands.TowerAlignment;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.ejectBeltCommands.EjectBeltLeft;
import frc.robot.commands.ejectBeltCommands.EjectBeltRight;
import frc.robot.commands.intakeCommands.IntakeRollerEjectCommand;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
import frc.robot.oi.OIContainer;
import frc.robot.sensors.SensorsContainer;
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
    Trace.getInstance().setTracePairsEnable(false);
    Trace.getInstance().logInfo("robot init started");
    m_sensorsContainer = new SensorsContainer();
    m_subsystemContainer = new SubsystemsContainer();
    NamedCommands.registerCommand("setVelocityToZero", new SwerveDriveSetVelocityToZero());
    NamedCommands.registerCommand("eject belt left", new EjectBeltLeft());
    NamedCommands.registerCommand("eject belt right", new EjectBeltRight());
    NamedCommands.registerCommand("tower alignment",
        new TowerAlignment(m_subsystemContainer.getDriveTrain(), 0.3,
            m_sensorsContainer.getTof1().getFacingAngle(), m_sensorsContainer.getTof1()));
    NamedCommands.registerCommand("extend climber",
        new ClimberExtensionCommand("LongClimberExtend"));
    NamedCommands.registerCommand("rotate climber", new ClimberRotationCommand("ClimbUp"));
    NamedCommands.registerCommand("right hub set pose", new RightHubSetPoseManually());
    NamedCommands.registerCommand("left hub set pose", new LeftHubSetPoseManually());
    NamedCommands.registerCommand("turn to 0", new TurnToCompassHeading(() -> 0));
    NamedCommands.registerCommand("turn to 180", new TurnToCompassHeading(() -> 180));
    NamedCommands.registerCommand("AHI extend", new SmartDashboardExtend());
    NamedCommands.registerCommand("AHI retract", new SmartDashboardRetract());
    NamedCommands.registerCommand("intake", new IntakeRollerIntakeCommand());
    NamedCommands.registerCommand("eject using intake", new IntakeRollerEjectCommand());

    try {
      m_subsystemContainer.getDriveTrain().configurePathPlanner();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    try {
      m_oiContainer = new OIContainer(m_subsystemContainer, m_sensorsContainer);
    } catch (FileVersionException | IOException | ParseException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    m_subsystemContainer.setDefaultCommands();
    if (Config4905.getConfig4905().doesSwerveDrivetrainExist()
        || Config4905.getConfig4905().doesTankDrivetrainExist()) {
    }
    m_subsystemContainer.getDriveTrain().setCoast(true);
    LiveWindow.disableAllTelemetry();
    CommandScheduler.getInstance()
        .onCommandInitialize(command -> Trace.getInstance().logCommandStart(command));
    CommandScheduler.getInstance()
        .onCommandFinish(command -> Trace.getInstance().logCommandStop(command));
    SignalLogger.enableAutoLogging(false);
    CommandScheduler.getInstance().schedule(FollowPathCommand.warmupCommand());
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
    m_sensorsContainer.periodic();
    SmartDashboard.putBoolean("is hub active", isHubActive());
    Trace.getInstance().flushCommandTraceFile();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    Trace.getInstance().logInfo("disabledInit called");
    if (DriverStation.isFMSAttached()) {
      Trace.getInstance().matchStarted(DriverStation.getMatchNumber());
    }
    m_subsystemContainer.getDriveTrain().setCoast(true);
    Trace.getInstance().flushTraceFiles();
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
    m_subsystemContainer.getDriveTrain().setCoast(false);
    m_subsystemContainer.getDriveTrain().disableParkingBrakes();
    LiveWindow.disableAllTelemetry();

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

  }

  private void setInitialZangleOffset() {
    m_sensorsContainer.getGyro().setInitialZangleOffset(0, false);
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
    m_subsystemContainer.getDriveTrain().setCoast(false);
    m_subsystemContainer.getDriveTrain().disableParkingBrakes();
    LiveWindow.disableAllTelemetry();
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

  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    m_subsystemContainer.getDriveTrain().setCoast(false);
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

  /**
   * Took this from the FIRST website. Put in robot.java because it seemed like it
   * would make the most sense to be in here. May want to create another method to
   * return true only when it changes, allowing for the controller to rumble when
   * the hub switches.
   */
  public boolean isHubActive() {
    Optional<Alliance> alliance = DriverStation.getAlliance();
    // If we have no alliance, we cannot be enabled, therefore no hub.
    if (alliance.isEmpty()) {
      return false;
    }
    // Hub is always enabled in autonomous.
    if (DriverStation.isAutonomousEnabled()) {
      return true;
    }
    // At this point, if we're not teleop enabled, there is no hub.
    if (!DriverStation.isTeleopEnabled()) {
      return false;
    }

    // We're teleop enabled, compute.
    double matchTime = DriverStation.getMatchTime();
    String gameData = DriverStation.getGameSpecificMessage();
    // If we have no game data, we cannot compute, assume hub is active, as its
    // likely early in teleop.
    if (gameData.isEmpty()) {
      return true;
    }
    boolean redInactiveFirst = false;
    switch (gameData.charAt(0)) {
    case 'R' -> redInactiveFirst = true;
    case 'B' -> redInactiveFirst = false;
    default -> {
      // If we have invalid game data, assume hub is active.
      return true;
    }
    }

    // Shift was is active for blue if red won auto, or red if blue won auto.
    boolean shift1Active = switch (alliance.get()) {
    case Red -> !redInactiveFirst;
    case Blue -> redInactiveFirst;
    };

    if (matchTime > 130) {
      // Transition shift, hub is active.
      return true;
    } else if (matchTime > 105) {
      // Shift 1
      return shift1Active;
    } else if (matchTime > 80) {
      // Shift 2
      return !shift1Active;
    } else if (matchTime > 55) {
      // Shift 3
      return shift1Active;
    } else if (matchTime > 30) {
      // Shift 4
      return !shift1Active;
    } else {
      // End game, hub always active.
      return true;
    }
  }

}