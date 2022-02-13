package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SparkMaxController;

public abstract class ClimberBase extends SubsystemBase {

  public ClimberBase() {
  }

  public abstract void driveFrontLeftWinch();

  public abstract void driveBackLeftWinch();

  public abstract void driveFrontRightWinch();

  public abstract void driveBackRightWinch();

  public abstract void unwindFrontLeftWinch();

  public abstract void unwindBackLeftWinch();

  public abstract void unwindFrontRightWinch();

  public abstract void unwindBackRightWinch();

  public abstract void stopFrontLeftWinch();

  public abstract void stopBackLeftWinch();

  public abstract void stopFrontRightWinch();

  public abstract void stopBacktRightWinch();

  public abstract SparkMaxController getBackLeftWinch();

  public abstract SparkMaxController getBackRightWinch();
}
