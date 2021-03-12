package frc.robot.sensors.gyro;

import java.util.TimerTask;

import com.kauailabs.navx.frc.AHRS;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import frc.robot.Config4905;

public class RealNavXGyroSensor extends Gyro {
  AHRS gyro; /* Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB */

  private long kInitializeDelay = 3000;
  private long kDefaultPeriod = 50;
  private java.util.Timer controlLoop;

  /**
   * Trys creating the gyro and if it can not then it reports an error to the
   * DriveStation.
   */
  public RealNavXGyroSensor() {
    try {
      /* Communicate w/navX MXP via the MXP SPI Bus. */
      /* Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB */
      /*
       * See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for
       * details.
       */
      Config conf = Config4905.getConfig4905().getSensorConfig();
      Config navXConfig = conf.getConfig("navx");
      String navXPort = navXConfig.getString("port");
      System.out.println("Creating a NavX Gyro on port: " + navXPort);
      if (navXPort.equals("MXP")) {
        gyro = new AHRS(SPI.Port.kMXP);
      } else if (navXPort.equals("SPI")) {
        gyro = new AHRS(SPI.Port.kOnboardCS0);
      } else {
        System.err.println("ERROR: Unkown NavX Port: " + navXPort);
        return;
      }
      System.out.println("Created NavX instance");
      // New thread to initialize the initial angle
      controlLoop = new java.util.Timer();
      SetInitialAngleReading task = new SetInitialAngleReading(this);
      controlLoop.schedule(task, kInitializeDelay, kDefaultPeriod);

    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX MXP: " + ex.getMessage(), true);
    }
  }

  private boolean calibrated = false;

  private class SetInitialAngleReading extends TimerTask {

    RealNavXGyroSensor m_navX;

    public SetInitialAngleReading(RealNavXGyroSensor navX) {
      m_navX = navX;
    }

    @Override
    public void run() {
      System.out.println("Setting Initial Gyro Angle");
      if (!gyro.isCalibrating()) {
        initialZAngleReading = gyro.getAngle();
        initialXAngleReading = gyro.getPitch();
        initialYAngleReading = gyro.getRoll();
        calibrated = true;
        cancel();
      }
    }
  }

  /**
   * Gets the Z angle and subtracts the initial angle member variable from it.
   * 
   * @return gyro.getAngle() - initialAngleReading
   */
  @Override
  public double getZAngle() {
    if (calibrated) {
      double correctedAngle = gyro.getAngle() - initialZAngleReading;
      Trace.getInstance().addTrace(true, "Gyro", new TracePair<>("Raw Angle", gyro.getAngle()),
          new TracePair<>("Corrected Angle", correctedAngle));
      return correctedAngle;
    }
    return 0;
  }

  @Override
  protected double getRawXAngle() {
    return gyro.getPitch();
  }

  @Override
  protected double getRawYAngle() {
    return gyro.getRoll();
  }

  /**
   * Returns the current compass heading of the robot Between 0 - 360
   */
  @Override
  public double getCompassHeading() {
    return AngleConversionUtils.ConvertAngleToCompassHeading(getZAngle());
  }

  @Override
  public void updateSmartDashboardReadings() {
    SmartDashboard.putNumber("Z Angle", getZAngle());
    SmartDashboard.putNumber("Robot Compass Angle", getCompassHeading());
  }

  @Override
  public void calibrate() {
    throw new RuntimeException(
        "Calibrate is not implemented in realNaNavX so you should implement it if you are trying to call it.");
  }

  @Override
  public void reset() {
    gyro.reset();

  }

  @Override
  public double getAngle() {
    return getZAngle();
  }

  @Override
  public double getRate() {
    return gyro.getRate();
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }
}
