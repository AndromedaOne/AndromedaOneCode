// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.gyro;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDevice.Direction;
import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.wpilibj.DriverStation;

/** Add your docs here. */
public class RomiGyro extends RealGyroBase {
  private class RomiGyroInst {
    private SimDouble m_simRateX = null;
    private SimDouble m_simRateY = null;
    private SimDouble m_simRateZ = null;
    private SimDouble m_simAngleX = null;
    private SimDouble m_simAngleY = null;
    private SimDouble m_simAngleZ = null;

    public RomiGyroInst() {
      SimDevice gyroSimDevice = SimDevice.create("Gyro:RomiGyro");
      if (gyroSimDevice != null) {
        gyroSimDevice.createBoolean("init", Direction.kOutput, true);
        m_simRateX = gyroSimDevice.createDouble("rate_x", Direction.kInput, 0.0);
        m_simRateY = gyroSimDevice.createDouble("rate_y", Direction.kInput, 0.0);
        m_simRateZ = gyroSimDevice.createDouble("rate_z", Direction.kInput, 0.0);

        m_simAngleX = gyroSimDevice.createDouble("angle_x", Direction.kInput, 0.0);
        m_simAngleY = gyroSimDevice.createDouble("angle_y", Direction.kInput, 0.0);
        m_simAngleZ = gyroSimDevice.createDouble("angle_z", Direction.kInput, 0.0);

        setInitialXAngleReading(m_simAngleX.get());
        setInitialYAngleReading(m_simAngleY.get());
        setInitialZAngleReading(m_simAngleZ.get());
        System.out.println("Created RomiGyro");
      }
      if ((gyroSimDevice == null) || (m_simAngleX == null) || (m_simAngleY == null)
          || (m_simAngleZ == null) || (m_simRateX == null) || (m_simRateY == null)
          || (m_simRateZ == null)) {
        DriverStation.reportError("Error instantiating RomiGyro internals: ", true);
        throw new RuntimeException("ERROR: could not create internal RomiGyro");
      }
    }

    public double getRawZAngle() {
      return m_simAngleZ.get();
    }

    public double getRawXAngle() {
      return m_simAngleX.get();
    }

    public double getRawYAngle() {
      return m_simAngleY.get();
    }

    /**
     * Get the rate of turn in degrees-per-second around the X-axis.
     *
     * @return rate of turn in degrees-per-second
     */
    public double getRateX() {
      return m_simRateX.get();
    }

    /**
     * Get the rate of turn in degrees-per-second around the Y-axis.
     *
     * @return rate of turn in degrees-per-second
     */
    public double getRateY() {
      return m_simRateY.get();
    }

    /**
     * Get the rate of turn in degrees-per-second around the Z-axis.
     *
     * @return rate of turn in degrees-per-second
     */
    public double getRateZ() {
      return m_simRateZ.get();
    }
  }

  private static RomiGyroInst m_gyro = null;

  /** Create a new RomiGyro. */
  public RomiGyro() {
    if (m_gyro == null) {
      m_gyro = new RomiGyroInst();
    }
  }

  @Override
  public double getRawXAngle() {
    return m_gyro.getRawXAngle();
  }

  @Override
  public double getRawYAngle() {
    return m_gyro.getRawYAngle();
  }

  @Override
  public double getRawZAngle() {
    return m_gyro.getRawZAngle();
  }

  /**
   * Get the rate of turn in degrees-per-second around the X-axis.
   *
   * @return rate of turn in degrees-per-second
   */
  public double getRateX() {
    return m_gyro.getRateX();
  }

  /**
   * Get the rate of turn in degrees-per-second around the Y-axis.
   *
   * @return rate of turn in degrees-per-second
   */
  public double getRateY() {
    return m_gyro.getRateY();
  }

  /**
   * Get the rate of turn in degrees-per-second around the Z-axis.
   *
   * @return rate of turn in degrees-per-second
   */
  public double getRateZ() {
    return m_gyro.getRateZ();
  }

  @Override
  public void calibrate() {
  }

  @Override
  public void reset() {
  }

  @Override
  public double getAngle() {
    return getZAngle();
  }

  @Override
  public double getRate() {
    return 0;
  }

  @Override
  public void close() throws Exception {
  }

  @Override
  public boolean getIsCalibrated() {
    return true;
  }

}
