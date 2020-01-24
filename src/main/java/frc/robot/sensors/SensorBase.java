package frc.robot.sensors;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public abstract class SensorBase {

  /**
   * This method will put readings from the sensor on Live Window. In order to
   * fill out this method add the following line as many times as needed:
   * super.putReadingOnLiveWindow(subsystemNameParam, sensorNameParam +
   * "NameOfMeasurement:", this::methodForGettingMeasurement);
   * 
   * @param subsystemNameParam Name of the subsystem to put on livewindow
   * @param sensorNameParam Name of the sensor to put on LiveWindow
   */
  public abstract void putSensorOnLiveWindow(String subsystemNameParam, String sensorNameParam);

  /**
   * Puts the double supplier on live window with the name "readingName" in the
   * subsystem specified
   * 
   * @param subsystem
   * @param readingName
   * @param doubleSupplier
   */
  protected void putReadingOnLiveWindow(String subsystem, String readingName, DoubleSupplier doubleSupplier) {
    Sendable sendable = new Sendable() {

      @Override
      public String getName() {
        return readingName;
      }

      @Override
      public void setName(String name) {

      }

      @Override
      public String getSubsystem() {
        return subsystem;
      }

      @Override
      public void setSubsystem(String subsystem) {

      }

      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Counter");
        // This needs to be value in order to work; Value is a magical string
        // that allows this counter to appear on Live Window.
        // WPILib are the people who decided on these magical strings...
        builder.addDoubleProperty("Value", doubleSupplier, null);
      }

    };

  }

  /**
   * Puts the boolean supplier on live window with the name "readingName" in the
   * subsystem specified
   * 
   * @param subsystem
   * @param readingName
   * @param booleanSupplier
   */
  protected void putReadingOnLiveWindow(String subsystem, String readingName, BooleanSupplier booleanSupplier) {
    Sendable sendable = new Sendable() {

      @Override
      public String getName() {
        return readingName;
      }

      @Override
      public void setName(String name) {

      }

      @Override
      public String getSubsystem() {
        return subsystem;
      }

      @Override
      public void setSubsystem(String subsystem) {

      }

      @Override
      public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Digital Input");
        // This needs to be value in order to work; Value is a magical string
        // that allows this Digital Input to appear on Live Window.
        // WPILib are the people who decided on these magical strings...
        builder.addBooleanProperty("Value", booleanSupplier, null);
      }

    };
  }

}