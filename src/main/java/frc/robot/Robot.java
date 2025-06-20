/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetVelocityToZero;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdAutoCommands.AutoFinish123C;
import frc.robot.commands.sbsdAutoCommands.AutoFinish123L;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4C;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4D;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4E;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4G;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4J;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4K;
import frc.robot.commands.sbsdAutoCommands.AutoFinish4L;
import frc.robot.commands.sbsdAutoCommands.WaitForCoral;
import frc.robot.commands.sbsdAutoCommands.sbsdCoralScoreLevel1;
import frc.robot.commands.sbsdAutoCommands.sbsdCoralScoreLevel2;
import frc.robot.commands.sbsdAutoCommands.sbsdCoralScoreLevel3;
import frc.robot.commands.sbsdAutoCommands.sbsdCoralScoreLevel4;
import frc.robot.commands.sbsdTeleOpCommands.sbsdCoralLoadArmEndEffectorPositon;
import frc.robot.commands.sbsdTeleOpCommands.sbsdScoreCoral;
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
    NamedCommands.registerCommand("sbsdCoralScoreLevel4", new sbsdCoralScoreLevel4());
    NamedCommands.registerCommand("sbsdCoralScoreLevel2", new sbsdCoralScoreLevel2());
    NamedCommands.registerCommand("sbsdCoralScoreLevel3", new sbsdCoralScoreLevel3());
    NamedCommands.registerCommand("sbsdCoralScoreLevel1", new sbsdCoralScoreLevel1());
    NamedCommands.registerCommand("sbsdScoreCoral", new sbsdScoreCoral());
    NamedCommands.registerCommand("sbsdCoralLoadArmEndEffectorPositon",
        new sbsdCoralLoadArmEndEffectorPositon());
    NamedCommands.registerCommand("WaitForCoral", new WaitForCoral());
    NamedCommands.registerCommand("autoFinish4C", new AutoFinish4C());
    NamedCommands.registerCommand("autoFinish4D", new AutoFinish4D());
    NamedCommands.registerCommand("autoFinish4E", new AutoFinish4E());
    NamedCommands.registerCommand("autoFinish4G", new AutoFinish4G());
    NamedCommands.registerCommand("autoFinish4J", new AutoFinish4J());
    NamedCommands.registerCommand("autoFinish4K", new AutoFinish4K());
    NamedCommands.registerCommand("autoFinish4L", new AutoFinish4L());
    NamedCommands.registerCommand("autoFinish123C", new AutoFinish123C());
    NamedCommands.registerCommand("autoFinish123L", new AutoFinish123L());
    NamedCommands.registerCommand("setVelocityToZero", new SwerveDriveSetVelocityToZero());

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
      NamedCommands.registerCommand("sbsdCoralScoreLevel4", new sbsdCoralScoreLevel4());
      NamedCommands.registerCommand("sbsdCoralScoreLevel2", new sbsdCoralScoreLevel2());
      NamedCommands.registerCommand("sbsdCoralScoreLevel3", new sbsdCoralScoreLevel3());
      NamedCommands.registerCommand("sbsdCoralScoreLevel1", new sbsdCoralScoreLevel1());
      NamedCommands.registerCommand("sbsdScoreCoral", new sbsdScoreCoral());
    }
    m_subsystemContainer.getDriveTrain().setCoast(true);
    m_subsystemContainer.getSBSDCoralIntakeEjectBase().setCoastMode();
    m_subsystemContainer.getSBSDCoralIntakeEjectBase()
        .setDriveController(m_oiContainer.getDriveController());
    m_subsystemContainer.getSBSDCoralIntakeEjectBase()
        .setSubsystemController(m_oiContainer.getSubsystemController());
    m_subsystemContainer.getSBSDArmBase().setGoalDeg(SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD);
    m_subsystemContainer.getSBSDCoralEndEffectorRotateBase()
        .setAngleDeg(SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD);
    LiveWindow.disableAllTelemetry();
    CommandScheduler.getInstance()
        .onCommandInitialize(command -> Trace.getInstance().logCommandStart(command));
    CommandScheduler.getInstance()
        .onCommandFinish(command -> Trace.getInstance().logCommandStop(command));
    Trace.getInstance().logInfo("robot init finished");
    FollowPathCommand.warmupCommand().schedule();
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
    m_subsystemContainer.getSBSDCoralIntakeEjectBase().setCoastMode();
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

    m_subsystemContainer.getSBSDClimberBase().setServoInitialPosition();

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
    m_subsystemContainer.getSBSDCoralIntakeEjectBase().setBrakeMode();
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
    m_subsystemContainer.getSBSDClimberBase().setServoInitialPosition();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    setInitialZangleOffset();
    if (DriverStation.isFMSAttached()) {
      Trace.getInstance().matchStarted(DriverStation.getMatchNumber());
    }
    m_subsystemContainer.getDriveTrain().setCoast(false);
    m_subsystemContainer.getDriveTrain().disableParkingBrakes();
    m_subsystemContainer.getSBSDCoralIntakeEjectBase().setBrakeMode();
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

}