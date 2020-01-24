package frc.robot.sensors;

import java.util.TimerTask;

import com.kauailabs.navx.frc.AHRS;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class NavXGyroSensor extends SensorBase {
  AHRS gyro; /* Alternatives: SPI.Port.kMXP, I2C.Port.kMXP or SerialPort.Port.kUSB */
  static NavXGyroSensor instance;
  private double initialZAngleReading = 0.0;
  private double initialXAngleReading = 0.0;
  private double initialYAngleReading = 0.0;
  boolean angleReadingSet = false;
  private long kInitializeDelay = 3000;
  private long kDefaultPeriod = 50;
  private java.util.Timer controlLoop;
  private double robotAngleCount = 0;

  /**
   * Trys creating the gyro and if it can not then it reports an error to the
   * DriveStation.
   */
  private NavXGyroSensor() {
    try {
      /* Communicate w/navX MXP via the MXP SPI Bus. */
      /* Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB */
      /*
       * See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for
       * details.
       */
      Config conf = Robot.getConfig();
      Config navXConfig = conf.getConfig("sensors.navx");
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
      SetInitialAngleReading task = new SetInitialAngleReading();
      controlLoop.schedule(task, kInitializeDelay, kDefaultPeriod);

    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    }
  }

  private class SetInitialAngleReading extends TimerTask {

    @Override
    public void run() {
      System.out.println("Setting Initial Gyro Angle");
      if (!isCalibrating()) {
        initialZAngleReading = gyro.getAngle();
        initialXAngleReading = gyro.getPitch();
        initialYAngleReading = gyro.getRoll();
        cancel();
      }
    }
  }

  /**
   * Gets the instance of the NavXGyroSensor.
   * 
   * @return instance
   */
  public static NavXGyroSensor getInstance() {
    if (instance == null) {
      System.out.println("Creating NavX gyro");
      instance = new NavXGyroSensor();
    }
    return instance;
  }

  public boolean isCalibrating() {
    return gyro.isCalibrating();
  }

  /**
   * Gets the Z angle and supbracts the initial angle member variable from it.
   * 
   * @return gyro.getAngle() - initialAngleReading
   */
  public double getZAngle() {
    double correctedAngle = gyro.getAngle() - initialZAngleReading;
    if ((robotAngleCount % 10) == 0) {
      SmartDashboard.putNumber("Raw Angle", gyro.getAngle());
      SmartDashboard.putNumber("Get Robot Angle", correctedAngle);
    }
    robotAngleCount++;
    Trace.getInstance().addTrace(false, "Gyro", new TracePair<>("Raw Angle", gyro.getAngle()),
        new TracePair<>("Corrected Angle", correctedAngle));

    return correctedAngle;
  }

  public double getXAngle() {
    double xAngle = gyro.getPitch() - initialXAngleReading;
    SmartDashboard.putNumber("Pitch Angle", xAngle);
    return xAngle;
  }

  public double getYAngle() {
    return gyro.getRoll() - initialYAngleReading;
  }

  /**
   * Returns the current compass heading of the robot Between 0 - 360
   */
  public double getCompassHeading() {
    double correctedAngle = getZAngle() % 360;
    if (correctedAngle < 0) {
      correctedAngle += 360;
    }
    return correctedAngle;
  }

  @Override
  public void putSensorOnLiveWindow(String subsystemNameParam, String sensorNameParam) {
    super.putReadingOnLiveWindow(subsystemNameParam, sensorNameParam + "ZAngle:", this::getZAngle);
  }

}
