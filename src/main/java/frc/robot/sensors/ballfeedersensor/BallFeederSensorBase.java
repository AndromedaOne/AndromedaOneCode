package frc.robot.sensors.ballfeedersensor;

public abstract class BallFeederSensorBase {

    abstract void getSensorReading();

    abstract boolean isBall(EnumBallLocation location);
}