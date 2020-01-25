package frc.robot.sensors.magencodersensor;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class RealMagEncoderSensor extends MagEncoderSensor {
  private WPI_TalonSRX talonSpeedController;
  private double initialPosition = 0;

  /**
   * Sets the talonSpeedController talon to the talon passed in, configures the
   * talon's feedback sensor to be a Mag Encoder and sets the sensor Phase to be
   * true.
   * 
   * @param talon Talon object to attach encoder to
   */
  public RealMagEncoderSensor(WPI_TalonSRX talon, boolean reverseDirectionParam, boolean useAbsoluteReadings) {
    talonSpeedController = talon;
    if (!useAbsoluteReadings) {
      talonSpeedController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    } else {
      talonSpeedController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
    }
    talonSpeedController.setSensorPhase(!reverseDirectionParam); /* keep sensor and motor in phase */
  }

  @Override
  public double getDistanceTicks() {
    double ticks = getPosition();
    return ticks - initialPosition;
  }

  @Override
  public void reset() {
    initialPosition = getPosition();
  }

  @Override
  public void resetTo(double value) {
    double error = value - getDistanceTicks();
    initialPosition -= error;
  }

  private double getPosition() {
    double ticks = talonSpeedController.getSelectedSensorPosition();
    return ticks;
  }

  @Override
  public double getVelocity() {
    // multiply by ten to get the velocity in ticks per second
    double velocity = talonSpeedController.getSelectedSensorVelocity() * 10;
    return velocity;
  }

}