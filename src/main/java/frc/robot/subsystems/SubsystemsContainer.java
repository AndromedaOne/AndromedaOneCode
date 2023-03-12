/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.SAMgripperCommands.DefaultGripperCommand;
import frc.robot.commands.driveTrainCommands.TeleOpCommand;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetractInternal;
import frc.robot.commands.samArmExtendRetractCommands.InitializeArmExtRet;
import frc.robot.commands.samArmRotateCommands.EnableArmBrake;
import frc.robot.commands.showBotCannon.AdjustElevation;
import frc.robot.commands.topGunFeederCommands.StopFeeder;
import frc.robot.commands.topGunIntakeCommands.RetractAndStopIntake;
import frc.robot.commands.topGunShooterCommands.DefaultShooterAlignment;
import frc.robot.commands.topGunShooterCommands.StopShooter;
import frc.robot.subsystems.SAMgripper.GripperBase;
import frc.robot.subsystems.SAMgripper.MockGripper;
import frc.robot.subsystems.SAMgripper.RealGripper;
import frc.robot.subsystems.compressor.CompressorBase;
import frc.robot.subsystems.compressor.MockCompressor;
import frc.robot.subsystems.compressor.RealCompressor;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.MockDriveTrain;
import frc.robot.subsystems.drivetrain.RomiDriveTrain;
import frc.robot.subsystems.drivetrain.SparkMaxDriveTrain;
import frc.robot.subsystems.drivetrain.TalonSRXDriveTrain;
import frc.robot.subsystems.ledlights.LEDs;
import frc.robot.subsystems.ledlights.MockLEDs;
import frc.robot.subsystems.ledlights.TopGunLEDs;
import frc.robot.subsystems.samArmExtRet.MockSamArmExtRet;
import frc.robot.subsystems.samArmExtRet.RealSamArmExtRet;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.MockSamArmRotate;
import frc.robot.subsystems.samArmRotate.RealSamArmRotate;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.subsystems.showBotCannon.CannonBase;
import frc.robot.subsystems.showBotCannon.MockCannon;
import frc.robot.subsystems.showBotCannon.RealCannon;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunFeeder.MockFeeder;
import frc.robot.subsystems.topGunFeeder.RealFeeder;
import frc.robot.subsystems.topGunIntake.IntakeBase;
import frc.robot.subsystems.topGunIntake.MockIntake;
import frc.robot.subsystems.topGunIntake.RealIntake;
import frc.robot.subsystems.topGunShooter.BottomShooterWheel;
import frc.robot.subsystems.topGunShooter.MockBottomShooter;
import frc.robot.subsystems.topGunShooter.MockShooterAlignment;
import frc.robot.subsystems.topGunShooter.MockTopShooter;
import frc.robot.subsystems.topGunShooter.ShooterAlignment;
import frc.robot.subsystems.topGunShooter.ShooterAlignmentBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;
import frc.robot.subsystems.topGunShooter.TopShooterWheel;

public class SubsystemsContainer {

  // Declare member variables.
  DriveTrain m_driveTrain;
  LEDs m_leds;
  CompressorBase m_compressor;
  CannonBase m_cannon;
  ShooterWheelBase m_topShooterWheel;
  ShooterWheelBase m_bottomShooterWheel;
  IntakeBase m_intake;
  FeederBase m_feeder;
  ShooterAlignmentBase m_shooterAlignment;
  GripperBase m_gripper;
  SamArmExtRetBase m_armExtRet;
  SamArmRotateBase m_armRotate;

  /**
   * The container responsible for setting all the subsystems to real or mock.
   * Uses config settings to determine this.
   * 
   */
  public SubsystemsContainer() {
    /*
     * Sets the member variables to use either a real or mock subsystem, so we can
     * use a robot that has them or is only a mule.
     *
     * The settings will be printed to the console.
     *
     */
    if (Config4905.getConfig4905().doesDrivetrainExist()) {
      System.out.println("Using real Drive Train.");
      if (Config4905.getConfig4905().getDrivetrainConfig().getString("motorController")
          .equals("sparkMax")) {
        System.out.println("Using real sparkMax Drive Train");
        m_driveTrain = new SparkMaxDriveTrain();
      } else if (Config4905.getConfig4905().getDrivetrainConfig().getString("motorController")
          .equals("talonSRX")) {
        System.out.println("Using real talonSRX Drive Train");
        m_driveTrain = new TalonSRXDriveTrain();
      } else if (Config4905.getConfig4905().getDrivetrainConfig().getString("motorController")
          .equals("romiDrive")) {
        System.out.println("Using Romi drive train");
        m_driveTrain = new RomiDriveTrain();
      } else {
        String drivetrainType = Config4905.getConfig4905().getDrivetrainConfig()
            .getString("motorController");
        throw (new RuntimeException(
            "ERROR: Unknown drivetrain type: " + drivetrainType + " in drivetrain.conf"));
      }
    } else {
      System.out.println("Using mock Drive Train.");
      m_driveTrain = new MockDriveTrain();
    }
    m_driveTrain.init();

    if (Config4905.getConfig4905().doesLEDExist()) {
      if (Config4905.getConfig4905().isShowBot()) {
        System.out.println("Using TopGun LEDs");
        m_leds = new TopGunLEDs();
      } else {
        System.out.println("Using TopGun LEDs");
        m_leds = new TopGunLEDs();
      }
    } else {
      System.out.println("Using Mock LEDs");
      m_leds = new MockLEDs();
    }
    if (Config4905.getConfig4905().doesCompressorExist()) {
      System.out.println("using real Compressor.");
      m_compressor = new RealCompressor();
      m_compressor.start();
    } else {
      System.out.println("Using mock Compressor");
      m_compressor = new MockCompressor();
    }
    if (Config4905.getConfig4905().doesGripperExist()) {
      // Gripper must be constructed after compressor
      System.out.println("using real gripper.");
      m_gripper = new RealGripper(m_compressor);
    } else {
      System.out.println("Using mock gripper");
      m_gripper = new MockGripper();
    }
    if (Config4905.getConfig4905().doesCannonExist()) {
      // Gripper must be constructed after compressor
      System.out.println("using real Cannon.");
      m_cannon = new RealCannon(m_compressor);
    } else {
      System.out.println("Using mock Cannon");
      m_cannon = new MockCannon();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      System.out.println("using real shooters");
      m_topShooterWheel = new TopShooterWheel();
      m_bottomShooterWheel = new BottomShooterWheel();
      m_shooterAlignment = new ShooterAlignment();
    } else {
      System.out.println("using mock shooters");
      m_topShooterWheel = new MockTopShooter();
      m_bottomShooterWheel = new MockBottomShooter();
      m_shooterAlignment = new MockShooterAlignment();
    }
    if (Config4905.getConfig4905().doesIntakeExist()) {
      System.out.println("using real intake");
      m_intake = new RealIntake();
    } else {
      System.out.println("using mock Intake");
      m_intake = new MockIntake();
    }
    if (Config4905.getConfig4905().doesFeederExist()) {
      System.out.println("using real feeder");
      m_feeder = new RealFeeder();
    } else {
      System.out.println("using mock feeder");
      m_feeder = new MockFeeder();
    }
    if (Config4905.getConfig4905().doesSamArmExtRetExist()) {
      System.out.println("using real arm extend retract");
      m_armExtRet = new RealSamArmExtRet();
    } else {
      System.out.println("using mock arm extend retract");
      m_armExtRet = new MockSamArmExtRet();
    }
    if (Config4905.getConfig4905().doesSamArmRotateExist()) {
      System.out.println("using real arm rotate");
      m_armRotate = new RealSamArmRotate(m_compressor);
    } else {
      System.out.println("using mock arm rotate");
      m_armRotate = new MockSamArmRotate();
    }

  }

  public DriveTrain getDrivetrain() {
    return m_driveTrain;
  }

  public CompressorBase getCompressor() {
    return m_compressor;
  }

  public GripperBase getGripper() {
    return m_gripper;
  }

  public CannonBase getCannon() {
    return m_cannon;
  }

  public ShooterWheelBase getTopShooterWheel() {
    return m_topShooterWheel;
  }

  public ShooterWheelBase getBottomShooterWheel() {
    return m_bottomShooterWheel;
  }

  public ShooterAlignmentBase getShooterAlignment() {
    return m_shooterAlignment;
  }

  public IntakeBase getIntake() {
    return m_intake;
  }

  public FeederBase getFeeder() {
    return m_feeder;
  }

  public SamArmExtRetBase getArmExtRetBase() {
    return m_armExtRet;
  }

  public SamArmRotateBase getArmRotateBase() {
    return m_armRotate;
  }

  public LEDs getLEDs() {
    return m_leds;
  }

  public void setDefaultCommands() {
    if (Config4905.getConfig4905().doesDrivetrainExist()) {
      m_driveTrain.setDefaultCommand(new TeleOpCommand());
    }
    if (Config4905.getConfig4905().isShowBot()) {
      m_cannon.setDefaultCommand(new AdjustElevation(m_cannon));
    }
    if (Config4905.getConfig4905().doesIntakeExist()) {
      m_intake.setDefaultCommand(new RetractAndStopIntake(m_intake));
    }
    if (Config4905.getConfig4905().doesFeederExist()) {
      m_feeder.setDefaultCommand(new StopFeeder(m_feeder));
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      m_topShooterWheel.setDefaultCommand(new StopShooter(m_topShooterWheel, m_bottomShooterWheel));
      m_bottomShooterWheel
          .setDefaultCommand(new StopShooter(m_topShooterWheel, m_bottomShooterWheel));
      m_shooterAlignment.setDefaultCommand(new DefaultShooterAlignment(m_shooterAlignment));
    }
    if (Config4905.getConfig4905().doesSamArmExtRetExist()) {
      m_armExtRet.setDefaultCommand(new SequentialCommandGroup(new InitializeArmExtRet(m_armExtRet),
          new ExtendRetractInternal(m_armExtRet, false)));
    }
    if (Config4905.getConfig4905().doesSamArmRotateExist()) {
      m_armRotate.setDefaultCommand(new EnableArmBrake(m_armRotate));
    }

    if (Config4905.getConfig4905().doesGripperExist()) {
      m_gripper.setDefaultCommand(new DefaultGripperCommand(m_gripper));
    }

  }
}
