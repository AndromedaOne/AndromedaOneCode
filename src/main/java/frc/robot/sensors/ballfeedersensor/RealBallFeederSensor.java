package frc.robot.sensors.ballfeedersensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import frc.robot.Config4905;

public class RealBallFeederSensor extends BallFeederSensorBase {

    boolean isOnboard;
    private static I2C i2c;
    private byte[] dataBuffer;
    private int numSensors;

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
    }

    @Override
    void getSensorReading() {
        // TODO Auto-generated method stub
        i2c.readOnly(dataBuffer, 2 * numSensors);
    }

    @Override
    boolean isBall(EnumBallLocation location) {
        // TODO Auto-generated method stub
        return false;
    }
}