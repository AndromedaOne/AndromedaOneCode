package frc.robot.sensors.gyro;

import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Config4905;

public class RealNavXGyroSensor extends RealGyroBase {
  // use singleton for the gyro member
  static private AHRS m_gyro = null;

  private long kInitializeDelay = 3000;
  private long kDefaultPeriod = 50;
  private java.util.Timer m_controlLoop;
  private Instant m_start;
  private boolean m_calibrated = false;

  /**
   * Trys creating the gyro and if it can not then it reports an error to the
   * DriveStation.
   */
  public RealNavXGyroSensor() {
    // if we have not created the gyro, do it now...
    if (m_gyro == null) {
      try {
        /* Communicate w/navX MXP via the MXP SPI Bus. */
        /*
         * Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB
         */
        /*
         * See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for
         * details.
         */
        Config conf = Config4905.getConfig4905().getSensorConfig();
        Config navXConfig = conf.getConfig("navx");
        String navXPort = navXConfig.getString("port");
        System.out.println("Creating a NavX Gyro on port: " + navXPort);
        /* Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB */
        if (navXPort.equals("MXP")) {
          m_gyro = new AHRS(NavXComType.kMXP_SPI);
        } else {
          System.err.println("ERROR: Unknown NavX Port: " + navXPort);
          return;
        }
        System.out.println("Created NavX instance");
        calibrate();
      } catch (RuntimeException ex) {
        DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
      }
    }
  }

  private class SetInitialAngleReading extends TimerTask {

    RealNavXGyroSensor m_navX;

    public SetInitialAngleReading(RealNavXGyroSensor navX) {
      m_navX = navX;
    }

    @Override
    public void run() {
      System.out.println("Setting Initial Gyro Angle");
      if (!m_gyro.isCalibrating()) {
        m_navX.setInitialZAngleReading(m_gyro.getAngle());
        m_navX.setInitialXAngleReading(m_gyro.getPitch());
        m_navX.setInitialYAngleReading(m_gyro.getRoll());
        m_calibrated = true;
        System.out.println("Gyro is calibrated. Initial Angles: \n\tZangle: " + m_gyro.getAngle()
            + "\n\tXangle: " + m_gyro.getPitch() + "\n\tYangle: " + m_gyro.getRoll() + "\n");
        cancel();
      }
    }
  }

  @Override
  public void calibrate() {
    m_start = Instant.now();
    m_calibrated = false;
    m_controlLoop = new java.util.Timer();
    SetInitialAngleReading task = new SetInitialAngleReading(this);
    m_controlLoop.schedule(task, kInitializeDelay, kDefaultPeriod);
  }

  @Override
  public double getRawZAngle() {
    if (!m_calibrated && (Duration.between(m_start, Instant.now()).toMillis() > 5000)) {
      System.out.println(
          "WARNING: navx gyro has not completed calibrating before getRawZangle has been called");
    }
    return m_gyro.getAngle();
  }

  @Override
  public double getRawXAngle() {
    if (!m_calibrated && (Duration.between(m_start, Instant.now()).toMillis() > 5000)) {
      System.out.println(
          "WARNING: navx gyro has not completed calibrating before getRawXangle has been called");
    }
    return m_gyro.getPitch();
  }

  @Override
  public double getRawYAngle() {
    if (!m_calibrated && (Duration.between(m_start, Instant.now()).toMillis() > 5000)) {
      System.out.println(
          "WARNING: navx gyro has not completed calibrating before getRawYangle has been called");
    }
    return m_gyro.getRoll();
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
    return m_gyro.getRate();
  }

  @Override
  public void close() throws Exception {

  }

  @Override
  public boolean getIsCalibrated() {
    return m_calibrated;
  }

}
