/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Config4905;
import frc.robot.commands.driveTrainCommands.TeleOpCommand;
import frc.robot.commands.showBotCannon.AdjustElevation;
import frc.robot.commands.showBotCannon.ResetCannon;
import frc.robot.subsystems.compressor.CompressorBase;
import frc.robot.subsystems.compressor.MockCompressor;
import frc.robot.subsystems.compressor.RealCompressor;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveTrain;
import frc.robot.subsystems.drivetrain.tankDriveTrain.MockTankDriveTrain;
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

  public LEDs getWs2812LEDs() {
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
    if (Config4905.getConfig4905().doesShowBotCannonExist()) {
      m_showBotCannon.setDefaultCommand(new ResetCannon());
    }
    if (Config4905.getConfig4905().doesShowBotCannonElevatorExist()) {
      m_showBotCannonElevator.setDefaultCommand(new AdjustElevation(m_showBotCannonElevator));
    }
  }
}
