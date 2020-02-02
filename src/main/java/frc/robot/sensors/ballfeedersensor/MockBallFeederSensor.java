package frc.robot.sensors.ballfeedersensor;

public class MockBallFeederSensor extends BallFeederSensorBase {

  @Override
  void getSensorReading() {
    // Attempting to getSensorReading from MockBallFeederSensor
  }

  @Override
  public boolean[] isThereBall() {
    // Returning an empty array, implementation of ball feeder sensor *must* check
    // to see if it is returned an empty array
    return new boolean[0];
  }

  @Override
  public boolean isBall(EnumBallLocation location) {
    return false;
  }

  @Override
  public int getNumberOfPowerCellsInFeeder() {
    // TODO Auto-generated method stub
    return 0;
  }

}