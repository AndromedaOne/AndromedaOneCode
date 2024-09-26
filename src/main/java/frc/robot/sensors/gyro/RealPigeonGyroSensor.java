package frc.robot.sensors.gyro;

import java.time.Duration;
import java.time.Instant;
import java.util.TimerTask;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Config4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class RealPigeonGyroSensor extends RealGyroBase {
  // use singleton for the gyro member
  static private Pigeon2 m_gyro = null;

  private long kInitializeDelay = 3000;
  private long kDefaultPeriod = 50;
  private java.util.Timer m_controlLoop;
  private Instant m_start;
  private boolean m_calibrated = false;

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
        DriveTrainBase driveTrain;
        int pigeonId = pigeonConfig.getInt("id");
        System.out.println("Creating a pigeon Gyro on port: " + pigeonId);
        /* Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB */
        m_gyro = new Pigeon2(pigeonId, "rio");
        System.out.println("Created pigeon instance");
        calibrate();
      } catch (RuntimeException ex) {
        DriverStation.reportError("Error instantiating pigeon MXP: " + ex.getMessage(), true);
      }
    }
  }

  private class SetInitialAngleReading extends TimerTask {

    RealPigeonGyroSensor m_pigeon;

    public SetInitialAngleReading(RealPigeonGyroSensor pigeon) {
      m_pigeon = pigeon;
    }

    @Override
    public void run() {
      System.out.println("Setting Initial Gyro Angle");
      m_pigeon.setInitialZAngleReading(m_gyro.getAngle());
      m_pigeon.setInitialYAngleReading(m_gyro.getPitch().getValueAsDouble());
      m_pigeon.setInitialXAngleReading(m_gyro.getRoll().getValueAsDouble());
      m_calibrated = true;
      System.out.println("Gyro is calibrated. Initial Angles: \n\tZangle: " + m_gyro.getAngle()
          + "\n\tXangle: " + m_gyro.getPitch() + "\n\tYangle: " + m_gyro.getRoll() + "\n");
      cancel();
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
          "WARNING: pigeon gyro has not completed calibrating before getRawZangle has been called");
    }
    return m_gyro.getAngle();
  }

  @Override
  public double getRawXAngle() {
    if (!m_calibrated && (Duration.between(m_start, Instant.now()).toMillis() > 5000)) {
      System.out.println(
          "WARNING: pigeon gyro has not completed calibrating before getRawXangle has been called");
    }
    return m_gyro.getPitch().getValueAsDouble();
  }

  @Override
  public double getRawYAngle() {
    if (!m_calibrated && (Duration.between(m_start, Instant.now()).toMillis() > 5000)) {
      System.out.println(
          "WARNING: pigeon gyro has not completed calibrating before getRawYangle has been called");
    }
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
