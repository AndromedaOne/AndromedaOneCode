package frc.robot.sensors.ballfeedersensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;

public class RealBallFeederSensor extends BallFeederSensorBase {

  boolean isOnboard;
  private static I2C i2c;
  private byte[] dataBuffer;
  private int numSensors;
  private int detectionThreshold;

  public RealBallFeederSensor(String confString) {
    Config conf = Config4905.getConfig4905().getSensorConfig();
    Config lineConf = conf.getConfig("sensors." + confString);
    isOnboard = lineConf.getBoolean("isOnboard");
    if (isOnboard) {
      i2c = new I2C(I2C.Port.kOnboard, 2);
    } else {
      i2c = new I2C(I2C.Port.kMXP, 2);
    }
    numSensors = lineConf.getInt("numSensors");
    dataBuffer = new byte[2 * numSensors];
    detectionThreshold = lineConf.getInt("detectionThreshold");
    SmartDashboard.putRaw("ballFeederSensor", dataBuffer);
  }

  @Override
  void getSensorReading() {
    // TODO Auto-generated method stub
    i2c.readOnly(dataBuffer, 2 * numSensors);
  }

  public boolean[] isThereBall() {
    boolean[] boolBuf = new boolean[dataBuffer.length / 2];
    double[] dValues = new double[dataBuffer.length / 2];

    i2c.readOnly(dataBuffer, (numSensors * 2) - 1);
    // Step through each even-numbered element in the array
    for (int i = 0; i < dataBuffer.length / 2; i++) {
      if (dataBuffer[i * 2] >= 0) {
        dValues[i] = dataBuffer[i * 2];
      } else {
        dValues[i] = dataBuffer[i * 2] + 256;
      }

    }

    SmartDashboard.putNumberArray("ballFeederSensor", dValues);
    String values = "";
    for (int i = 0; i < dValues.length; i++) {
      values += dValues[i] + ",";
    }
    System.out.println(values);
    // Check for whether the line is found
    for (int i = 0; i < dValues.length; i++) {
      if (dValues[i] >= detectionThreshold) {
        boolBuf[i] = true;
      } else {
        boolBuf[i] = false;
      }
    }
    return boolBuf;
  }

  @Override
  boolean isBall(EnumBallLocation location) {
    // TODO Auto-generated method stub
    return false;
  }
}