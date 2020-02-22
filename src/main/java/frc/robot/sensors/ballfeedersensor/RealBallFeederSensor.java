package frc.robot.sensors.ballfeedersensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;

public class RealBallFeederSensor extends BallFeederSensorBase {

  boolean m_isOnboard;
  private static I2C m_i2c;
  private byte[] m_dataBuffer;
  private int m_numSensors;
  private int m_detectionThreshold;
  GetSensorData m_getSensorData;

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
    m_getSensorData = new GetSensorData();
    m_getSensorData.start();
  }

  public boolean[] isThereBall() {
    boolean[] boolBuf = new boolean[m_dataBuffer.length / 2];
    double[] dValues = new double[m_dataBuffer.length / 2];

    m_dataBuffer = m_getSensorData.getCurrentDataBuffer();
    // Step through each even-numbered element in the array
    for (int i = 0; i < m_dataBuffer.length / 2; i++) {
      if (m_dataBuffer[i * 2 + 1] >= 0) {
        dValues[i] = m_dataBuffer[i * 2] * 256 + m_dataBuffer[i * 2 + 1];
      } else {
        dValues[i] = m_dataBuffer[i * 2] * 256 + m_dataBuffer[i * 2 + 1] + 256;
      }
      // System.out.println("Sensor " + i + " : " + dValues[i]);
    }

    String values = "";
    for (int i = 0; i < dValues.length; i++) {
      values += dValues[i] + ",";
    }
    // System.out.println(values.toString());

    // Check for whether the line is found
    for (int i = 0; i < dValues.length; i++) {
      if (dValues[i] <= m_detectionThreshold) {
        boolBuf[i] = true;
      } else {
        boolBuf[i] = false;
      }
    }

    SmartDashboard.putBoolean("S1L", boolBuf[EnumBallLocation.STAGE_1_LEFT.getIndex()]);
    SmartDashboard.putBoolean("S1R", boolBuf[EnumBallLocation.STAGE_1_RIGHT.getIndex()]);
    SmartDashboard.putBoolean("S1E", boolBuf[EnumBallLocation.STAGE_1_END.getIndex()]);
    SmartDashboard.putBoolean("S2B", boolBuf[EnumBallLocation.STAGE_2_BEGINNING.getIndex()]);
    SmartDashboard.putBoolean("S2BM", boolBuf[EnumBallLocation.STAGE_2_BEGINNING_MIDDLE.getIndex()]);
    SmartDashboard.putBoolean("S2M", boolBuf[EnumBallLocation.STAGE_2_MIDDLE.getIndex()]);
    SmartDashboard.putBoolean("S2EM", boolBuf[EnumBallLocation.STAGE_2_END_MIDDLE.getIndex()]);
    SmartDashboard.putBoolean("S2E", boolBuf[EnumBallLocation.STAGE_2_END.getIndex()]);

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

  private class GetSensorData extends Thread {
    private byte[] m_dataBufferNew = new byte[2 * m_numSensors];
    private byte[] m_dataBufferOld = new byte[2 * m_numSensors];

    @Override
    public void run() {
      super.run();
      while(true){
        updateNewDataBuffer();
        setOldBuffer();
      }
    }

    private void updateNewDataBuffer() {
      m_i2c.readOnly(m_dataBufferNew, (m_numSensors * 2));
    }

    private synchronized void setOldBuffer() {
      m_dataBufferOld = m_dataBufferNew.clone();
    }

    public synchronized byte[] getCurrentDataBuffer() {
      return m_dataBufferOld;
    }
  }

  
}