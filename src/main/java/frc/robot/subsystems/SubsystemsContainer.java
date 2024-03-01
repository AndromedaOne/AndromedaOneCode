/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Config4905;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenClimberCommands.StopClimber;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.driveTrainCommands.TeleOpCommand;
import frc.robot.commands.showBotCannon.AdjustElevation;
import frc.robot.commands.showBotCannon.ResetCannon;
import frc.robot.commands.topGunFeederCommands.StopFeeder;
import frc.robot.commands.topGunIntakeCommands.RetractAndStopIntake;
import frc.robot.commands.topGunShooterCommands.DefaultShooterAlignment;
import frc.robot.commands.topGunShooterCommands.StopShooter;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billArmRotate.MockBillArmRotate;
import frc.robot.subsystems.billArmRotate.RealBillArmRotate;
import frc.robot.subsystems.billClimber.BillClimberBase;
import frc.robot.subsystems.billClimber.MockBillClimber;
import frc.robot.subsystems.billClimber.RealBillClimber;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billEndEffectorPosition.MockBillEndEffectorPosition;
import frc.robot.subsystems.billEndEffectorPosition.RealBillEndEffectorPosition;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billFeeder.MockBillFeeder;
import frc.robot.subsystems.billFeeder.RealBillFeeder;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.subsystems.billShooter.MockBillShooter;
import frc.robot.subsystems.billShooter.RealBillShooter;
import frc.robot.subsystems.compressor.CompressorBase;
import frc.robot.subsystems.compressor.MockCompressor;
import frc.robot.subsystems.compressor.RealCompressor;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveTrain;
import frc.robot.subsystems.drivetrain.tankDriveTrain.MockTankDriveTrain;
import frc.robot.subsystems.drivetrain.tankDriveTrain.RomiTankDriveTrain;
import frc.robot.subsystems.drivetrain.tankDriveTrain.SparkMaxTankDriveTrain;
import frc.robot.subsystems.ledlights.BillsLEDs;
import frc.robot.subsystems.ledlights.LEDs;
import frc.robot.subsystems.ledlights.WS2812LEDs;
import frc.robot.subsystems.showBotAudio.MockShowBotAudio;
import frc.robot.subsystems.showBotAudio.RealShowBotAudio;
import frc.robot.subsystems.showBotAudio.ShowBotAudioBase;
import frc.robot.subsystems.showBotCannon.CannonBase;
import frc.robot.subsystems.showBotCannon.MockCannon;
import frc.robot.subsystems.showBotCannon.RealCannon;
import frc.robot.subsystems.showBotCannonElevator.CannonElevatorBase;
import frc.robot.subsystems.showBotCannonElevator.MockCannonElevator;
import frc.robot.subsystems.showBotCannonElevator.RealCannonElevator;
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
import frc.robot.telemetries.Trace;

public class SubsystemsContainer {

  // Declare member variables.
  DriveTrainBase m_driveTrain;
  LEDs m_leds;
  LEDs m_leftLeds;
  LEDs m_rightLeds;
  LEDs m_ws2812LEDs;
  CompressorBase m_compressor;
  CannonBase m_showBotCannon;
  CannonElevatorBase m_showBotCannonElevator;
  ShowBotAudioBase m_showBotAudio;
  ShooterWheelBase m_topShooterWheel;
  ShooterWheelBase m_bottomShooterWheel;
  IntakeBase m_intake;
  FeederBase m_feeder;
  ShooterAlignmentBase m_shooterAlignment;
  BillShooterBase m_billShooter;
  BillFeederBase m_billFeeder;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillClimberBase m_billClimber;

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
    if (Config4905.getConfig4905().doesTankDrivetrainExist()) {
      Trace.getInstance().logInfo("Using real Drive Train.");
      if (Config4905.getConfig4905().getDrivetrainConfig().getString("motorController")
          .equals("sparkMax")) {
        Trace.getInstance().logInfo("Using real sparkMax Drive Train");
        m_driveTrain = new SparkMaxTankDriveTrain();
      } else if (Config4905.getConfig4905().getDrivetrainConfig().getString("motorController")
          .equals("romiDrive")) {
        Trace.getInstance().logInfo("Using Romi drive train");
        m_driveTrain = new RomiTankDriveTrain();
      } else {
        String drivetrainType = Config4905.getConfig4905().getDrivetrainConfig()
            .getString("motorController");
        throw (new RuntimeException(
            "ERROR: Unknown drivetrain type: " + drivetrainType + " in drivetrain.conf"));
      }
      m_driveTrain.init();

    } else if (Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
      Trace.getInstance().logInfo("Using swerve drive train.");
      m_driveTrain = new SwerveDriveTrain();
      m_driveTrain.init();

    } else {
      Trace.getInstance().logInfo("Using mock Drive Train.");
      m_driveTrain = new MockTankDriveTrain();
      m_driveTrain.init();
    }

    if (Config4905.getConfig4905().doesLeftLEDExist()) {
      Trace.getInstance().logInfo("Using Real Left LEDs");
      m_leftLeds = new BillsLEDs(Config4905.getConfig4905().getLeftLEDConfig(), m_driveTrain);
    }
    if (Config4905.getConfig4905().doesRightLEDExist()) {
      Trace.getInstance().logInfo("Using Real Right LEDs");
      m_rightLeds = new BillsLEDs(Config4905.getConfig4905().getRightLEDConfig(), m_driveTrain);
    }
    if (Config4905.getConfig4905().doesLEDExist()) {
      Trace.getInstance().logInfo("Using Real LEDs");
      m_leds = new BillsLEDs(Config4905.getConfig4905().getLEDConfig(), m_driveTrain);
    }
    if (Config4905.getConfig4905().doesWS2812LEDsExist()) {
      Trace.getInstance().logInfo("Using WS2812 LEDs");
      m_ws2812LEDs = new WS2812LEDs(Config4905.getConfig4905().getWS2812LEDsConfig(), m_driveTrain);
    }
    if (Config4905.getConfig4905().doesCompressorExist()) {
      Trace.getInstance().logInfo("using real Compressor.");
      m_compressor = new RealCompressor();
      m_compressor.start();
    } else {
      Trace.getInstance().logInfo("Using mock Compressor");
      m_compressor = new MockCompressor();
    }
    if (Config4905.getConfig4905().doesShowBotCannonExist()) {
      Trace.getInstance().logInfo("using real showBotCannon.");
      m_showBotCannon = new RealCannon(m_compressor);
    } else {
      Trace.getInstance().logInfo("Using mock showBotCannon");
      m_showBotCannon = new MockCannon();
    }
    if (Config4905.getConfig4905().doesShowBotCannonElevatorExist()) {
      Trace.getInstance().logInfo("using real Cannon elevator.");
      m_showBotCannonElevator = new RealCannonElevator();
    } else {
      Trace.getInstance().logInfo("Using mock Cannon elevator");
      m_showBotCannonElevator = new MockCannonElevator();
    }
    if (Config4905.getConfig4905().doesShowBotAudioExist()) {
      Trace.getInstance().logInfo("Using real showBotAudio");
      m_showBotAudio = new RealShowBotAudio();
    } else {
      Trace.getInstance().logInfo("Using mock showBotAudio");
      m_showBotAudio = new MockShowBotAudio();
    }
    if (Config4905.getConfig4905().doesShooterExist()) {
      Trace.getInstance().logInfo("using real shooters");
      m_topShooterWheel = new TopShooterWheel();
      m_bottomShooterWheel = new BottomShooterWheel();
      m_shooterAlignment = new ShooterAlignment();
    } else {
      Trace.getInstance().logInfo("using mock shooters");
      m_topShooterWheel = new MockTopShooter();
      m_bottomShooterWheel = new MockBottomShooter();
      m_shooterAlignment = new MockShooterAlignment();
    }
    if (Config4905.getConfig4905().doesIntakeExist()) {
      Trace.getInstance().logInfo("using real intake");
      m_intake = new RealIntake();
    } else {
      Trace.getInstance().logInfo("using mock Intake");
      m_intake = new MockIntake();
    }
    if (Config4905.getConfig4905().doesFeederExist()) {
      Trace.getInstance().logInfo("using real feeder");
      m_feeder = new RealFeeder();
    } else {
      Trace.getInstance().logInfo("using mock feeder");
      m_feeder = new MockFeeder();
    }
    if (Config4905.getConfig4905().doesBillFeederExist()) {
      Trace.getInstance().logInfo("using real Billthoven feeder");
      m_billFeeder = new RealBillFeeder();
    } else {
      Trace.getInstance().logInfo("using mock Billthoven feeder");
      m_billFeeder = new MockBillFeeder();
    }
    if (Config4905.getConfig4905().doesBillShooterExist()) {
      Trace.getInstance().logInfo("using real Billthoven shooter");
      m_billShooter = new RealBillShooter();
    } else {
      Trace.getInstance().logInfo("using mock Billthoven shooter");
      m_billShooter = new MockBillShooter();
    }
    if (Config4905.getConfig4905().doesEndEffectorExist()) {
      Trace.getInstance().logInfo("using real Billthoven end effector");
      m_endEffector = new RealBillEndEffectorPosition(m_compressor);
    } else {
      Trace.getInstance().logInfo("using mock Billthoven end effector");
      m_endEffector = new MockBillEndEffectorPosition();
    }
    if (Config4905.getConfig4905().doesArmRotateExist()) {
      Trace.getInstance().logInfo("using real Billthoven arm rotate");
      m_armRotate = new RealBillArmRotate(m_compressor);
    } else {
      Trace.getInstance().logInfo("using mock Billthoven arm rotate");
      m_armRotate = new MockBillArmRotate();
    }
    if (Config4905.getConfig4905().doesBillClimberExist()) {
      Trace.getInstance().logInfo("using real Billthoven climber");
      m_billClimber = new RealBillClimber();
    } else {
      Trace.getInstance().logInfo("using mock Billthoven climber");
      m_billClimber = new MockBillClimber();
    }
  }

  public DriveTrainBase getDriveTrain() {
    return m_driveTrain;
  }

  public CompressorBase getCompressor() {
    return m_compressor;
  }

  public CannonBase getShowBotCannon() {
    return m_showBotCannon;
  }

  public CannonElevatorBase getShowBotCannonElevator() {
    return m_showBotCannonElevator;
  }

  public ShowBotAudioBase getShowBotAudio() {
    return m_showBotAudio;
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

  public BillShooterBase getBillShooter() {
    return m_billShooter;
  }

  public BillFeederBase getBillFeeder() {
    return m_billFeeder;
  }

  public BillArmRotateBase getBillArmRotate() {
    return m_armRotate;
  }

  public BillEndEffectorPositionBase getBillEffectorPosition() {
    return m_endEffector;
  }

  public BillClimberBase getBillClimber() {
    return m_billClimber;
  }

  public LEDs getWs2812LEDs(){
    return m_ws2812LEDs;
  }

  public void setDefaultCommands() {
    if (Config4905.getConfig4905().doesTankDrivetrainExist()) {
      m_driveTrain.setDefaultCommand(new TeleOpCommand());
    } else {
      if (Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
        m_driveTrain.setDefaultCommand(new TeleOpCommand(() -> false));
      }
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
    if (Config4905.getConfig4905().doesShowBotCannonExist()) {
      m_showBotCannon.setDefaultCommand(new ResetCannon());
    }
    if (Config4905.getConfig4905().doesShowBotCannonElevatorExist()) {
      m_showBotCannonElevator.setDefaultCommand(new AdjustElevation(m_showBotCannonElevator));
    }
    if (Config4905.getConfig4905().doesBillClimberExist()) {
      m_billClimber.setDefaultCommand(new StopClimber(m_billClimber));
    }

    if (Config4905.getConfig4905().isBillthoven()) {
      if (Config4905.getConfig4905().doesArmRotateExist()) {
        m_armRotate.setDefaultCommand(new ArmRotate(m_armRotate, () -> 333, false, true));
      }
      if (Config4905.getConfig4905().doesEndEffectorExist()) {
        m_endEffector.setDefaultCommand(new MoveEndEffector(m_endEffector, () -> false, false));
      }
    }

  }
}
