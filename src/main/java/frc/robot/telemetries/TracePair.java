package frc.robot.telemetries;

public class TracePair {
  public TracePair(String columnName, double value) {
    m_columnName = columnName;
    m_value = value;
  }

  public String getColumnName() {
    return m_columnName;
  }

  public double getValue() {
    return m_value;
  }

  private String m_columnName;
  private double m_value;
}
