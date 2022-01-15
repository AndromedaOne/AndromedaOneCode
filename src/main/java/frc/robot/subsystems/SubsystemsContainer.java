/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Config4905;
import frc.robot.actuators.ServoMotor;
import frc.robot.commands.TeleOpCommand;
import frc.robot.commands.romiCommands.romiBallMopper.ResetBallMopper;
import frc.robot.commands.showBotCannon.AdjustElevation;
import frc.robot.subsystems.compressor.CompressorBase;
import frc.robot.subsystems.compressor.MockCompressor;
import frc.robot.subsystems.compressor.RealCompressor;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.MockDriveTrain;
import frc.robot.subsystems.drivetrain.RomiDriveTrain;
import frc.robot.subsystems.drivetrain.SparkMaxDriveTrain;
import frc.robot.subsystems.drivetrain.TalonSRXDriveTrain;
import frc.robot.subsystems.ledlights.*;
import frc.robot.subsystems.romiBallMopper.MockRomiBallMopper;
import frc.robot.subsystems.romiBallMopper.RealRomiBallMopper;
import frc.robot.subsystems.romiBallMopper.RomiBallMopperBase;
import frc.robot.subsystems.romiwings.MockRomiWings;
import frc.robot.subsystems.romiwings.RealRomiWings;
import frc.robot.subsystems.romiwings.RomiWingsBase;
import frc.robot.subsystems.showBotCannon.CannonBase;
import frc.robot.subsystems.showBotCannon.MockCannon;
import frc.robot.subsystems.showBotCannon.RealCannon;

public class SubsystemsContainer {

  // Declare member variables.
  DriveTrain m_driveTrain;
  LEDs m_leds;
  ServoMotor m_romiIntake;
  ServoMotor m_conveyor;
  Boolean m_conveyorState;
  double m_conveyorSpeed;
  RomiWingsBase m_romiWings;
  CompressorBase m_compressor;
  CannonBase m_cannon;
  RomiBallMopperBase m_romiBallMopper;

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
      m_leds = new RealLEDs("LEDStringOne");
    } else {
      m_leds = new MockLEDs();
    }

    // 7. Romi Intake
    if (Config4905.getConfig4905().doesHarvesterExist()) {
      m_romiIntake = new ServoMotor(Config4905.getConfig4905().getHarvesterConfig()
          .getConfig("combineHarvesterServo").getInt("port"));
    }

    // 8. Romi Wings
    if (Config4905.getConfig4905().doesRomiWingsExist()) {
      m_romiWings = new RealRomiWings();
    } else {
      m_romiWings = new MockRomiWings();
    }

    // 8. Romi Conveyor
    if (Config4905.getConfig4905().doesConveyorExist()) {
      m_conveyor = new ServoMotor(
          Config4905.getConfig4905().getConveyorConfig().getConfig("conveyorServo").getInt("port"));
      // True means conveyor is running
      m_conveyorState = false;
      m_conveyorSpeed = 0.0;

    }
    if (Config4905.getConfig4905().doesCompressorExist()) {
      System.out.println("using real Compressor.");
      m_compressor = new RealCompressor();
      m_compressor.start();
    } else {
      System.out.println("Using mock Compressor");
      m_compressor = new MockCompressor();
    }
    if (Config4905.getConfig4905().doesCannonExist()) {
      System.out.println("using real Cannon.");
      m_cannon = new RealCannon();
    } else {
      System.out.println("Using mock Cannon");
      m_cannon = new MockCannon();
    }
    if (Config4905.getConfig4905().doesRomiBallMopperExist()) {
      m_romiBallMopper = new RealRomiBallMopper();
      System.out.println("using real Romi Ball Mopper.");
    } else {
      System.out.println("using mock Romi Ball Mopper.");
      m_romiBallMopper = new MockRomiBallMopper();
    }
  }

  public DriveTrain getDrivetrain() {
    return m_driveTrain;
  }

  public LEDs getLEDs(String name) {
    return m_leds;
  }

  public ServoMotor getRomiIntake() {
    return m_romiIntake;
  }

  public ServoMotor getConveyor() {
    return m_conveyor;
  }

  public Boolean getConveyorState() {
    return m_conveyorState;

  }

  public void setConveyorState(Boolean state) {
    m_conveyorState = state;

  }

  public CompressorBase getCompressor() {
    return m_compressor;
  }

  public CannonBase getCannon() {
    return m_cannon;
  }

  public RomiBallMopperBase getRomiBallMopper() {
    return m_romiBallMopper;
  }

  public RomiWingsBase getWings() {
    return m_romiWings;
  }

  public void setDefaultCommands() {
    if(Config4905.getConfig4905().doesDrivetrainExist()) {
      m_driveTrain.setDefaultCommand(new TeleOpCommand());
    }
    if (Config4905.getConfig4905().isShowBot()) {
      m_cannon.setDefaultCommand(new AdjustElevation(m_cannon));
    }
    if (Config4905.getConfig4905().isRomi()) {
      m_romiBallMopper.setDefaultCommand(new ResetBallMopper());
    }
  }

}
