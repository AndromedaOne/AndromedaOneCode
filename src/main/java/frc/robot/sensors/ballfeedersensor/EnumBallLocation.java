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

  public static String prettyPrint(boolean[] sensorOutput) {
    StringBuffer buf = new StringBuffer();
    buf.append(sensorOutput[STAGE_1_LEFT.getIndex()] ? "1-L" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_1_RIGHT.getIndex()] ? "1-R" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_1_END.getIndex()] ? "1-E" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_2_BEGINNING.getIndex()] ? "2-B" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_2_BEGINNING_MIDDLE.getIndex()] ? "2MB" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_2_MIDDLE.getIndex()] ? "2-M" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_2_END_MIDDLE.getIndex()] ? "2ME" : "---");
    buf.append(" ");
    buf.append(sensorOutput[STAGE_2_END.getIndex()] ? "2-E" : "---");
    buf.append(" ");
    return buf.toString();
  }
}
