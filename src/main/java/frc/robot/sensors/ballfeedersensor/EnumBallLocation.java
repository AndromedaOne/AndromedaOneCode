package frc.robot.sensors.ballfeedersensor;

public enum EnumBallLocation {
  STAGE_1_LEFT(1), STAGE_1_RIGHT(0), STAGE_1_END(3),

  STAGE_2_BEGINNING(2), STAGE_2_MIDDLE(5), STAGE_2_END(6);

  private final int index;

  private EnumBallLocation(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}