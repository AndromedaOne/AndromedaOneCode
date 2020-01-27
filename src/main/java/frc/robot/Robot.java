/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.actuators.SparkMaxController;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private CANSparkMax shooterOne;
  private CANSparkMax shooterTwo;
  private WPI_TalonSRX shooterSeries;
  private double speed = 0;
  private boolean speedUpFlag = false;
  private boolean speedDownFlag = false;
  private boolean speedSmallUpFlag = false;
  private boolean speedSmallDownFlag = false;
  private Joystick controller = new Joystick(0);
  private CANEncoder shooterEncoder;
  private int loopCount = 0;

 // private static Config nameConfig = ConfigFactory.parseFile(new File("/home/lvuser/name.conf"));

  /**
   * This config should live on the robot and have hardware- specific configs.
   */
  //private static Config environmentalConfig = ConfigFactory
  //    .parseFile(new File("/home/lvuser/deploy/robotConfigs/" + nameConfig.getString("robot.name") + "/robot.conf"));

  /**
   * This config lives in the jar and has hardware-independent configs.
   */
  //private static Config defaultConfig = ConfigFactory.parseResources("application.conf");

  /**
   * Combined config
   */
 // protected static Config conf = environmentalConfig.withFallback(defaultConfig).resolve();

  /**
   * Get the robot's config
   * 
   * @return the config
   */
  /*public static Config getConfig() {
    return conf;
  }*/

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
   // System.out.println("Robot name = " + nameConfig.getString("robot.name"));

    /*if (conf.hasPath("subsystems.driveTrain")) {
      System.out.println("Using real drivetrain");
    } else {
      System.out.println("Using fake drivetrain");
    }*/

    shooterOne = new CANSparkMax(1, MotorType.kBrushless);
    shooterTwo = new CANSparkMax(2, MotorType.kBrushless);
    shooterSeries = new WPI_TalonSRX(6);
    shooterEncoder = new CANEncoder(shooterOne);
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

    Trace.getInstance().addTrace(true, "ShootingWheel",
     new TracePair<Double>("Shooting Velocity", shooterEncoder.getVelocity()));

    if(loopCount > 25) {
      loopCount = 0;
      System.out.println(" --- Shooter: " + shooterEncoder.getVelocity() + " RPM ---"); 
      System.out.println();
      System.out.println(); 
    }

    loopCount++;

    if (controller.getRawButtonPressed(4) && !speedUpFlag) {
      speed += speed < 1 ? .1 : 0;
      System.out.println(" - Speed: " + speed);
      speedUpFlag = true;
    } else if (controller.getRawButtonReleased(4)) {
      speedUpFlag = false;
    }

    if (controller.getRawButtonPressed(1) && !speedDownFlag) {
      speed -= speed > -1 ? .1 : 0;
      System.out.println(" - Speed: " + speed);
      speedDownFlag = true;
    } else if (controller.getRawButtonReleased(1)) {
      speedDownFlag = false;
    }

    if (controller.getPOV() == 90 && !speedSmallUpFlag) {
      speed += speed < 1 ? .05 : 0;
      System.out.println(" - Speed: " + speed);
      speedSmallUpFlag = true;
    } else if (controller.getPOV() == 90) {
      speedSmallUpFlag = false;
    }

    if (controller.getPOV() == 270 && !speedSmallDownFlag) {
      speed -= speed < 1 ? .05 : 0;
      System.out.println(" - Speed: " + speed);
      speedSmallDownFlag = true;
    } else if (controller.getPOV() == 270) {
      speedSmallDownFlag = false;
    }

    

    shooterOne.set(-speed);
    shooterTwo.set(speed);
    shooterSeries.set(speed);
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
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
    m_autonomousCommand = null;

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
