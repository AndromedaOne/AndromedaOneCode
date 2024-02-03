package frc.robot.subsystems.billClimber;

import frc.robot.subsystems.SubsystemInterface;

public interface BillClimberBase extends SubsystemInterface {

  public abstract void driveWinch(double speed);

  public abstract void unwindWinch();

  public abstract void stopWinch();

  public abstract double getWinchAdjustedEncoderValue();

  public abstract void setWinchBrakeMode(boolean brakeOn);
}