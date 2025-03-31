/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Config4905;
import frc.robot.commands.driveTrainCommands.TeleOpCommand;
import frc.robot.commands.sbsdAlgaeManipulatorCommands.DefaultAlgaeManipulatorCommand;
import frc.robot.commands.sbsdArmCommands.ArmControlCommand;
import frc.robot.commands.sbsdArmCommands.CoralIntakeEjectDefaultCommand;
import frc.robot.commands.sbsdArmCommands.EndEffectorControlCommand;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints;
import frc.robot.commands.sbsdClimberCommands.ClimberDefaultCommand;
import frc.robot.subsystems.compressor.CompressorBase;
import frc.robot.subsystems.compressor.MockCompressor;
import frc.robot.subsystems.compressor.RealCompressor;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.MockSwerveDriveTrain;
import frc.robot.subsystems.drivetrain.swerveDriveTrain.SwerveDriveTrain;
import frc.robot.subsystems.ledlights.LEDs;
import frc.robot.subsystems.ledlights.WS2812LEDs;
import frc.robot.subsystems.sbsdAlgaeManipulator.MockSBSDAlgaeManipulator;
import frc.robot.subsystems.sbsdAlgaeManipulator.RealSBSDAlgaeManipulator;
import frc.robot.subsystems.sbsdAlgaeManipulator.SBSDAlgaeManipulatorBase;
import frc.robot.subsystems.sbsdArm.MockSBSDArm;
import frc.robot.subsystems.sbsdArm.RealSBSDArm;
import frc.robot.subsystems.sbsdArm.SBSDArmBase;
import frc.robot.subsystems.sbsdclimber.MockSBSDClimber;
import frc.robot.subsystems.sbsdclimber.RealSBSDClimber;
import frc.robot.subsystems.sbsdclimber.SBSDClimberBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;
import frc.robot.subsystems.sbsdcoralendeffector.MockCoralEndEffectorRotate;
import frc.robot.subsystems.sbsdcoralendeffector.MockCoralIntakeEject;
import frc.robot.subsystems.sbsdcoralendeffector.RealCoralEndEffectorRotate;
import frc.robot.subsystems.sbsdcoralendeffector.RealCoralIntakeEject;
import frc.robot.telemetries.Trace;

public class SubsystemsContainer {

  // Declare member variables.
  DriveTrainBase m_driveTrain;
  LEDs m_leds;
  LEDs m_leftLeds;
  LEDs m_rightLeds;
  LEDs m_ws2812LEDs;
  CompressorBase m_compressor;
  SBSDClimberBase m_sbsdClimber;
  SBSDArmBase m_sbsdArmBase;
  CoralEndEffectorRotateBase m_sbsdCoralEndEffectorRotateBase;
  CoralIntakeEjectBase m_sbsdCoralIntakeEjectBase;
  SBSDAlgaeManipulatorBase m_sbsdAlgaeManipulatorBase;

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
    if (Config4905.getConfig4905().doesSBSDClimberExist()) {
      Trace.getInstance().logInfo("using real sbsd climber");
      m_sbsdClimber = new RealSBSDClimber();
    } else {
      Trace.getInstance().logInfo("using mock sbsd climber");
      m_sbsdClimber = new MockSBSDClimber();
    }
    if (Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      Trace.getInstance().logInfo("using real SBSD coral end effector");
      m_sbsdCoralEndEffectorRotateBase = new RealCoralEndEffectorRotate();
      SBSDArmSetpoints.setUpSetpointsFromConfig();
    } else {
      Trace.getInstance().logInfo("using mock SBSD end effector");
      m_sbsdCoralEndEffectorRotateBase = new MockCoralEndEffectorRotate();
    }
    if (Config4905.getConfig4905().doesSBSDArmExist()) {
      Trace.getInstance().logInfo("using real SBSD arm");
      m_sbsdArmBase = new RealSBSDArm();
      m_sbsdArmBase.setEndEffector(m_sbsdCoralEndEffectorRotateBase);
      SBSDArmSetpoints.setUpSetpointsFromConfig();
    } else {
      Trace.getInstance().logInfo("using mock SBSD arm");
      m_sbsdArmBase = new MockSBSDArm();
    }
    if (Config4905.getConfig4905().doesSBSDCoralIntakeEjectExist()) {
      Trace.getInstance().logInfo("using real SBSD coral intake eject");
      m_sbsdCoralIntakeEjectBase = new RealCoralIntakeEject();
    } else {
      Trace.getInstance().logInfo("using mock SBSD coral intake eject");
      m_sbsdCoralIntakeEjectBase = new MockCoralIntakeEject();
    }
    if (Config4905.getConfig4905().doesSBSDAlgaeManipulatorExist()) {
      Trace.getInstance().logInfo("using real SBSD algae manipulator");
      m_sbsdAlgaeManipulatorBase = new RealSBSDAlgaeManipulator();
    } else {
      Trace.getInstance().logInfo("using mock SBSD algae manipulator");
      m_sbsdAlgaeManipulatorBase = new MockSBSDAlgaeManipulator();
    }
  }

  public DriveTrainBase getDriveTrain() {
    return m_driveTrain;
  }

  public CompressorBase getCompressor() {
    return m_compressor;
  }

  public SBSDArmBase getSBSDArmBase() {
    return m_sbsdArmBase;
  }

  public CoralEndEffectorRotateBase getSBSDCoralEndEffectorRotateBase() {
    return m_sbsdCoralEndEffectorRotateBase;
  }

  public CoralIntakeEjectBase getSBSDCoralIntakeEjectBase() {
    return m_sbsdCoralIntakeEjectBase;
  }

  public SBSDAlgaeManipulatorBase getSBSDAlgaeManipulatorBase() {
    return m_sbsdAlgaeManipulatorBase;
  }

  public SBSDClimberBase getSBSDClimberBase() {
    return m_sbsdClimber;
  }

  public LEDs getWs2812LEDs() {
    return m_ws2812LEDs;
  }

  public void setDefaultCommands() {
    if (Config4905.getConfig4905().doesSwerveDrivetrainExist()) {
      m_driveTrain.setDefaultCommand(new TeleOpCommand(() -> false));
    }
    if (Config4905.getConfig4905().doesSBSDArmExist()) {
      m_sbsdArmBase.setDefaultCommand(
          new ArmControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, false));
    }
    if (Config4905.getConfig4905().doesSBSDCoralEndEffectorExist()) {
      System.out.println("Using end effector default command");
      m_sbsdCoralEndEffectorRotateBase.setDefaultCommand(
          new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CORAL_LOAD, false));
    }
    if (Config4905.getConfig4905().doesSBSDCoralIntakeEjectExist()) {
      m_sbsdCoralIntakeEjectBase.setDefaultCommand(new CoralIntakeEjectDefaultCommand(false));
    }
    if (Config4905.getConfig4905().doesSBSDAlgaeManipulatorExist()) {
      m_sbsdAlgaeManipulatorBase.setDefaultCommand(new DefaultAlgaeManipulatorCommand());
    }
    if (Config4905.getConfig4905().doesSBSDClimberExist()) {
      m_sbsdClimber.setDefaultCommand(new ClimberDefaultCommand());
    }
  }
}
