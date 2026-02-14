package frc.robot.sensors.gyro;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Config4905;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AngleConversionUtils;

public class RealPigeonGyroSensor extends RealGyroBase {
  // use singleton for the gyro member
  static private Pigeon2 m_gyro = null;

  /**
   * Trys creating the gyro and if it can not then it reports an error to the
   * DriveStation.
   */
  public RealPigeonGyroSensor() {
    // if we have not created the gyro, do it now...
    if (m_gyro == null) {
      try {
        /* Communicate w/pigeon MXP via the MXP SPI Bus. */
        /*
         * Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB
         */
        /*
         * See http://pigeon-mxp.kauailabs.com/guidance/selecting-an-interface/ for
         * details.
         */
        Config conf = Config4905.getConfig4905().getSensorConfig();
        Config pigeonConfig = conf.getConfig("pigeon");
        int pigeonId = pigeonConfig.getInt("id");
        Trace.getInstance().logInfo("Creating a pigeon Gyro on port: " + pigeonId);
        /* Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB */
        m_gyro = new Pigeon2(pigeonId, new CANBus("rio"));
        Trace.getInstance().logInfo("Created pigeon instance");
        Trace.getInstance().logInfo("Setting Initial Gyro Angle");
        setInitialZAngleReading(getCorrectedZAngle());
        setInitialYAngleReading(m_gyro.getPitch().getValueAsDouble());
        setInitialXAngleReading(m_gyro.getRoll().getValueAsDouble());
        Trace.getInstance()
            .logInfo("Gyro is calibrated. Initial Angles: \n\tZangle: " + getCorrectedZAngle()
                + "\n\tXangle: " + m_gyro.getPitch() + "\n\tYangle: " + m_gyro.getRoll() + "\n");
      } catch (RuntimeException ex) {
        DriverStation.reportError("Error instantiating pigeon: " + ex.getMessage(), true);
      }
    }
  }

  @Override
  public double getRawZAngle() {
    return getCorrectedZAngle();
  }

  @Override
  public double getRawXAngle() {
    return m_gyro.getPitch().getValueAsDouble();
  }

  @Override
  public double getRawYAngle() {
    return m_gyro.getRoll().getValueAsDouble();
  }

  @Override
  public void reset() {
    m_gyro.reset();

  }

  @Override
  public double getAngle() {
    return getZAngle();
  }

  @Override
  public double getRate() {
    // this is not used so there will be errors I was too lazy to fix
    // errors will be related to CCW+ vs CW+ stuff
    // the original method (getRate) was CW+ but this one is CCW+
    return m_gyro.getAngularVelocityZWorld().getValueAsDouble();
  }

  @Override
  public void close() throws Exception {

  }

  @Override
  public boolean getIsCalibrated() {
    return true;
  }

  private double getCorrectedZAngle() {
    return 360 - AngleConversionUtils.turn180AnglesInto360(m_gyro.getRotation2d().getDegrees());
  }

  @Override
  public void calibrate() {
  }

}
