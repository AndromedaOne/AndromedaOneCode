package frc.robot.subsystems.shooter;

import java.util.Iterator;
import java.util.List;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.lib.interpolate.InterpolatingDouble;
import frc.robot.lib.interpolate.InterpolatingTreeMap;

public class ShooterMap {
  private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> m_shooterRPMMap = new InterpolatingTreeMap<>();
  private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> m_shooterFeedForwardMap = new InterpolatingTreeMap<>();
  private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> m_shooterToleranceMap = new InterpolatingTreeMap<>();

  public class ShootingParameters {
    public double rpm = 0;
    public double feedForward = 0;
    public double tolerance = 0;
  }

  public ShooterMap() {
    if (!Config4905.getConfig4905().doesShooterExist()) {
      m_shooterRPMMap.put(new InterpolatingDouble(1.0), new InterpolatingDouble(1.0));
      m_shooterToleranceMap.put(new InterpolatingDouble(1.0), new InterpolatingDouble(1.0));
      m_shooterFeedForwardMap.put(new InterpolatingDouble(1.0), new InterpolatingDouble(1.0));
      return;
    }

    Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();

    m_shooterRPMMap = createMap(m_shooterConfig.getDoubleList("shootingRPMMap"),
        m_shooterConfig.getDouble("rpmtranslation"));
    m_shooterFeedForwardMap = createMap(m_shooterConfig.getDoubleList("shootingFeedForwardMap"));
    m_shooterToleranceMap = createMap(m_shooterConfig.getDoubleList("shootingFeedForwardMap"));
  }

  /**
   * This will create a tree map using a double list
   * 
   * @param list        This should be an even numbered double list
   * @param translation This will scale all the values of the map by adding to all
   *                    the values inputed into the map
   */
  private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> createMap(List<Double> list,
      double translation) {
    Iterator<Double> it = list.iterator();
    InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> newMap = new InterpolatingTreeMap<>();

    while (it.hasNext()) {
      double nextValue = it.next();
      if (!it.hasNext()) {
        System.err.println("WARN: Uneven Number of Map Values in Config");
        break;
      }
      newMap.put(new InterpolatingDouble(nextValue), new InterpolatingDouble(it.next() + translation));
    }
    return newMap;
  }

  /**
   * This will create a tree map using a double list
   * 
   * @param list This should be an even numbered double list
   */
  private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> createMap(List<Double> list) {
    return createMap(list, 0);
  }

  /**
   * This method returns the interpolated rpm with a distance based off the
   * shooter map inside the config
   * 
   * @param distance Distance in inches to the target
   * @return
   */
  public double getInterpolatedRPM(double distance) {
    return m_shooterRPMMap.getInterpolated(new InterpolatingDouble(distance)).value
        + RunShooterWheelVelocity.getManualShooterAdjustment();
  }

  /**
   * This method returns the interpolated feedforward with a given rpm based off
   * the shooter map inside the config
   * 
   * @param rpm Rpm of the shooter
   * @return
   */
  public double getInterpolatedFeedForward(double rpm) {
    return m_shooterFeedForwardMap.getInterpolated(new InterpolatingDouble(rpm)).value;
  }

  /**
   * This method returns the interpolated tolerance with a given distance based
   * off the shooter map inside the config
   * 
   * @param distance Distance in inches to the target
   * @return
   */
  public double getInterpolatedTolerance(double distance) {
    return m_shooterToleranceMap.getInterpolated(new InterpolatingDouble(distance)).value;
  }

  /**
   * Takes in a distance and calculates all the shooting parameters.
   * This includes the rpm, tolerance and the feedforward values.
   * @param distance in inches
   * @return
   */
  public ShootingParameters getShootingParameters(double distance) {
    ShootingParameters parameters = new ShootingParameters();
    double rpm = getInterpolatedRPM(distance);
    parameters.rpm = rpm;
    parameters.tolerance = getInterpolatedTolerance(distance);
    parameters.feedForward = getInterpolatedFeedForward(rpm);

    return parameters;
  }
}