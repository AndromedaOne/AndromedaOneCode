package frc.robot.sensors.ballfeedersensor;

public enum EnumBallLocation {
  STAGE_1_LEFT(1), STAGE_1_RIGHT(0), STAGE_1_END(3),

  STAGE_2_BEGINNING(2), STAGE_2_END_MIDDLE(7), STAGE_2_END(5),

  STAGE_2_BEGINNING_MIDDLE(6), STAGE_2_MIDDLE(4);

  private final int index;

  private EnumBallLocation(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}