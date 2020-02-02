package frc.robot.sensors.ballfeedersensor;

public abstract class BallFeederSensorBase {

  abstract void getSensorReading();

  public abstract boolean[] isThereBall();

  public abstract boolean isBall(EnumBallLocation location);

  public abstract int getNumberOfPowerCellsInFeeder();
}