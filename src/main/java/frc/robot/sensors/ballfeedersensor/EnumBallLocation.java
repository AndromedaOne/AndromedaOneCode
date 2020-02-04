package frc.robot.sensors.ballfeedersensor;

public enum EnumBallLocation {
  STAGE_2_END(0), STAGE_2_MIDDLE(2), STAGE_2_BEGINNING(7), STAGE_1_LEFT(4), STAGE_1_RIGHT(3);

  private final int index;

  private EnumBallLocation(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}