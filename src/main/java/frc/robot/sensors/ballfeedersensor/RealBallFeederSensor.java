package frc.robot.sensors.ballfeedersensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import frc.robot.Config4905;

public class RealBallFeederSensor extends BallFeederSensorBase {

  boolean m_isOnboard;
  private static I2C m_i2c;
  private byte[] m_dataBuffer;
  private int m_numSensors;
  private int m_detectionThreshold;

  public RealBallFeederSensor(String confString) {
    Config conf = Config4905.getConfig4905().getSensorConfig();
    Config lineConf = conf.getConfig("sensors." + confString);
    m_isOnboard = lineConf.getBoolean("isOnboard");
    if (m_isOnboard) {
      m_i2c = new I2C(I2C.Port.kOnboard, 2);
    } else {
      m_i2c = new I2C(I2C.Port.kMXP, 2);
    }
    m_numSensors = lineConf.getInt("numSensors");
    m_dataBuffer = new byte[2 * m_numSensors];
    m_detectionThreshold = lineConf.getInt("detectionThreshold");
  }

  @Override
  void getSensorReading() {
    m_i2c.readOnly(m_dataBuffer, 2 * m_numSensors);
  }

  public boolean[] isThereBall() {
    boolean[] boolBuf = new boolean[m_dataBuffer.length / 2];
    double[] dValues = new double[m_dataBuffer.length / 2];

    m_i2c.readOnly(m_dataBuffer, (m_numSensors * 2) - 1);
    // Step through each even-numbered element in the array
    for (int i = 0; i < m_dataBuffer.length / 2; i++) {
      if (m_dataBuffer[i * 2] >= 0) {
        dValues[i] = m_dataBuffer[i * 2];
      } else {
        dValues[i] = m_dataBuffer[i * 2] + 256;
      }

    }

    String values = "";
    for (int i = 0; i < dValues.length; i++) {
      values += dValues[i] + ",";
    }
    //System.out.println(values.toString());

    // Check for whether the line is found
    for (int i = 0; i < dValues.length; i++) {
      if (dValues[i] <= m_detectionThreshold) {
        boolBuf[i] = true;
      } else {
        boolBuf[i] = false;
      }
    }
    return boolBuf;
  }

  public int getNumberOfPowerCellsInFeeder() {
    int powerCellCount = 0;
    for (boolean isBall : isThereBall()) {
      if (isBall) {
        powerCellCount++;
      }
    }
    return powerCellCount;
  }

  @Override
  public boolean isBall(EnumBallLocation location) {
    boolean[] feederBallLocations = isThereBall();
    if (feederBallLocations[location.getIndex()] == true) {
      return true;
    } else {
      return false;
    }
  }
}