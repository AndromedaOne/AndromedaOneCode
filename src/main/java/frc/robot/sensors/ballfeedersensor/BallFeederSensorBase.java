package frc.robot.sensors.ballfeedersensor;

public abstract class BallFeederSensorBase {

  abstract void getSensorReading();

  public abstract boolean[] isThereBall();

  abstract boolean isBall(EnumBallLocation location);
}