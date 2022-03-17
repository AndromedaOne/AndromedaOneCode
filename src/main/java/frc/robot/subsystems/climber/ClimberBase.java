package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ClimberBase extends SubsystemBase {

  public ClimberBase() {
  }

  public abstract void driveFrontLeftWinch();

  public abstract void driveBackLeftWinch(double speed);

  public abstract void driveFrontRightWinch();

  public abstract void driveBackRightWinch(double speed);

  public abstract void unwindFrontLeftWinch();

  public abstract void unwindBackLeftWinch();

  public abstract void unwindFrontRightWinch();

  public abstract void unwindBackRightWinch();

  public abstract void stopFrontLeftWinch();

  public abstract void stopBackLeftWinch();

  public abstract void stopFrontRightWinch();

  public abstract void stopBackRightWinch();

  public abstract boolean backLeftWinchAtTopLimitSwitch();

  public abstract boolean backLeftWinchAtBottomLimitSwitch();

  public abstract boolean backRightWinchAtTopLimitSwitch();

  public abstract boolean backRightWinchAtBottomLimitSwitch();

  public abstract double getBackLeftWinchAdjustedEncoderValue();

  public abstract double getBackRightWinchAdjustedEncoderValue();

  public abstract void setWinchBrakeMode(boolean brakeOn);
}
