package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SparkMaxController;

public abstract class ClimberBase extends SubsystemBase {

  public ClimberBase() {
  }

  public abstract void extendFrontLeftArm();

  public abstract void extendBackLeftArm();

  public abstract void extendFrontRightArm();

  public abstract void extendBackRightArm();

  public abstract void retractFrontLeftArm();

  public abstract void retractBackLeftArm();

  public abstract void retractFrontRightArm();

  public abstract void retractBackRightArm();

  public abstract void driveFrontLeftWinch();

  public abstract void driveBackLeftWinch();

  public abstract void driveFrontRightWinch();

  public abstract void driveBacktRightWinch();

  public abstract void stopFrontLeftWinch();

  public abstract void stopBackLeftWinch();

  public abstract void stopFrontRightWinch();

  public abstract void stopBacktRightWinch();

  public abstract SparkMaxController getBackLeftWinch();

  public abstract SparkMaxController getBackRightWinch();
}
