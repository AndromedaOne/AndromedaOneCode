package frc.robot.sensors.ballfeedersensor;

public enum EnumBallLocation {
  STAGE_2_END(0), STAGE_2_MIDDLE(2), STAGE_2_BEGINNING(4), STAGE_1_LEFT(6), STAGE_1_RIGHT(8);

  private final int index;

  private EnumBallLocation(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}