package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ClimberBase extends SubsystemBase {

  public ClimberBase() {
  }

  public abstract void driveLeftWinch(double speed);

  public abstract void driveRightWinch(double speed);

  public abstract void unwindLeftWinch();

  public abstract void unwindRightWinch();

  public abstract void stopLeftWinch();

  public abstract void stopRightWinch();

  public abstract double getLeftWinchAdjustedEncoderValue();

  public abstract double getRightWinchAdjustedEncoderValue();

  public abstract void setWinchBrakeMode(boolean brakeOn);
}
