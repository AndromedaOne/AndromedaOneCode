/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Config4905;
import frc.robot.commands.driveTrainCommands.TeleOpCommand;
import frc.robot.subsystems.compressor.CompressorBase;
import frc.robot.subsystems.compressor.MockCompressor;
import frc.robot.subsystems.compressor.RealCompressor;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.MockSwerveDriveTrain;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveTrain;
import frc.robot.subsystems.hopperbelts.HopperBeltsBase;
import frc.robot.subsystems.hopperbelts.MockHopperBelts;
import frc.robot.subsystems.hopperbelts.RealHopperBelts;
import frc.robot.subsystems.ledlights.LEDs;
import frc.robot.subsystems.ledlights.WS2812LEDs;
import frc.robot.telemetries.Trace;

public class SubsystemsContainer {

  // Declare member variables.
  DriveTrainBase m_driveTrain;
  LEDs m_leds;
  LEDs m_leftLeds;
  LEDs m_rightLeds;
  LEDs m_ws2812LEDs;
  CompressorBase m_compressor;
  HopperBeltsBase m_hopperBelts;

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
    if (Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
      Trace.getInstance().logInfo("Using swerve drive train.");
      m_driveTrain = new SwerveDriveTrain();
      m_driveTrain.init();

    } else {
      Trace.getInstance().logInfo("Using mock swerve drive Train.");
      m_driveTrain = new MockSwerveDriveTrain();
      m_driveTrain.init();
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
    if (Config4905.getConfig4905().doesHopperBeltsExist()) {
      Trace.getInstance().logInfo("using real hopper belts.");
      m_hopperBelts = new RealHopperBelts();
    } else {
      Trace.getInstance().logInfo("Using mock hopper belts");
      m_hopperBelts = new MockHopperBelts();
    }
  }

  public DriveTrainBase getDriveTrain() {
    return m_driveTrain;
  }

  public CompressorBase getCompressor() {
    return m_compressor;
  }

  public HopperBeltsBase getHopperBelts() {
    return m_hopperBelts;
  }

  public LEDs getWs2812LEDs() {
    return m_ws2812LEDs;
  }

  public void setDefaultCommands() {
    if (Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
      m_driveTrain.setDefaultCommand(new TeleOpCommand(() -> false));
    }
  }
}
